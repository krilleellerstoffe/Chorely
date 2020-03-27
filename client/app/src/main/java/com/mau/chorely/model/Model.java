package com.mau.chorely.model;



import shared.transferable.ErrorMessage;
import shared.transferable.NetCommands;
import shared.transferable.Transferable;
import com.mau.chorely.model.persistentStorage.PersistentStorage;
import com.mau.chorely.model.utils.MultiMap;
import com.mau.chorely.model.utils.ResultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;


public class Model implements NetworkListener{

    private MultiMap<NetCommands, ResultHandler> threadsWaitingForResult = new MultiMap<>();
    private HashMap<NetCommands, NetCommands> resultAndRequestPairs = new HashMap<>();
    private LinkedBlockingDeque<ArrayList<Transferable>> taskToHandle = new LinkedBlockingDeque<>();
    private NetInterface network;
    private Thread modelThread = new Thread(new ModelThread());
    private ErrorMessage errorMessage;
    PersistentStorage storage = new PersistentStorage();

    Model(){
        network = new NetInterface(this);
        modelThread.start();
    }

    private void registerCommandPairs(){
        resultAndRequestPairs.put(NetCommands.registrationOk, NetCommands.register);
        resultAndRequestPairs.put(NetCommands.registrationDenied, NetCommands.register);
    }

    public void stop(){
        network.disconnect();
        modelThread.interrupt();
    }

    public ErrorMessage getErrorMessage(){
        ErrorMessage ret = errorMessage;
        errorMessage = null;
        return ret;
    }

    public synchronized NetCommands notifyForResult(ArrayList<Transferable> curWorkingOn){
        try {
            taskToHandle.put(curWorkingOn);
            ResultHandler threadLockObject = new ResultHandler();
            threadsWaitingForResult.put(((NetCommands)curWorkingOn.get(0)), threadLockObject);
            return threadLockObject.waitForResult();
        } catch (InterruptedException e){
            System.out.println("Exception putting in notifyForResult" + e.getMessage());
            return NetCommands.internalClientError;
        }
    }

    private void handleResult(NetCommands returnCommand){
        ArrayList<ResultHandler> waitingThreads = new ArrayList<>();
                threadsWaitingForResult.get(resultAndRequestPairs.get(returnCommand));

        for (ResultHandler thread : waitingThreads) {
            thread.notifyResult(returnCommand);
        }
    }

    private void handleError(ArrayList<Transferable> errorList){
        errorMessage = (ErrorMessage) errorList.get(1);
        HashMap<NetCommands, ArrayList<ResultHandler>> tempMap = threadsWaitingForResult.getHashMap();
        for (Map.Entry<NetCommands, ArrayList<ResultHandler>> entry : tempMap.entrySet()){
            ArrayList<ResultHandler> mapData = entry.getValue();
            for(ResultHandler thread : mapData){
                thread.notifyResult((NetCommands)errorList.get(0));
            }
        }
    }


    @Override
    public  void notify(ArrayList<Transferable> transferred) {
        try {
            taskToHandle.put(transferred);

        } catch (InterruptedException e){
            System.out.println("Error in model callback" + e.getMessage());
        }
    }


    private class ModelThread implements Runnable{
        @Override
        public void run() {

            while (!Thread.interrupted()){
                System.out.println("Entering queue");
                try {
                    ArrayList<Transferable> curWorkingOn = taskToHandle.take();

                    switch ((NetCommands) curWorkingOn.get(0)) {
                        case register:
                            storage.updateData("/user.cho", curWorkingOn.get(0));
                            network.sendData(curWorkingOn);
                            break;
                        case registrationOk:
                            handleResult((NetCommands) curWorkingOn.get(0));
                            break;
                        case internalClientError:
                            handleError(curWorkingOn);

                    }
                } catch (InterruptedException e){
                    System.out.println("Thread interrupted in main model queue");
                }
            }
        }
    }

}
