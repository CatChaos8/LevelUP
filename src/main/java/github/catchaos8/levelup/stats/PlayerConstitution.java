package github.catchaos8.levelup.stats;


import net.minecraft.nbt.CompoundTag;

public class PlayerConstitution {
    private int constitution = 0;
    private final int min_stat = 0;

    public int getCons() {
        return constitution;
    }

    public void addCons(int add) {
        this.constitution = constitution + add;
    }

    public void subCons(int sub) {
        this.constitution = Math.max(constitution - sub, min_stat);
    }

    public void copyFrom(PlayerConstitution source){
        this.constitution = source.constitution;
    }


    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("constitution", constitution);
    }
    public void loadNBTData(CompoundTag nbt) {
        constitution = nbt.getInt("constitution");
    }
}
