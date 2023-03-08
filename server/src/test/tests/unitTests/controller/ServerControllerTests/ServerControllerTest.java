package unitTests.controller.ServerControllerTests;

import controller.ClientHandler;
import controller.ServerController;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
//import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Assert;
import service.DatabaseConnection;
import service.QueryExecutor;
import shared.transferable.Group;
import shared.transferable.User;

import javax.management.Query;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;

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
//TODO: Test goes here...

    ConcurrentHashMap<User, ClientHandler> onlineUsers = new ConcurrentHashMap<>();
    User user = new User("Testme", "123");

    try {
        ServerSocket serverSocket = new ServerSocket(1234);
        Socket socket = serverSocket.accept();
        ClientHandler handler = new ClientHandler(socket, new ServerController(1234));

        onlineUsers.put(user, handler);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    assertTrue(onlineUsers.contains(user));
    //assertTrue(onlineUsers.contains(user);


    //assertEquals(user, );


} 

/** 
* 
* Method: removeOnlineClient(User user) 
* 
*/ 
@Test
public void testRemoveOnlineClient() throws Exception {

    ConcurrentHashMap<User, ClientHandler> onlineUsers = new ConcurrentHashMap<>();
    ServerController controller = new ServerController(1234);
    ClientHandler clientHandler = new ClientHandler(new Socket(), controller);
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
//TODO: Test goes here... 
} 

/** 
* 
* Method: sendReply(Message reply) 
* 
*/ 
@Test
public void testSendReply() throws Exception { 
//TODO: Test goes here...

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
//TODO: Test goes here... 
} 

/** 
* 
* Method: registerNewGroup(Message request) 
* 
*/ 
@Test
public void testRegisterNewGroup() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: updateGroup(Message request) 
* 
*/ 
@Test
public void testUpdateGroup() throws Exception {

//TODO: Test goes here...
Group group = new Group();
QueryExecutor query = new QueryExecutor(new DatabaseConnection("chorelyBackup"));

//String query = "INSERT into [Group]"
    //MockConnection

} 

/** 
* 
* Method: searchForUser(Message request) 
* 
*/ 
@Test
public void testSearchForUser() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: run() 
* 
*/ 
@Test
public void testRun() throws Exception { 
//TODO: Test goes here... 
} 


/** 
* 
* Method: deleteGroup(Message msg) 
* 
*/ 
@Test
public void testDeleteGroup() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = ServerController.getClass().getMethod("deleteGroup", Message.class); 
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
