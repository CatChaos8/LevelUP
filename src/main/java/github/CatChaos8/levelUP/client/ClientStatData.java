package github.catchaos8.levelup.client;

public class ClientStatData {
    private static int[] playerStats;
    private static int[] limitedPlayerStats;

    public static void set(int[] stats, int[] limitedStats) {
        ClientStatData.playerStats = stats;
        ClientStatData.limitedPlayerStats = limitedStats;
    }

    public static int[] getPlayerStats() {
        return playerStats;
    }

    public static int[] getLimitedPlayerStats() {
        return limitedPlayerStats;
    }

    public int getStat(int type) {
        return playerStats[type];
    }

    public int getLimitedStat(int type) {
        return limitedPlayerStats[type];
    }
}
