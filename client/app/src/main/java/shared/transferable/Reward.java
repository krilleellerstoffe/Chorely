package shared.transferable;



/**
 * Reward is a class that represents a reward in the application.
 * version 1.0 2020-04-08
 *
 * @autor Emma Svensson
 */
public class Reward implements Transferable {
    private String name;
    private int rewardPrice;
    private String description;


    public Reward(String name, int rewardPrice) {
        this.name = name;
        this.rewardPrice = rewardPrice;
    }

    public Reward(String name, int rewardPrice, String description) {
        this.name = name;
        this.rewardPrice = rewardPrice;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getRewardPrice() {
        return rewardPrice;
    }

    public String getDescription() {
        return description;
    }

    public void updateReward(Reward reward){
        this.name = reward.getName();
        this.description = reward.getDescription();
        this.rewardPrice = reward.getRewardPrice();
    }

    public boolean nameEquals(Reward reward){
        return reward.getName().equals(this.name);
    }

    @Override
    public boolean equals( Object obj) {
        if (obj instanceof Reward){
            return (((Reward) obj).getName().equals(name) &&
                    ((Reward) obj).getDescription().equals(description) &&
                    ((Reward) obj).getRewardPrice() == rewardPrice);
        }
        else
            return false;
    }
}
