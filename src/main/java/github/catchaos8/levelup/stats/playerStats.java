package github.catchaos8.levelup.stats;


import net.minecraft.nbt.CompoundTag;

public class playerStats {

    private final int min_stat = 0;

    private int dexterity = 0;
    private int constitution = 0;
    private int strength = 0;
    private int intelligence = 0;
    private int endurance = 0;

    public int getDext() {
        return dexterity;
    }
    public int getCons() {
        return constitution;
    }
    public int getStre() {
        return strength;
    }
    public int getInte() {
        return intelligence;
    }
    public int getEndu() {
        return endurance;
    }

    public void addDext(int add) {
        this.dexterity = dexterity + add;
    }
    public void addCons(int add) {
        this.constitution = constitution + add;
    }
    public void addStre(int add) {
        this.strength = strength + add;
    }
    public void addInte(int add) {
        this.intelligence = intelligence + add;
    }
    public void addEndu(int add) {
        this.endurance = endurance + add;
    }

    public void subDext(int sub) {
        this.dexterity = Math.min(dexterity - sub, min_stat);
    }
    public void subCons(int sub) {
        this.constitution = Math.min(constitution - sub, min_stat);
    }
    public void subStre(int sub) {
        this.strength = Math.min(strength - sub, min_stat);
    }
    public void subInte(int sub) {
        this.intelligence = Math.min(intelligence - sub, min_stat);
    }
    public void subEndu(int sub) {
        this.endurance = Math.min(endurance - sub, min_stat);
    }

    public void copyFrom(playerStats source){
        this.dexterity = source.dexterity;
        this.constitution = source.constitution;
        this.strength = source.strength;
        this.intelligence = source.intelligence;
        this.endurance = source.endurance;
    }


    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("dexterity", dexterity);
        nbt.putInt("constitution", constitution);
        nbt.putInt("strength", strength);
        nbt.putInt("intelligence", intelligence);
        nbt.putInt("endurance", endurance);
    }
}
