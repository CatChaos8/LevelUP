package github.catchaos8.levelup.client;

public class ClientLimitedStatData {
    private static int[] limitedPlayerStats;

    public static void setArr(int[] limitedStats) {
        ClientLimitedStatData.limitedPlayerStats = limitedStats;
    }
    public static void set(int limitedStatsType, int amount) {
        ClientLimitedStatData.limitedPlayerStats[limitedStatsType] = amount;
    }

    public static int[] getLimitedPlayerStats() {
        return limitedPlayerStats;
    }

    public int getLimitedStat(int type) {
        return limitedPlayerStats[type];
    }
}
