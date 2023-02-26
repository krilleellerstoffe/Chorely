package service;

import shared.transferable.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class GroupQueries {

    private QueryExecutor queryExecutor;
    UserQueries userQueries;
    LeaderboardQueries leaderboardQueries;
    ChoreRewardQueries choreRewardQueries;

    public GroupQueries(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
        userQueries = queryExecutor.getUserQueries();
        leaderboardQueries = queryExecutor.getLeaderboardQueries();
        choreRewardQueries = queryExecutor.getChoreRewardQueries();
    }

    //add a group to the database
    public Group createGroup(String owner, String groupName, String description) {
        Group createdGroup = null;
        owner = makeSqlSafe(owner);
        groupName = makeSqlSafe(groupName);
        description = makeSqlSafe(description);
        String query = "INSERT INTO [Group] (group_name, group_owner, group_description) VALUES ('" + groupName + "', '" + owner + "', '" + description + "');" +
                "INSERT INTO [Member] VALUES ((select group_id from [Group] where group_name = '" + groupName + "' AND group_owner = '" + owner + "'), '" + owner + "', 0, 1);" +
                "SELECT * FROM [Group] WHERE group_id = (select group_id from [Group] where group_name = '" + groupName + "' AND group_owner = '" + owner + "');";
        try {
            ResultSet resultSet = queryExecutor.executeReadQuery(query);
            while (resultSet.next()) {
                int newGroupId = resultSet.getInt("group_id");
                String newGroupName = resultSet.getString("group_name");
                String newGroupDescription = resultSet.getString("group_description");
                createdGroup = new Group(newGroupId, newGroupName, owner, newGroupDescription);
                createdGroup.addMember(new User(owner));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return createdGroup;
    }


    public Group getGroup(int groupID) {
        Group group = null;
        String query = "SELECT * FROM [Group] WHERE group_id = " + groupID + ";";
        System.out.println(query);
        try {
            ResultSet resultSet = queryExecutor.executeReadQuery(query);
            if (resultSet.next()) {
                group = new Group(
                        resultSet.getInt("group_id"),
                        resultSet.getString("group_name"),
                        resultSet.getString("group_owner"),
                        resultSet.getString("group_description"));
                group.setLeaderboard(leaderboardQueries.getLeaderboard(groupID));
                group.setMembers(getGroupMembers(groupID).getMembers());
                group.setChores(choreRewardQueries.getChoreList(groupID));
                group.setRewards(choreRewardQueries.getRewardList(groupID));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return group;
    }

    public ArrayList<Group> getGrouplist(String sqlSafeUsername) {
        ArrayList<Group> groups = new ArrayList<>();
        String query = "SELECT * FROM [Group] INNER JOIN [Member] ON [Group].group_id = [Member].group_id WHERE user_name = '" + sqlSafeUsername + "';";
        System.out.println(query);
        try {
            ResultSet resultSet = queryExecutor.executeReadQuery(query);
            while (resultSet.next()) {
                int groupID = resultSet.getInt("group_id");
                Group group = new Group(
                        groupID,
                        resultSet.getString("group_name"),
                        resultSet.getString("group_owner"),
                        resultSet.getString("group_description"));
                group.setLeaderboard(leaderboardQueries.getLeaderboard(groupID));
                group.setMembers(getGroupMembers(groupID).getMembers());
                group.setChores(choreRewardQueries.getChoreList(groupID));
                group.setRewards(choreRewardQueries.getRewardList(groupID));
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public boolean updateGroupName(Group oldGroup, Group updatedGroup) {
        boolean nameUpdated = false;
        if(!oldGroup.getName().equals(updatedGroup.getName())) {
            String query = "UPDATE [Group]\nSET group_name = '" + makeSqlSafe(updatedGroup.getName())
                    + "'\nWHERE group_id = " + oldGroup.getGroupID() + ";";

            try {
                queryExecutor.executeUpdateQuery(query);
                nameUpdated = true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return nameUpdated;
    }

    public boolean updateGroupDescription(Group oldGroup, Group updatedGroup) {
        boolean updatedDesc = false;
        if(!oldGroup.getDescription().equals(updatedGroup.getDescription())) {
            String query = "UPDATE [Group]\nSET group_description = '" + makeSqlSafe(updatedGroup.getDescription())
                    + "'\nWHERE group_id = " + oldGroup.getGroupID() + ";";

            try {
                queryExecutor.executeUpdateQuery(query);
                updatedDesc = true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        return updatedDesc;
    }

    /**
     * Method adds a member to a given group,
     * with, by default, 0 points and no admin rights
     *
     * @param user  user to add to group
     * @param group group to be updated
     * @return the updated group if successful, null if not
     */
    public boolean addMember(User user, Group group) {
        int groupID = group.getIntGroupID();
        boolean success = false;
        String userName = makeSqlSafe(user.getUsername());
        String query = "INSERT INTO [Member] VALUES (" + groupID + ", '" + userName + "', 0 , 0);";
        System.out.println(query);
            try {
                queryExecutor.executeUpdateQuery(query);
                success = true;
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

        return success;
    }

    /**
     * Remove a member from a group, if the member is the owner,
     * remove the group and all it's members/chores/rewards
     *
     * @param user  name of member to remove
     * @param group group to be updated
     * @return true when success
     */
    public boolean removeMember(User user, Group group) {
        boolean memberRemoved = false;
        String owner = group.getOwner();
        String query;
            if (owner.equals(user.getUsername())) {
                query =          //remove chores
                        "DELETE FROM [Chore] WHERE group_id = '" + group.getGroupID() + "');" +
                                //remove rewards
                                "DELETE FROM [Reward] WHERE group_id = '" + group.getGroupID() + "');" +
                                //remove members
                                "DELETE FROM [Member] WHERE group_id = '" + group.getGroupID() + "');" +
                                //finally, remove group
                                "DELETE FROM [Group] WHERE group_id = '" + group.getGroupID() + "';";

            } else {
                query = "DELETE FROM [Member] WHERE user_name = '" + user.getUsername() + "' AND group_id = '" + group.getGroupID() + "'";
            }
        System.out.println(query);
            try {
                Statement statement = queryExecutor.beginTransaction();
                statement.executeUpdate(query);
                queryExecutor.endTransaction();
                memberRemoved = true;
            } catch (SQLException sqlException) {
                try {
                    System.out.println(sqlException);
                    queryExecutor.rollbackTransaction();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        return memberRemoved;
    }

    private static String makeSqlSafe(String string) {
        return string.replace("'", "''");
    }

    public boolean isUserAdmin(User user, Group group) {
        boolean isAdmin = false;
        String query = "SELECT * FROM [Member] WHERE user_name= " + user.getUsername() + " AND group_id= " + group.getIntGroupID();
        try {
            ResultSet resultSet = queryExecutor.executeReadQuery(query);
            if (resultSet.next()) {
                int adminBit = resultSet.getInt("is_admin");
                if (adminBit == 1) {
                    isAdmin = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAdmin;
    }

    /**
     * @param group
     */
    public boolean removeGroup(Group group) {
        boolean groupRemoved = false;
        String query =          //remove chores
                "DELETE FROM [Chore] WHERE group_id = " + group.getGroupID() + ";" +
                        //remove rewards
                        "DELETE FROM [Reward] WHERE group_id = " + group.getGroupID() + ";" +
                        //remove members
                        "DELETE FROM [Member] WHERE group_id = " + group.getGroupID() + ";" +
                        //finally, remove group
                        "DELETE FROM [Group] WHERE group_id = " + group.getGroupID() + ";";
        System.out.println(query);
        try {
            Statement statement = queryExecutor.beginTransaction();
            statement.executeUpdate(query);
            queryExecutor.endTransaction();
            groupRemoved = true;
        } catch (SQLException sqlException) {
            try {
                System.out.println(sqlException);
                queryExecutor.rollbackTransaction();
                groupRemoved = false;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return groupRemoved;
    }

    public boolean updateMembers(Group oldGroup, Group updatedGroup) {
        boolean membersUpdated = false;
        try {
            for (User u: oldGroup.getMembers()) { //remove if not in updated list and not owner
                if (!updatedGroup.getUsers().contains(u) && !Objects.equals(oldGroup.getOwner(), u.getUsername())) {
                    removeMember(u, oldGroup);
                }
            }
            for (User u : updatedGroup.getMembers()) { //add if not in old list
                if (!oldGroup.getUsers().contains(u)) {
                    addMember(u, updatedGroup);
                }
            }
            membersUpdated = true;
        }catch (Exception e) {
            membersUpdated = false;
        }
        return membersUpdated;
    }

    private Group getGroupMembers(int id) {
        Group groupWithMembers = new Group();
        String query = "SELECT * FROM [Member] WHERE group_id = " + id + ";";
        System.out.println(query);
        try {
            ResultSet resultSet = queryExecutor.executeReadQuery(query);
            while (resultSet.next()) {
                String userName = resultSet.getString("user_name");
                User member = new User(userName);
                groupWithMembers.addMember(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupWithMembers;
    }

    public boolean updateChores(Group oldGroup, Group updatedGroup) {
        boolean choresUpdated = false;
        try {
            ArrayList<Chore> oldChores = oldGroup.getChores();;
            for (Chore chore: oldChores) { //remove if not in updated list
                if (!updatedGroup.getChores().contains(chore)) {
                    choreRewardQueries.deleteChore(chore);
                }
            }
            for (Chore chore : updatedGroup.getChores()) {
                if (!oldChores.contains(chore)) { //add if not in old list
                    choreRewardQueries.createChore(chore);
                } else { //update if found
                    choreRewardQueries.updateChore(chore);
                }
            }
            choresUpdated = true;
        }catch (Exception e) {
            choresUpdated = false;
        }
        return choresUpdated;
    }

    public boolean updateRewards(Group oldGroup, Group updatedGroup) {
            boolean rewardsUpdated = false;
            try {
                ArrayList<Reward> oldRewards = oldGroup.getRewards();
                for (Reward reward: oldRewards) { //remove if not in updated list
                    if (!updatedGroup.getRewards().contains(reward)) {
                        choreRewardQueries.deleteReward(reward);
                    }
                }
                for (Reward reward : updatedGroup.getRewards()) {
                    if (!oldRewards.contains(reward)) { //add if not in old list
                        choreRewardQueries.createReward(reward);
                    } else { //update if found
                        choreRewardQueries.updateReward(reward);
                    }
                }
                rewardsUpdated = true;
            }catch (Exception e) {
                rewardsUpdated = false;
            }
            return rewardsUpdated;

    }
}
