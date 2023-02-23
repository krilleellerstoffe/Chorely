package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Unit_QueryExecutorTest {
    public static DatabaseConnection con = null;
    private QueryExecutor queryExecutor = null;

    //This was intended for integration testing

    // Establish connection before execution

    @BeforeEach
    void setUp() {
        con= mock(DatabaseConnection.class);
        if(con == null){
          try{
              assert !con.getConnection().isClosed();
              queryExecutor = new QueryExecutor(con);
          } catch (SQLException e) {
              System.out.print("Connection wasn't established: , system closing \n" + e.getMessage());
              System.exit(0);
          }
        }
    }

    @AfterEach
    void tearDown() {
        if(con != null){
            try{
                con.closeConnection();
                assert con.getConnection().isClosed();
                con = null;
            } catch (SQLException e) {
                System.out.print("Connection didn't close: , system closing \n" + e.getMessage());
                System.exit(0);
            }
        }
    }

    /**
     * @author Hadi Saghir
     * This method tests UPDATE queries
     *
     * In order to test query we need to test
     *  -The query executes (this is done by throw exception not being invoked, only in integration testing)
     *  -Element updates according (this is done by executing a SELECT query and comparing)
     * We also need to test invalid queries and the response of the DB. This can be done by either or
     *  - testing an invalid update input, e.g. UPDATE user_id TO 123
     *  - testing invalid WHERE statement,  e.g. WHERE groupID = "TEST"
     *
     * */
    @Test
    void executeUpdateQuery() throws SQLException {
        // Mock the statement object to always succeed
        Statement statement = mock(Statement.class);
        Mockito.when(con.getConnection().createStatement()).thenReturn(statement);

        // Mock when invalid
        Mockito.when(statement.executeUpdate("UPDATE user SET name = 'Hadi' WHERE name = 'Alice'")).thenThrow(new SQLException("update failed"));

        //Ignore closing connection when called in method
        doNothing().when(con).closeConnection();

        // Insert a test row into the database
        String insertQuery = "INSERT INTO user (name) VALUES ('Alice');";
        assertDoesNotThrow(() -> queryExecutor.executeUpdateQuery(insertQuery), "executeUpdateQuery: unsert failed");

        // Test updating the rwo
        String updateQuery = "UPDATE user SET name = 'Bob' WHERE name = 'Alice';";
        assertDoesNotThrow(() -> queryExecutor.executeUpdateQuery(updateQuery), "executeUpdateQuery: update failed");

        // Test invalid where condition
        String invalidUpdateQuery = "UPDATE user SET name = 'Hadi' WHERE name = 'Alice';";
        assertThrows(SQLException.class, () -> queryExecutor.executeUpdateQuery(invalidUpdateQuery), "executeUpdateQuery: Alice wasn't updated or error undetected");

    }

    /**
     * @author Hadi Saghir
     * This method tests SELECT queries
     *
     * In order to test query we need to test both valid queries
     *  -The query executes (this is done by throw exception not being invoked, only in integration testing)
     *  -Returns:
     *      -no return (no elements match query) (recieving an empty resultSet)
     *      -with return (this is done by throw exception not being invoked)
     *
     * We also need to test invalid queries and the response of the DB. This can be done by
     *  - testing invalid WHERE statement,  e.g. WHERE groupID = "TEST"
     *  - testing invalid table,  e.g. table user instead of users
     *
     * */
    @Test
    void executeReadQuery() throws SQLException {
        // Mock the statement object to always succeed
        Statement statement = mock(Statement.class);
        when(con.getConnection().createStatement()).thenReturn(statement);

        // Mock when invalid
        when(statement.executeQuery("SELECT * FROM user WHERE name = 'Alice'")).thenThrow(new SQLException("invalid SELECT query"));

        //Ignore closing connection when called in method
        doNothing().when(con).closeConnection();


        // Insert a test row into the database
        String insertQuery = "INSERT INTO user (name) VALUES ('Alice');";
        assertDoesNotThrow(() -> queryExecutor.executeUpdateQuery(insertQuery), "executeUpdateQuery: unsert failed");

        // Test updating the two
        String readQuery = "SELECT * FROM users WHERE name = 'Alice';";
        assertDoesNotThrow(() -> queryExecutor.executeReadQuery(readQuery), "executeUpdateQuery: update failed");

        // Test invalid table
        String invalidReadQuery = "SELECT * FROM user WHERE name = 'Alice';";
        assertThrows(SQLException.class, () -> queryExecutor.executeReadQuery(invalidReadQuery), "executeUpdateQuery: Alice wasn't updated or error undetected");

    }

    /**
     * @author Hadi Saghir
     * This method tests SELECT transaction
     *
     * As this is a mock test, a state transition check can be testing using setAutoCommit's state
     *  1 switch test should do it
     *  -begin, chnge, end and change
     *  -being, change, rollback, end, change.
     *  -Returns:
     *      -no return (no elements match query) (recieving an empty resultSet)
     *      -with return (this is done by throw exception not being invoked)
     *
     * We also need to test invalid queries and the response of the DB. This can be done by
     *  - testing invalid WHERE statement,  e.g. WHERE groupID = "TEST"
     *  - testing invalid table,  e.g. table user instead of users
     *
     * */
    @Test
    void beginTransaction() throws SQLException {
        // Mock state where autocommit is false
        Statement statement = mock(Statement.class);
        when(con.getConnection().createStatement()).thenReturn(statement);
        doNothing().when(con.getConnection()).setAutoCommit(false);

        // Call the method and assert that the Statement object is returned
        Statement result = queryExecutor.beginTransaction();
        assertEquals(statement, result);

        // Verify that the setAutoCommit method was called once
        verify(con.getConnection(), times(1)).setAutoCommit(false);
    }

    @Test
    void endTransaction() throws SQLException {
        // Mock state where autocommit is true
        Statement statement = mock(Statement.class);
        when(con.getConnection().createStatement()).thenReturn(statement);
        doNothing().when(con.getConnection()).setAutoCommit(true);

        // Call the method and assert that the Statement object is returned
        Statement result = queryExecutor.endTransaction();
        assertEquals(statement, result);

        // Verify that the setAutoCommit method was called once
        verify(con.getConnection(), times(1)).setAutoCommit(true);
    }

    @Test
    void rollbackTransaction() {
    }

    void oneWayswitch() throws SQLException {
        // Mock state where autocommit is true
        Statement statement = mock(Statement.class);
        when(con.getConnection().createStatement()).thenReturn(statement);
        doNothing().when(con.getConnection()).setAutoCommit(true);

        // Call the method and assert that the Statement object is returned
        Statement result = queryExecutor.endTransaction();
        assertEquals(statement, result);

        // Verify that the setAutoCommit method was called once
        verify(con.getConnection(), times(1)).setAutoCommit(true);
    }

    void change() throws SQLException {
        // Mock state where autocommit is true
        Statement statement = mock(Statement.class);
        when(con.getConnection().createStatement()).thenReturn(statement);
        doNothing().when(con.getConnection()).setAutoCommit(true);

        // Call the method and assert that the Statement object is returned
        Statement result = queryExecutor.endTransaction();
        assertEquals(statement, result);

        // Verify that the setAutoCommit method was called once
        verify(con.getConnection(), times(1)).setAutoCommit(true);
    }
}