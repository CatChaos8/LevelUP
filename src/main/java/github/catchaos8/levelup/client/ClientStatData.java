package github.catchaos8.levelup.client;

public class ClientStatData {
    private static float[] playerStats;

    public static void set(float[] stats) {
        ClientStatData.playerStats = stats;
    }

    public static float[] getPlayerStats() {
        return playerStats;
    }

    public float getStat(int type) {
        return playerStats[type];
    }
}
