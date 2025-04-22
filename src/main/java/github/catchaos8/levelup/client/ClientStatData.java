package github.catchaos8.levelup.client;

import github.catchaos8.levelup.lib.StatType;

public class ClientStatData {
    private static float[] playerInfo;
    private static StatType[] statTypes;

    public static void setStatTypes(StatType[] statTypes) {
        ClientStatData.statTypes = statTypes;
    }

    public static void setPlayerInfo(float[] playerInfo) {
        ClientStatData.playerInfo = playerInfo;
    }

    public static float[] getPlayerInfo() {
        return playerInfo;
    }

    public static StatType[] getStatTypes() {
        return statTypes;
    }

}
