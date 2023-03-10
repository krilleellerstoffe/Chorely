package unitTests.controller.ServerControllerTests;


import controller.ClientHandler;
import controller.ServerController;
import model.RegisteredGroups;
import model.RegisteredUsers;
import static org.mockito.ArgumentMatcher.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.checkerframework.checker.units.qual.C;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
//import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Assert;
import service.DatabaseConnection;
import service.QueryExecutor;
import shared.transferable.*;

import javax.management.Query;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/** 
* ServerController Tester. 
* 
* @author <Authors name> 
* @since <pre>feb. 8, 2023</pre> 
* @version 1.0 
*/

public class ServerControllerTest {

/** 
* 
* Method: handleMessage(Message msg) 
* 
*/ 
@Test
public void testHandleMessage() throws Exception {

//TODO: Test goes here... 
} 

/** 
* 
* Method: addOnlineClient(User user, ClientHandler client) 
* 
*/ 
@Test
public void testAddOnlineClient() throws Exception {

    ConcurrentHashMap<User, ClientHandler> onlineUsers = new ConcurrentHashMap<>();
    User user = new User("Testme", "123");

    ClientHandler handler = mock(ClientHandler.class);
    onlineUsers.put(user, handler);
    assertEquals(handler, onlineUsers.get(user));

} 

/** 
* 
* Method: removeOnlineClient(User user) 
* 
*/ 
@Test
public void testRemoveOnlineClient() throws Exception {

    ConcurrentHashMap<User, ClientHandler> onlineUsers = new ConcurrentHashMap<>();
    ServerController controller = mock(ServerController.class);
    ClientHandler clientHandler = mock(ClientHandler.class);
    for (int i = 1; i < 5; i++){        //Create 5 new users and add them to the map
        User user = new User("Name" + i, "password");
        onlineUsers.put(user, clientHandler);
    }
    onlineUsers.remove("Name2");    //remove a "random" user created earlier

    //Test that the map is still active, and the size of it modified
    assertNotNull(onlineUsers);
    int arraySize = onlineUsers.size();
    assertEquals(arraySize, 4);


} 

/** 
* 
* Method: sendSavedGroups(User user) 
* 
*/ 
@Test
public void testSendSavedGroups() throws Exception {

} 

/** 
* 
* Method: sendReply(Message reply) 
* 
*/ 
@Test
public void testSendReply() throws Exception {
    ConcurrentHashMap<User, ClientHandler> onlineUsers = new ConcurrentHashMap<>();
    ServerController controller = mock(ServerController.class);
    ClientHandler handler = mock(ClientHandler.class);

    for (int i = 1; i < 5; i++){        //Create 5 new users and add them to the map
        User user = new User("Name" + i, "password");
        onlineUsers.put(user, handler);
    }
    User sender = new User("sender");
    Message reply = new Message(NetCommands.updateGroup, sender);

    assertNotNull(reply);

    onlineUsers.remove("Name1");
    if(onlineUsers.containsKey("Name1")){       //if this is true, something has gone wrong
        fail();
    }else{
        //wait for user to log on
        assertEquals(0, controller.sendReply(reply));
    }
} 

/** 
* 
* Method: notifyGroupChanges(Group group) 
* 
*/ 
@Test
public void testNotifyGroupChanges() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: handleClientTask(Message msg) 
* 
*/ 
@Test
public void testHandleClientTask() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: logoutUser(Message msg) 
* 
*/ 
@Test
public void testLogoutUser() throws Exception {
    ClientHandler c = mock(ClientHandler.class);
    User user = new User("TestPerson");
    Message message = new Message((NetCommands.logout), user);
    assertEquals(user, message.getUser());
    when (c.logout(user)).thenReturn(true);

}

/** 
* 
* Method: registerNewGroup(Message request) 
* 
*/ 
@Test
public void  testRegisterNewGroup() throws Exception {
//TODO: Test goes here...

} 

/** 
* 
* Method: updateGroup(Message request) 
* 
*/ 
@Test
public void testUpdateGroup() throws Exception {
    Group updatedGroup = new Group();
    updatedGroup.setName("ExampleName");
    updatedGroup.setOwner("GroupOwner");
    RegisteredGroups groups = mock(RegisteredGroups.class);
    groups.updateGroup(updatedGroup);
    ArrayList<User> memberList = new ArrayList<>();
    for (int i = 0; i < 4; i++){
        User dummy = new User("Name" + i);
        updatedGroup.addMember(dummy);       //adds 5 random users to the grp
        memberList.add(dummy);
    }

    assertEquals(updatedGroup.getName(), "ExampleName");
    assertEquals(updatedGroup.getOwner(), "GroupOwner");
    assertEquals(updatedGroup.getMembers(), memberList);
} 

/** 
* 
* Method: searchForUser(Message request) 
* 
*/ 
@Test
public void testSearchForUser() throws Exception {
    User dummy = new User("Dummy");
    Message request = new Message(NetCommands.searchForUser, dummy);

    RegisteredUsers registeredUsers = mock(RegisteredUsers.class);
    registeredUsers.writeUserToFile(dummy);

    assertEquals(dummy, request.getUser());
    assertNotNull(dummy);
}

/** 
* 
* Method: deleteGroup(Message msg) 
* 
*/ 
@Test
public void testDeleteGroup() throws Exception {
    RegisteredGroups groups = mock(RegisteredGroups.class);
    Group group = new Group(123);

    for(int i = 1; i < 5; i++){
        User user = new User("Username" + i);
        group.addMember(user);
    }
    assertNotNull(group.getUsers());
    assertEquals(group.getUsers().size(), 4);

    groups.deleteGroup(group);
    assertNull(groups.getGroupFromFile(123));
} 

/** 
* 
* Method: updateUsersInGroup(Group group) 
* 
*/ 
@Test
public void testUpdateUsersInGroup() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = ServerController.getClass().getMethod("updateUsersInGroup", Group.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: removeUsers(Group newGroup) 
* 
*/ 
@Test
public void testRemoveUsers() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = ServerController.getClass().getMethod("removeUsers", Group.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 
