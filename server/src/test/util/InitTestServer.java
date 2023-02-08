package test.util;

import model.RegisteredGroups;
import model.RegisteredUsers;
import shared.transferable.*;


public class InitTestServer {
    static final RegisteredUsers users = RegisteredUsers.getInstance();
    static final RegisteredGroups groups = RegisteredGroups.getInstance();

    static final User user1 = new User("user1", "pass1");
    static final User user2 = new User("user2", "pass2");
    static final User user3 = new User("user3", "pass3");
    static final Group group1 = new Group("grupp1");
    static final Group group2 = new Group("grupp2");

    static final Chore chore1 = new Chore("syssla1", 50, "syssla1");
    static final Chore chore2 = new Chore("syssla2", 200, "syssla2");

    static final Reward reward1 = new Reward("belöning1", 5, "belöning1");
    static final Reward reward2 = new Reward("belöning2", 50, "belöning2");

    private static boolean registerTestUsers() {
        try {
            users.writeUserToFile(user1);
            users.writeUserToFile(user2);
            users.writeUserToFile(user3);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean createTestGroups() {
        try{

            group1.addUser(user1);
            group1.addUser(user2);
            group2.addUser(user1);
            group2.addUser(user3);
            user1.addGroupMembership(group1.getGroupID());
            user1.addGroupMembership(group2.getGroupID());
            user2.addGroupMembership(group1.getGroupID());
            user3.addGroupMembership(group2.getGroupID());
            users.updateUser(user1);
            users.updateUser(user2);
            users.updateUser(user3);
            group1.addChore(chore1);
            group1.addChore(chore2);
            group2.addChore(chore1);
            group2.addChore(chore2);
            group1.addReward(reward1);
            group1.addReward(reward2);
            group2.addReward(reward1);
            group2.addReward(reward2);
            group1.modifyUserPoints(user2, 100);
            group2.modifyUserPoints(user3, 100);
            groups.writeGroupToFile(group1);
            groups.writeGroupToFile(group2);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
    public static boolean resetTestData() {

        TestUtils.deleteGroup(group1);
        TestUtils.deleteGroup(group2);
        TestUtils.deleteUser(user1);
        TestUtils.deleteUser(user2);
        TestUtils.deleteUser(user3);
        TestUtils.deleteUser(user3);

        return true;
    }

    public static void main(String[] args) {
        resetTestData();
        registerTestUsers();
        createTestGroups();
    }

}
