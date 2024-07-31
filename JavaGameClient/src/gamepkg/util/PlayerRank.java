package gamepkg.util;

import java.io.Serializable;

public class PlayerRank implements Serializable{
    private String playerName;
    private int rewardCoins;
    private int rank;

    public PlayerRank(String playerName, int rewardCoins, int rank) {
        this.playerName = playerName;
        this.rewardCoins = rewardCoins;
        this.rank = rank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getRewardCoins() {
        return rewardCoins;
    }

    public void setRewardCoins(int rewardCoins) {
        this.rewardCoins = rewardCoins;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
