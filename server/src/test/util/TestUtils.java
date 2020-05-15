package test.util;

import shared.transferable.Group;
import shared.transferable.Message;
import shared.transferable.User;

import java.io.File;
import java.util.List;
import java.util.concurrent.*;

public class TestUtils {

    /**
     * Deletes a User from file. This is used after the User has been created during a test.
     *
     * @param user the user that you want to delete from file.
     */
    public static void deleteUser(User user) {
        String username = user.getUsername();
        String filename = "files/users/" + username + ".dat";
        File file = new File(filename);
        file.delete();
    }

    public static void deleteGroup(Group group) {
        String filename = "files/groups/" + group.getGroupID() + ".dat";
        File file = new File(filename);
        file.delete();
    }

    public static List<Message> sendAndReceive(List<Message> outgoingMessages, int port) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Callable<List<Message>> testClient = TestClient.newTestRun(outgoingMessages, port);
        Future<List<Message>> received = executorService.submit(testClient);
        return received.get();
    }
}
