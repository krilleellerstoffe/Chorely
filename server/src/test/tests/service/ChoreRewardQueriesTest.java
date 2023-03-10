package service;

import org.junit.Before;
import org.junit.Test;
import shared.transferable.Chore;
import shared.transferable.Reward;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;


/**
 * This class has the purpose to test the user queries, this entails the following:
 *  -dependency on the database (functional database, functional queryExecutor class)
 *  -Testing the queries written against the database
 *  -Testing the response the database
 * */
class ChoreRewardQueriesTest {

    public static DatabaseConnection con = null;
    private ChoreRewardQueries choreRewardQueries;
    private QueryExecutor queryExecutor;

    @Before
    public void setUp() throws Exception {
        // Set up a test database and instantiate UserQueries
        con = new DatabaseConnection("test");
        queryExecutor = new QueryExecutor(con);
        choreRewardQueries = new ChoreRewardQueries(queryExecutor);
    }

    @Test
    void createChore() throws SQLException {
        // Create a test chore
        Chore chore = new Chore("Clean room", 10,"Clean the bedroom and vacuum the floor", 1);

        // Create the chore and check that it was successfully added to the database
        assertTrue(choreRewardQueries.createChore(chore));

        // Check that the chore was added to the database with the correct values
        String query = "SELECT * FROM Chore WHERE chore_name = 'Clean room' AND group_id = 1";
        assertTrue(queryExecutor.executeReadQuery(query).next());
        assertEquals("Clean room", queryExecutor.executeReadQuery(query).getString("chore_name"));
        assertEquals("Clean the bedroom and vacuum the floor", queryExecutor.executeReadQuery(query).getString("chore_description"));
        assertEquals(10, queryExecutor.executeReadQuery(query).getInt("chore_points"));
    }

    @Test
    void updateChore() throws SQLException {
        // Create a test chore
        Chore chore = new Chore("Clean room", 10,"Clean the bedroom and vacuum the floor", 1);

        // Create the chore and check that it was successfully added to the database
        assertTrue(choreRewardQueries.createChore(chore));

        // Update the chore and check that the database was updated
        chore = new Chore("Clean room", 15,"Clean the bedroom, vacuum the floor, and dust the shelves", 1);
        chore.setLastDoneByUser("Alice");
        assertTrue(choreRewardQueries.updateChore(chore));

        // Check that the chore was updated in the database with the correct values
        String query = "SELECT * FROM Chore WHERE chore_name = 'Clean room' AND group_id = 1";
        assertTrue(queryExecutor.executeReadQuery(query).next());
        assertEquals("Clean room", queryExecutor.executeReadQuery(query).getString("chore_name"));
        assertEquals("Clean the bedroom, vacuum the floor, and dust the shelves", queryExecutor.executeReadQuery(query).getString("chore_description"));
        assertEquals(15, queryExecutor.executeReadQuery(query).getInt("chore_points"));
        assertEquals("Alice", queryExecutor.executeReadQuery(query).getString("last_user"));
    }

    @Test
    void deleteChore() {
    }

    @Test
    void createReward() {
    }

    @Test
    void updateReward() {
    }

    @Test
    void deleteReward() {
    }
}