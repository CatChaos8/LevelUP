package github.catchaos8.levelup.stats;

import net.minecraft.nbt.CompoundTag;

import java.util.Arrays;

public class PlayerStats {

    private final int min_stat = 0;

    private float[] stats = {
            0.0f, //Con            0
            0.0f, //Dex            1
            0.0f, //Str            2
            0.0f, //Vit            3
            0.0f, //End            4
            0.0f, //Freepoints     5
            0.0f, //Class XP       6
            0.0f, //Class Level    7
            0.0f, //Limit Con      8
            0.0f, //Limit Dex      9
            0.0f, //Limit Str      10
            0.0f, //Limit Vit      11
            0.0f  //Limit End      12
    };

    public float[] getStatArr() {
        return this.stats;
    }

    public float getStat(int type) {
        return stats[type];
    }

    public void addStat(int statType, float add) {
        this.stats[statType] = stats[statType] + add;
    }

    public void subStat(int statType, float sub) {
        this.stats[statType] = Math.max(stats[statType] - sub, min_stat);
    }

    public void setStat(int statType, float amount) {
        this.stats[statType] = Math.max(amount, min_stat);
    }

    public void copyFrom(PlayerStats source){
        this.stats = Arrays.copyOf(source.stats, source.stats.length);
    }


    public void saveNBTData(CompoundTag nbt) {
        nbt.putFloat("constitution", stats[0]);
        nbt.putFloat("dexterity", stats[1]);
        nbt.putFloat("strength", stats[2]);
        nbt.putFloat("vitality", stats[3]);
        nbt.putFloat("endurance", stats[4]);

        nbt.putFloat("freepoints", stats[5]);
        nbt.putFloat("classxp", stats[6]);
        nbt.putFloat("classLevel", stats[7]);

        nbt.putFloat("limit_con", stats[8]);
        nbt.putFloat("limit_dex", stats[9]);
        nbt.putFloat("limit_str", stats[10]);
        nbt.putFloat("limit_vit", stats[11]);
        nbt.putFloat("limit_end", stats[12]);
    }

    public void loadNBTData(CompoundTag nbt) {
        stats[0] = nbt.getFloat("constitution");
        stats[1] = nbt.getFloat("dexterity");
        stats[2] = nbt.getFloat("strength");
        stats[3] = nbt.getFloat("vitality");
        stats[4] = nbt.getFloat("endurance");

        stats[5] = nbt.getFloat("freepoints");
        stats[6] = nbt.getFloat("classxp");
        stats[7] = nbt.getFloat("classLevel");

        stats[8] = nbt.getFloat("limit_con");
        stats[9] = nbt.getFloat("limit_dex");
        stats[10] = nbt.getFloat("limit_str");
        stats[11] = nbt.getFloat("limit_vit");
        stats[12] = nbt.getFloat("limit_end");
    }

}
