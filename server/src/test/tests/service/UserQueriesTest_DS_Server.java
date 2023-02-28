package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import shared.transferable.Group;
import shared.transferable.User;

import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class has the purpose to test the user queries, this entails the following:
 *  -dependency on the database (functional database, functional queryExecutor class)
 *  -Testing the queries written against the database
 *  -Testing the response the database
 * */
class UserQueriesTest_DS_Server {

    public static DatabaseConnection con = null;
    private Socket socket;
    private QueryExecutor queryExecutor;
    private UserQueries userQueries;

    @BeforeEach
    void setUp() {

        con = new DatabaseConnection("test");
        // Set up a test database and instantiate UserQueries
        queryExecutor = new QueryExecutor(con);
        userQueries = new UserQueries(queryExecutor);
    }

    /**
     * @author Hadi Saghir
     * @author Mirko
     * */

    @Test
    void registerUser() throws SQLException {
        // Register a new user
        String username = "test_user";
        String password = "test_password";
        boolean isAdult = true;
        boolean success = userQueries.registerUser(username, password, isAdult);

        // Check that the user was successfully registered
        assertTrue(success);
        ResultSet resultSet = queryExecutor.executeReadQuery("SELECT * FROM User WHERE user_name = 'test_user';");
        assertTrue(resultSet.next());
        assertEquals(BCrypt.hashpw(password, BCrypt.gensalt()), resultSet.getString("password"));
        assertEquals(isAdult, resultSet.getBoolean("is_adult"));


        // Register a user that already exists
        success = userQueries.registerUser(username, password, isAdult);
        assertTrue(success);

        // Try to register the same user again
        success = userQueries.registerUser(username, password, isAdult);

        // Check that the second registration failed
        assertFalse(success);
    }

    @Test
    void loginUser() {
        // Register a user
        String username = "test_user";
        String password = "test_password";
        boolean isAdult = true;
        userQueries.registerUser(username, "test_password", isAdult); //in case registerUser doesn't run first

        // Log in the user with correct credentials
        User loggedInUser = userQueries.loginUser(username, password);

        // Check that the correct user was logged in and that their groups were retrieved
        assertNotNull(loggedInUser);
        assertEquals(username, loggedInUser.getUsername());
        assertEquals(isAdult, loggedInUser.isAdult());
        assertEquals(new ArrayList<Group>(), loggedInUser.getDbGroups()); // empty group list

        // Log in the user with incorrect password
        loggedInUser = userQueries.loginUser(username, "incorrect_password");

        // Check that the login failed and null was returned
        assertNull(loggedInUser);
    }

    @Test
    void getUserInfo() {
        // Register a user
        String username = "test_user";
        boolean isAdult = true;
        userQueries.registerUser(username, "test_password", isAdult); //in case registerUser doesn't run first

        // Get the user's info
        User user = userQueries.getUserInfo(username);

        // Check that the correct user was returned
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals(isAdult, user.isAdult());


        // Log in the user with incorrect password
        User loggedInUser = userQueries.loginUser(username, "incorrect_password");

        // Check that the login failed and null was returned
        assertNull(loggedInUser);


    }

    @Test
    void deleteAccount() {
        // create a user for testing
        String testUserName = "testUser";
        String testPassword = "testPassword";
        userQueries.registerUser(testUserName, testPassword, true);
        User testUser = userQueries.getUserInfo(testUserName);

        // delete user with wrong password
        String wrongPassword = "wrongPassword";
        boolean isDeleted = userQueries.deleteAccount(testUser, wrongPassword);
        assertFalse(isDeleted);

        // check that user is not deleted
        assertNotNull(userQueries.getUserInfo(testUserName));

        // delete user
        isDeleted = userQueries.deleteAccount(testUser, testPassword);
        assertTrue(isDeleted);

        // check that user is deleted
        assertNull(userQueries.getUserInfo(testUserName));
    }

    @Test
    void checkPassword() {
        // create a user for testing
        String testUserName = "testUser";
        String testPassword = "testPassword";
        userQueries.registerUser(testUserName, testPassword, true);

        // check password for correct user
        boolean isPasswordCorrect = userQueries.checkPassword(testUserName, testPassword);
        assertTrue(isPasswordCorrect);

        // check password for wrong user
        String wrongUserName = "wrongUser";
        boolean isWrongPasswordCorrect = userQueries.checkPassword(wrongUserName, testPassword);
        assertFalse(isWrongPasswordCorrect);

        // check wrong password for correct user
        String wrongPassword = "wrongPassword";
        boolean isWrongPasswordCorrect2 = userQueries.checkPassword(testUserName, wrongPassword);
        assertFalse(isWrongPasswordCorrect2);
    }
}