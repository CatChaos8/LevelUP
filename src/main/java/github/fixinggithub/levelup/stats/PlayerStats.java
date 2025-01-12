package github.fixinggithub.levelup.stats;

import net.minecraft.nbt.CompoundTag;

import java.util.Arrays;

public class PlayerStats {

    private final int min_stat = 0;

    private int[] stats = {
            0, //Con            0
            0, //Dex            1
            0, //Str            2
            0, //Vit            3
            0, //End            4
            0, //Freepoints     5
            0, //Class XP       6
            0, //Class Level    7
            0, //Limit Con      8
            0, //Limit Dex      9
            0, //Limit Str      10
            0, //Limit Vit      11
            0  //Limit End      12
    };

    public int[] getStatArr() {
        return this.stats;
    }

    public int getStat(int type) {
        return stats[type];
    }

    public void addStat(int statType, int add) {
        this.stats[statType] = stats[statType] + add;
    }

    public void subStat(int statType, int sub) {
        this.stats[statType] = Math.max(stats[statType] - sub, min_stat);
    }

    public void setStat(int statType, int amount) {
        this.stats[statType] = Math.max(amount, min_stat);
    }

    public void copyFrom(PlayerStats source){
        this.stats = Arrays.copyOf(source.stats, source.stats.length);
    }


    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("constitution", stats[0]);
        nbt.putInt("dexterity", stats[1]);
        nbt.putInt("strength", stats[2]);
        nbt.putInt("vitality", stats[3]);
        nbt.putInt("endurance", stats[4]);

        nbt.putInt("freepoints", stats[5]);
        nbt.putInt("classxp", stats[6]);
        nbt.putInt("classLevel", stats[7]);

        nbt.putInt("limit_con", stats[8]);
        nbt.putInt("limit_dex", stats[9]);
        nbt.putInt("limit_str", stats[10]);
        nbt.putInt("limit_vit", stats[11]);
        nbt.putInt("limit_end", stats[12]);
    }

    public void loadNBTData(CompoundTag nbt) {
        stats[0] = nbt.getInt("constitution");
        stats[1] = nbt.getInt("dexterity");
        stats[2] = nbt.getInt("strength");
        stats[3] = nbt.getInt("vitality");
        stats[4] = nbt.getInt("endurance");

        stats[5] = nbt.getInt("freepoints");
        stats[6] = nbt.getInt("classxp");
        stats[7] = nbt.getInt("classLevel");

        stats[8] = nbt.getInt("limit_con");
        stats[9] = nbt.getInt("limit_dex");
        stats[10] = nbt.getInt("limit_str");
        stats[11] = nbt.getInt("limit_vit");
        stats[12] = nbt.getInt("limit_end");
    }

}
