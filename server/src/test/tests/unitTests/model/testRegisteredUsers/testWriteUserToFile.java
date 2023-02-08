package unitTests.model.testRegisteredUsers;
import model.RegisteredUsers;
import org.junit.jupiter.api.*;
import shared.transferable.*;

public class testWriteUserToFile {

    /*
    todo: these tests rely on getUserFromFile because currently writeUserToFile returns void. all tests need to be updated once method is updated to give a return value
     */

    @Test
    public void testNull() {
        User testUser = null;
        RegisteredUsers.getInstance().writeUserToFile(testUser);
        User testResult = RegisteredUsers.getInstance().getUserFromFile(testUser);
        Assertions.assertNull(testResult);
    }
    @Test
    public void testEmpty() {
        User testUser = new User("", "");
        RegisteredUsers.getInstance().writeUserToFile(testUser);
        User testResult = RegisteredUsers.getInstance().getUserFromFile(testUser);
        Assertions.assertEquals(testUser, testResult);
    }
    @Test
    public void testStringLowercase() {
        User testUser = new User("testuser", "testpassword");
        RegisteredUsers.getInstance().writeUserToFile(testUser);
        User testResult = RegisteredUsers.getInstance().getUserFromFile(testUser);
        Assertions.assertEquals(testUser, testResult);
    }
    @Test
    public void testStringUppercase() {
        User testUser = new User("TESTUSER", "PASSWORD");
        RegisteredUsers.getInstance().writeUserToFile(testUser);
        User testResult = RegisteredUsers.getInstance().getUserFromFile(testUser);
        Assertions.assertEquals(testUser, testResult);
    }
    @Test
    public void testStringWithNumbers() {
        User testUser = new User("1234567890", "1234567890");
        RegisteredUsers.getInstance().writeUserToFile(testUser);
        User testResult = RegisteredUsers.getInstance().getUserFromFile(testUser);
        Assertions.assertEquals(testUser, testResult);
    }
    @Test
    public void testStringWithUnusualCharacters() {
        User testUser = new User("!\"'åö#$.{[", "!\"'åö#$.)&");
        RegisteredUsers.getInstance().writeUserToFile(testUser);
        User testResult = RegisteredUsers.getInstance().getUserFromFile(testUser);
        Assertions.assertEquals(testUser, testResult);
    }
}
