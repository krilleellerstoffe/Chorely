package com.mau.chorely.test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import com.mau.chorely.model.ClientNetworkManager;
import com.mau.chorely.model.Model;
import com.mau.chorely.model.persistentStorage.PersistentStorage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import shared.transferable.Group;
import shared.transferable.Message;
import shared.transferable.NetCommands;
import shared.transferable.Transferable;
import shared.transferable.User;

/**
 * Unit tests for the model class
 * @author Johan Salomonsson
 */
public class ModelTest {

    @Test
    public void testHasLastStoredUserNoUser() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);

        Boolean result = model.hasStoredUser();
        assertEquals(false, result);
    }
    @Test
    public void testHasLastStoredUserValidUser() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);
        User user = new User("testy");
        when(storage.getUser()).thenReturn(user);

        Boolean result = model.hasStoredUser();
        assertEquals(true, result);
    }
    @Test
    public void testRemoveLastSearchedUser() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);
        User user = new User("testy");
        model.setLastSearchedUser(user);

        User result = model.removeLastSearchedUser();
        assertEquals(user, result);
    }
    @Test
    public void testUpdateGroupExternalSuccessfulUpdate() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);

        User user = new User("testy");
        Group group = new Group("Group A", user);
        ArrayList<Transferable> data = new ArrayList<>();
        data.add(group);
        Message message = new Message(null, user, data);

        when(storage.getUser()).thenReturn(user);
        when(storage.saveOrUpdateGroup(group)).thenReturn(true);

        String result = model.updateGroupExternal(message);
        assertEquals("Group successfully updated", result);
    }

    @Test
    public void testUpdateGroupExternalFailedToUpdate() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);

        User user = new User("testy");
        Group group = new Group("Group A", user);
        ArrayList<Transferable> data = new ArrayList<>();
        data.add(group);
        Message message = new Message(null, user, data);

        when(storage.getUser()).thenReturn(user);
        when(storage.saveOrUpdateGroup(group)).thenReturn(false);

        String result = model.updateGroupExternal(message);
        assertEquals("Failed to update group.", result);
    }

    @Test
    public void testUpdateGroupExternalUserNotInGroup() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);

        User user = new User("testy");
        Group group = new Group("Group A", user);
        ArrayList<Transferable> data = new ArrayList<>();
        data.add(group);
        Message message = new Message(null, user, data);

        when(storage.getUser()).thenReturn(null);

        String result = model.updateGroupExternal(message);
        assertEquals("Deleted group.", result);
    }

    @Test
    public void testAutomaticLogInWithNoStoredUser() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);

        when(storage.getUser()).thenReturn(null);

        String result = model.automaticLogIn();
        assertEquals(null,result);
    }

    @Test
    public void testAutomaticLoginSuccessful() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);

        User user = new User("testUser");
        when(storage.getUser()).thenReturn(user);

        String result = model.automaticLogIn();
        assertEquals("Automatic Login", result);
    }

    @Test
    public void testHandleTask() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);
        ArrayList<Transferable> data = new ArrayList<>();
        User user = new User("testy");

        Message message = new Message(NetCommands.loginOk, user);
        boolean result = model.handleTask(message);
        assertEquals(true, result);
    }

    @Test
    public void testCreateGroupSuccess() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);
        ArrayList<Transferable> data = new ArrayList<>();
        User user = new User("testy");
        Group group = new Group("Group A", user);
        data.add(group);
        Message message = new Message(NetCommands.registerNewGroup, user, data);
        doNothing().when(network).sendMessage(any(Message.class));

        String result = model.createGroup(message);
        assertEquals("Group created", result);
    }
    @Test
    public void testCreateGroupFail() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);
        ArrayList<Transferable> data = new ArrayList<>();
        User user = new User("testy");
        Group group = new Group("Group A", user);
        data.add(group);
        Message message = new Message(NetCommands.registerNewGroup, user, data);
        doThrow(new RuntimeException("Failed to create group")).when(network).sendMessage(any(Message.class));

        String result = model.createGroup(message);
        assertEquals("java.lang.RuntimeException: Failed to create group", result);
    }

    @Test
    public void testUpdateGroupSuccess() {
        // Mocks
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);
        ArrayList<Transferable> data = new ArrayList<>();
        User user = new User("testy");
        Group group = new Group("Group A", user);
        data.add(group);
        Message message = new Message(NetCommands.updateGroup,user,data);

        when(storage.getUser()).thenReturn(user);
        when(storage.saveOrUpdateGroup(group)).thenReturn(true);
        doNothing().when(network).sendMessage(any(Message.class));

        String result = model.updateGroup(message);
        assertEquals("Group successfully updated", result);
    }

    @Test
    public void testUpdateGroupFailedUpdate() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);

        Group group = new Group("Test Group");
        User user = new User("Testy");
        group.addMember(user);
        ArrayList<Transferable> data = new ArrayList<>();
        data.add(group);


        when(storage.getUser()).thenReturn(user);
        when(storage.saveOrUpdateGroup(group)).thenReturn(false);

        Message message = new Message(NetCommands.updateGroup, user, data);
        String result = model.updateGroup(message);
        assertEquals("Failed to update group.", result);
    }

    @Test
    public void testUpdateGroupDeleteGroup() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);

        Group group = new Group("Test Group");
        User user = new User("Testy");
        ArrayList<Transferable> data = new ArrayList<>();
        data.add(group);
        Message message = new Message(NetCommands.updateGroup, user, data);

        when(storage.saveOrUpdateGroup(group)).thenReturn(true);
        when(storage.getSelectedGroup()).thenReturn(group);
        doNothing().when(network).sendMessage(message);

        String result = model.updateGroup(message);
        assertEquals("Deleted group.", result);

    }


    @Test
    public void manualLogInTest(){
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);
        Message message = new Message(NetCommands.login, new User("Testy"));
        String result = model.manualLogIn(message);
        assertEquals("Manual login", result);
    }

    @Test
    public void testLogOut(){
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);
        Message message = new Message(NetCommands.logout, new User("Testy McTest"));
        String result = model.logOut(message);
        assertEquals("User logged out", result);
    }

    @Test
    public void validNameGetGroupNameForNotificationTest() {
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);
        Group group = new Group("Test Group");
        Message message = new Message(NetCommands.notificationReceived, new User("Testy McTest"), Arrays.asList((Transferable)group));
        String groupName = model.getGroupNameForNotification(message);
        assertEquals("Test Group", groupName);
    }

    @Test
    public void noGroupGetGroupNameForNotificationTest(){
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);
        Message message = new Message(NetCommands.notificationReceived,new User("Testy McTest"));
        String groupName = model.getGroupNameForNotification(message);
        assertEquals(null,groupName);

    }

    @Test
    public void emptyStringGetGroupNameForNotificationTest(){
        ClientNetworkManager network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        Model model = new Model(network, storage);
        Group group = new Group("");
        Message message = new Message(NetCommands.notificationReceived, new User("Testy McTest"), Arrays.asList((Transferable)group));
        String groupName = model.getGroupNameForNotification(message);
        assertEquals("", groupName);
    }
}