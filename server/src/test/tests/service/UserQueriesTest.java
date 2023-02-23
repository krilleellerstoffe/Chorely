package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import shared.transferable.Group;
import shared.transferable.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.*;

class UserQueriesTest {

    public static DatabaseConnection con = null;
    private QueryExecutor queryExecutor;
    private UserQueries userQueries;

    @BeforeEach
    void setUp() {
        con = new DatabaseConnection("test");
        // Set up a test database and instantiate UserQueries
        queryExecutor = new QueryExecutor(con);
        userQueries = new UserQueries(queryExecutor);
    }


    @Test
    void registerUser() {
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
        boolean success = userQueries.registerUser(username, password, isAdult);
        assertTrue(success);

        // Try to register the same user again
        success = userQueries.registerUser(username, password, isAdult);

        // Check that the second registration failed
        assertFalse(success);
    }

    @Test
    void loginUser() {
    }

    @Test
    void getUserInfo() {
    }

    @Test
    void deleteAccount() {
    }

    @Test
    void checkPassword() {
    }
}