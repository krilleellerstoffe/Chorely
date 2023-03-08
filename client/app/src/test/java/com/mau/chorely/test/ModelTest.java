package com.mau.chorely.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.net.Network;

import com.mau.chorely.activities.utils.Presenter;
import com.mau.chorely.model.ClientNetworkManager;
import com.mau.chorely.model.Model;
import com.mau.chorely.model.persistentStorage.PersistentStorage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import shared.transferable.Group;
import shared.transferable.Message;
import shared.transferable.NetCommands;
import shared.transferable.Transferable;
import shared.transferable.User;

public class ModelTest {
    ClientNetworkManager network;
    Model model;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void manualLogInTest(){
        network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        model = new Model(network,storage);
        Message message = new Message(NetCommands.login, new User("Testy"));
        String result = model.manualLogIn(message);
        assertEquals("Manual login", result);
    }

    @Test
    public void testLogOut(){
        network = mock(ClientNetworkManager.class);
        PersistentStorage storage = mock(PersistentStorage.class);
        model = new Model(network, storage);
        Message message = new Message(NetCommands.logout, new User("Testy McTest"));
        String result = model.logOut(message);
        assertEquals("User logged out", result);
    }

    @Test
    public void validNameGetGroupNameForNotificationTest() {
        Group group = new Group("Test Group");
        Message message = new Message(NetCommands.notificationReceived, new User("Testy McTest"), Arrays.asList((Transferable)group));
        String groupName = model.getGroupNameForNotification(message);
        assertEquals("Test Group", groupName);
    }

    @Test
    public void noGroupGetGroupNameForNotificationTest(){
        Message message = new Message(NetCommands.notificationReceived,new User("Testy McTest"));
        String groupName = model.getGroupNameForNotification(message);
        assertEquals(null,groupName);

    }

    @Test
    public void emptyStringGetGroupNameForNotificationTest(){
        Group group = new Group("");
        Message message = new Message(NetCommands.notificationReceived, new User("Testy McTest"), Arrays.asList((Transferable)group));
        String groupName = model.getGroupNameForNotification(message);
        assertEquals("", groupName);
    }
}