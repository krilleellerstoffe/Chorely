package service;

import shared.transferable.Chore;
import shared.transferable.Reward;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ChoreRewardQueries {

    private QueryExecutor queryExecutor;

    public ChoreRewardQueries(QueryExecutor queryExecutor) {

        this.queryExecutor = queryExecutor;
    }
    /**
     * Create a chore within a given group
     * @param chore
     * @return
     */
    public boolean createChore (Chore chore) {
        boolean success = false;
        String sqlSafeName = makeSqlSafe(chore.getName());
        String sqlSafeDesc = makeSqlSafe(chore.getName());
        int points = chore.getScore();
        int groupID = chore.getGroupID();
        String query = "INSERT INTO [Chore] VALUES ('" + sqlSafeName + "', '" + groupID + "', '" + sqlSafeDesc + "', '" + points + "', null)";
        try {
            queryExecutor.executeUpdateQuery(query);
            success = true;
        }
        catch (SQLException sqlException) {
            //todo will fail if duplicate chore found -> throw appropriate error message
            sqlException.printStackTrace();
        }
        return success;
    }

    /**
     * Update a chores attributes, note name and groupID cannot be changed
     * once chore created
     * @param chore
     * @return
     */
    public boolean updateChore(Chore chore) {
        boolean choreUpdated = false;
        String sqlSafeName = makeSqlSafe(chore.getName());
        int groupID = chore.getGroupID();
        String sqlSafeDesc = makeSqlSafe(chore.getDescription());
        int points = chore.getScore();
        String lastUser = chore.getLastDoneByUser();
        String query = "UPDATE [Chore] SET" +
                " chore_description = '" + sqlSafeDesc + "'," +
                " chore_points = " + points + "," +
                " last_user = '" + lastUser + "'" +
                " WHERE chore_name = '" + sqlSafeName + "' AND group_id = " + groupID;
        try {
            Statement statement = queryExecutor.beginTransaction();
            statement.executeUpdate(query);
            queryExecutor.endTransaction();
            choreUpdated = true;
        }
        catch (SQLException sqlException) {

            try {
                System.out.println(sqlException);
                queryExecutor.rollbackTransaction();
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return choreUpdated;

    }

    public boolean deleteChore(Chore chore) {
        boolean choreDeleted = false;
        String sqlSafeName = makeSqlSafe(chore.getName());
        int groupID = chore.getGroupID();
        String query = "DELETE FROM [Chore] WHERE chore_name = '" + sqlSafeName + "' AND group_id = " + groupID;
        try {
            queryExecutor.executeUpdateQuery(query);
            choreDeleted = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return choreDeleted;
    }

    /**
     * gets a chore's information based on it's name and the
     * group it belongs to
     * @param choreName chore to be found
     * @param groupID group it's in
     * @return chore with details/null if not found
     */
    public Chore getChore(String choreName, int groupID) {
        Chore gotChore = null;
        String sqlSafeName = makeSqlSafe(choreName);
        String query = "SELECT * FROM [Chore] WHERE chore_name = '" + sqlSafeName + "' AND group_id = " + groupID;
        try {
            ResultSet resultSet = queryExecutor.executeReadQuery(query);
            if (resultSet.next()) {
                gotChore = new Chore(
                        resultSet.getString("chore_name"),
                        resultSet.getInt("chore_points"),
                        resultSet.getString("chore_description"),
                        resultSet.getInt("group_id"));
                if (resultSet.getString("last_user")!=null) {
                    gotChore.setLastDoneByUser(resultSet.getString("last_user"));
                }
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return gotChore;
    }

    /**
     * Create a reward within a given group
     * @param reward to be created
     * @return
     */
    public boolean createReward (Reward reward) {
        boolean success = false;
        String sqlSafeName = makeSqlSafe(reward.getName());
        String sqlSafeDesc = makeSqlSafe(reward.getName());
        int points = reward.getRewardPrice();
        int groupID = reward.getGroupID();
        String query = "INSERT INTO [Reward] VALUES ('" + sqlSafeName + "', '" + groupID + "', '" + sqlSafeDesc + "', '" + points + "', null)";
        try {
            queryExecutor.executeUpdateQuery(query);
            success = true;
        }
        catch (SQLException sqlException) {
            //todo will fail if duplicate chore found -> throw appropriate error message
            sqlException.printStackTrace();
        }
        return success;
    }

    /**
     * Update a rewards attributes, note name and groupID cannot be changed
     * once created
     * @param reward to be updated
     * @return
     */
    public boolean updateReward(Reward reward) {
        boolean rewardUpdated = false;
        String sqlSafeName = makeSqlSafe(reward.getName());
        int groupID = reward.getGroupID();
        String sqlSafeDesc = makeSqlSafe(reward.getDescription());
        int points = reward.getRewardPrice();
        String lastUser = reward.getLastDoneByUser();
        String query = "UPDATE [Chore] SET" +
                " reward_description = '" + sqlSafeDesc + "'," +
                " reward_price = " + points + "," +
                " last_user = '" + lastUser + "'" +
                " WHERE reward_name = '" + sqlSafeName + "' AND group_id = " + groupID;
        try {
            Statement statement = queryExecutor.beginTransaction();
            statement.executeUpdate(query);
            queryExecutor.endTransaction();
            rewardUpdated = true;
        }
        catch (SQLException sqlException) {

            try {
                System.out.println(sqlException);
                queryExecutor.rollbackTransaction();
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rewardUpdated;

    }

    public boolean deleteReward(Reward reward) {
        boolean rewardDeleted = false;
        String sqlSafeName = makeSqlSafe(reward.getName());
        int groupID = reward.getGroupID();
        String query = "DELETE FROM [Reward] WHERE reward_name = '" + sqlSafeName + "' AND group_id = " + groupID;
        try {
            queryExecutor.executeUpdateQuery(query);
            rewardDeleted = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return rewardDeleted;
    }

    private static String makeSqlSafe(String string) {
        //simple (but not secure) method to clean sql input
        return string.replace("'", "''");
    }
    /**
     * gets a reward's information based on it's name and the
     * group it belongs to
     * @param rewardName chore to be found
     * @param groupID group it's in
     * @return chore with details/null if not found
     */
    public Reward getReward(String rewardName, int groupID) {
        Reward gotReward = null;
        String sqlSafeName = rewardName;
        String query = "SELECT * FROM [Reward] WHERE reward_name = '" + sqlSafeName + "' AND group_id = " + groupID;
        try {
            ResultSet resultSet = queryExecutor.executeReadQuery(query);
            if (resultSet.next()) {
                gotReward = new Reward(
                        resultSet.getString("reward_name"),
                        resultSet.getInt("reward_price"),
                        resultSet.getString("reward_description"),
                        resultSet.getInt("group_id"));
                if (resultSet.getString("last_user")!=null) {
                    gotReward.setLastDoneByUser(resultSet.getString("last_user"));
                }
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return gotReward;
    }
}
