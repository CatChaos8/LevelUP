package github.catchaos8.levelup.lib;

public class StatType {
    private float base;
    private float limited;
    private final String name;

    public StatType(float base, float limited, String name) {
        this.base = base;
        this.limited = limited;
        this.name = name;
    }

    public float getBase() {
        return base;
    }
    public void setBase(float base) {
        this.base = base;
    }

    public float getLimited() { return limited; }
    public void setLimited(float limited) {
        this.limited = limited;
    }

    public String getName() {
        return name;
    }
}
