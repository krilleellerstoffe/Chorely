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
    public void testSingleCharacter() {
        //placeholder awaiting requirement to be implemented or not
        String testUser = "t";
        boolean testResult = RegisteredUsers.getInstance().userNameAvailable(testUser);
        Assertions.assertTrue(testResult);
    }

    @Test
    public void testStringLowercaseTenCharacters() {
        String testUser = "testuseron";
        File file = new File("files/users/" + testUser + ".dat");
        file.delete();
        boolean testResult = RegisteredUsers.getInstance().userNameAvailable(testUser);
        Assertions.assertTrue(testResult);
    }

    @Test
    public void testLowercaseMoreThanTenCharacters() {
        //placeholder awaiting requirement to be implemented or not
        String testUser = "testuserone";
        boolean testResult = RegisteredUsers.getInstance().userNameAvailable(testUser);
        Assertions.assertFalse(testResult);
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
