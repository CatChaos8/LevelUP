package github.catchaos8.levelup.stats;


import net.minecraft.nbt.CompoundTag;

public class PlayerStats {

    private final int min_stat = 0;
    private final int min_freepoints = 0;
    private final int min_classxp = 0;
    private final int min_classLevel = 1;

    private int[] stats = {
            0, //Dex
            0, //Con
            0, //Str
            0, //Int
            0  //End
    };

    private int freepoints = 0;
    private int classxp = 0;
    private int classLevel = 0;


    public int getDext() {
        return stats[0];
    }
    public int getCons() {
        return stats[1];
    }
    public int getStre() {
        return stats[2];
    }
    public int getInte() {
        return stats[3];
    }
    public int getEndu() {
        return stats[4];
    }

    public int getFreepoints() {
        return freepoints;
    }
    public int getClassxp() {
        return classxp;
    }
    public int getClassLevel() {
        return classLevel;
    }

    public void addDext(int add) {
        this.stats[0] = stats[0] + add;
    }
    public void addCons(int add) {
        this.stats[1] = stats[1] + add;
    }
    public void addStre(int add) {
        this.stats[2] = stats[2] + add;
    }
    public void addInte(int add) {
        this.stats[3] = stats[3] + add;
    }
    public void addEndu(int add) {
        this.stats[4] = stats[4] + add;
    }

    public void addFP(int add) {
        this.freepoints = freepoints + add;
    }
    public void addCXP(int add) {
        this.classxp = classxp + add;
    }
    public void setCLevel(int add) {
        this.classLevel = classLevel + add;
    }


    public void subDext(int sub) {
        this.stats[0] = Math.min(stats[0] - sub, min_stat);
    }
    public void subCons(int sub) {
        this.stats[1] = Math.min(stats[1] - sub, min_stat);
    }
    public void subStre(int sub) {
        this.stats[2] = Math.min(stats[2] - sub, min_stat);
    }
    public void subInte(int sub) {
        this.stats[3] = Math.min(stats[3] - sub, min_stat);
    }
    public void subEndu(int sub) {
        this.stats[4] = Math.min(stats[4] - sub, min_stat);
    }

    public void subFP(int sub) {
        this.freepoints = Math.min(freepoints - sub, min_freepoints);
    }
    public void subCXP(int sub) {
        this.classxp = Math.min(classxp - sub, min_classxp);
    }
    public void subCLevel(int sub) {
        this.classLevel = Math.min(classLevel - sub, min_classLevel);
    }

    public void copyFrom(PlayerStats source){
        this.stats[0] = source.stats[0];
        this.stats[1] = source.stats[1];
        this.stats[2] = source.stats[2];
        this.stats[3] = source.stats[3];
        this.stats[4] = source.stats[4];

        this.freepoints = source.freepoints;
        this.classxp = source.classxp;
        this.classLevel = source.classLevel;
    }


    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("dexterity", stats[0]);
        nbt.putInt("constitution", stats[1]);
        nbt.putInt("strength", stats[2]);
        nbt.putInt("intelligence", stats[3]);
        nbt.putInt("endurance", stats[4]);

        nbt.putInt("freepoints", freepoints);
        nbt.putInt("classxp", classxp);
        nbt.putInt("classLevel", classLevel);
    }
    public void loadNBTData(CompoundTag nbt) {
        stats[0] = nbt.getInt("dexterity");
        stats[1] = nbt.getInt("constitution");
        stats[2] = nbt.getInt("strength");
        stats[3] = nbt.getInt("intelligence");
        stats[4] = nbt.getInt("endurance");

        freepoints = nbt.getInt("freepoints");
        classxp = nbt.getInt("classxp");
        classLevel = nbt.getInt("classLevel");
    }

}
