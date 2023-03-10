package com.mau.chorely.model;

import android.graphics.ColorSpace.Model;

import junit.framework.TestCase;

import java.util.ArrayList;

public class ClientNetworkManagerTest extends TestCase {

    /*

    public void testSendMessage() {
        Model model = new Model();
        ClientNetworkManager clientNetworkManager = new ClientNetworkManager(model); //i don't understnad the dependancy system in android
        Message message = new Message(NetCommands.connected, null, new ArrayList<>());

        clientNetworkManager.sendMessage(message);

        assertFalse(clientNetworkManager.outBoundQueue.isEmpty());
        assertEquals(message, clientNetworkManager.outBoundQueue.poll());


        for(int i = 0; i < 15; i++){
            clientNetworkManager.sendMessage(message);
            if(i%3 == 0){
                assertEquals(message, clientNetworkManager.outBoundQueue.poll());
            }
        }
        for(int i = 0; i < 10; i++){
            assertEquals(message, clientNetworkManager.outBoundQueue.poll());
        }
    }

    */
}