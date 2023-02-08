package unitTests.model.RegisteredUsersTests;

import model.RegisteredUsers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class UserNameAvailableTest {

    @Test
    public void testNull() {
        String testUser = null;
        boolean testResult = RegisteredUsers.getInstance().userNameAvailable(testUser);
        Assertions.assertFalse(testResult);
    }

    @Test
    public void testEmpty() {
        String testUser = "";
        boolean testResult = RegisteredUsers.getInstance().userNameAvailable(testUser);
        Assertions.assertFalse(testResult);
    }

    @Test
    public void testLongerThanTen() {
        //placeholder awaiting requirement to be implemented or not
        String testUser = "testusername";
        boolean testResult = RegisteredUsers.getInstance().userNameAvailable(testUser);
        Assertions.assertFalse(testResult);
    }

    @Test
    public void testStringLowercase() {
        String testUser = "testuser";
        File file = new File("files/users/" + testUser + ".dat");
        file.delete();
        boolean testResult = RegisteredUsers.getInstance().userNameAvailable(testUser);
        Assertions.assertTrue(testResult);
    }

    @Test
    public void testStringUppercase() {
        String testUser = "TESTUSER";
        File file = new File("files/users/" + testUser + ".dat");
        file.delete();
        boolean testResult = RegisteredUsers.getInstance().userNameAvailable(testUser);
        Assertions.assertTrue(testResult);
    }

    @Test
    public void testStringWithNumbers() {
        String testUser = "1234567890";
        File file = new File("files/users/" + testUser + ".dat");
        file.delete();
        boolean testResult = RegisteredUsers.getInstance().userNameAvailable(testUser);
        Assertions.assertTrue(testResult);
    }

    @Test
    public void testStringWithUnusualCharacters() {
        String testUser = "!\"'åö#$.{[";
        File file = new File("files/users/" + testUser + ".dat");
        file.delete();
        boolean testResult = RegisteredUsers.getInstance().userNameAvailable(testUser);
        Assertions.assertTrue(testResult);
    }
}
