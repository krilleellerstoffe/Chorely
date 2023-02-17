package service;

import org.mindrot.jbcrypt.BCrypt;
import shared.transferable.User;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Class responsible for calling the database about users.

 */
public class UserQueries {

    private QueryExecutor database;

    public UserQueries(QueryExecutor database){
       this.database = database;
    }

    /**
     * Method to save a new user and a hashed password using BCrypt.
     * @param userName and password from login message received from client
     * @return A boolean value, true if the user is created, false if user already exists or something else went wrong
     */
    public boolean registerUser(String userName, String password, boolean adult) {
        boolean success = false;
        //Hash password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sqlSafeUsername = makeSqlSafe(userName);
        int isAdult = 1;
        if (!adult) {
            isAdult = 0;
        }
        String query = "INSERT INTO [User] VALUES ('" + sqlSafeUsername + "', '" + hashedPassword + "', " + isAdult + ")";
        try {
            database.executeUpdateQuery(query);
            success = true;
        }
        catch (SQLException sqlException) {
            //todo will fail if duplicate username found -> throw appropriate error message
            sqlException.printStackTrace();
        }
        return success;
    }
    private static String makeSqlSafe(String string) {
        //simple (but not secure) method to clean sql input
        return string.replace("'", "''");
    }

    /**
     * Method to check a user's credentials and log them in
     * @param userName and password from login message received from client
     * @return A user containing their username, whether they are an adult, and a list of the groups they are a member of
     */
    public User loginUser(String userName, String password) {
        User loggedInUser = null;
        ArrayList<Integer> groups = new ArrayList<>();
        if (checkPassword(userName, password)) {
            String sqlSafeUsername = makeSqlSafe(userName);
            loggedInUser = getUser(sqlSafeUsername);
            //todo once groups changed to accept int as ID implement following call -> group statements to ONE query call
            //join with group table to get group names, points etc
            String query = "SELECT * FROM [Member] WHERE user_name = '" + sqlSafeUsername + "';";
            try {
                ResultSet resultSet = database.executeReadQuery(query);
                while (resultSet.next()) {
                    int groupId = resultSet.getInt("group_id");
                    loggedInUser.getDbGroups().add(groupId);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return loggedInUser;
    }

    public User getUser(String sqlSafeUsername) {
        boolean adult = false;
        String query = "SELECT * FROM [User] WHERE user_name = '" + sqlSafeUsername + "';";
        try {
            ResultSet resultSet = database.executeReadQuery(query);
            while (resultSet.next()) {
                int adultInt = resultSet.getInt("is_adult");
                    if(adultInt == 1) adult = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new User(sqlSafeUsername, adult);
    }

    /**
     * Method to delete a user
     *
     */
    public boolean deleteAccount(User userToDelete, String password) {
        boolean accountDeleted = false;
        String sqlSafeUsername = makeSqlSafe(userToDelete.getUsername());
        if (checkPassword(userToDelete.getUsername(), password)) {
            try {
                Statement statement = database.beginTransaction();
                String queryDeleteUser = "DELETE FROM [User] WHERE user_name = '" + sqlSafeUsername + "';";
                statement.executeUpdate(queryDeleteUser);
                database.endTransaction();
                accountDeleted = true;
            }
            catch (SQLException sqlException) {
                try {
                    System.out.println(sqlException);
                   database.rollbackTransaction();
                }
                catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return accountDeleted;
    }

    /**
     * Method to check if a user exists in database.
     *
     * @param password typed password from client and the application
     * @return A boolean value, true if the user exist in database and the password is correct
     */
    public boolean checkPassword(String username, String password) {
        boolean isVerified = false;
        //simple (but not secure) method to clean sql input
        String sqlSafeUsername = makeSqlSafe(username);
        String query = "SELECT user_password FROM [User] WHERE user_name = '" + sqlSafeUsername + "';";
        try {
            ResultSet resultSet = database.executeReadQuery(query);
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString(1);
                isVerified = BCrypt.checkpw(password, hashedPassword);
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        System.out.println(isVerified);
        return isVerified;
    }



}

