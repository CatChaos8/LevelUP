package github.catchaos8.levelup.client;

public class ClientData {
    private static int level;
    private static double xp;
    private static double freepoints;
    private static int[] base;
    private static int[] limited;
    private static String username;

    public static void setLevel(int level) {
        ClientData.level = level;
    }

    public static void setXp(double xp) {
        ClientData.xp = xp;
    }

    public static void setFreepoints(double freepoints) {
        ClientData.freepoints = freepoints;
    }

    public static void setBase(int[] base) {
        ClientData.base = base;
    }

    public static void setLimited(int[] limited) {
        ClientData.limited = limited;
    }

    public static void setUsername(String username) {
        ClientData.username = username;
    }

    public static int getLevel() {
        return level;
    }

    public static double getXp() {
        return xp;
    }

    public static double getFreepoints() {
        return freepoints;
    }

    public static int[] getBase() {
        return base;
    }

    public static int[] getLimited() {
        return limited;
    }

    public static String getUsername() {
        return username;
    }
}
