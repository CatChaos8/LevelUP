package github.catchaos8.levelup.client;

public class ClientStatData {
    private static int playerStats[];

    public static void set(int[] stats) {
        ClientStatData.playerStats = stats;
    }

    public static int[] getPlayerStats() {
        return playerStats;
    }

}
