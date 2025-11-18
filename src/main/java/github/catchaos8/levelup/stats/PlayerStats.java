package github.catchaos8.levelup.stats;

import github.catchaos8.levelup.lib.StatType;
import net.minecraft.nbt.CompoundTag;

import java.util.Arrays;

public class PlayerStats {

    private final int min_stat = 0;

    private Float prevHP = 20f;

    private StatType[] statTypes = new StatType[] {
            new StatType(0.0f, 0.0f, "Constitution"),
            new StatType(0.0f, 0.0f, "Dexterity"),
            new StatType(0.0f, 0.0f, "Strength"),
            new StatType(0.0f, 0.0f, "Vitality"),
            new StatType(0.0f, 0.0f, "Endurance")
    };

    private float[] info = {
            0.0f, //Freepoints     5
            0.0f, //Class XP       6
            0.0f, //Class Level    7
    };

    public float getPrevHP() {
        return prevHP;
    }

    public void setPrevHP(Float prevHP) {
        this.prevHP = prevHP;
    }

    public float[] getInfoArr() {
        return this.info;
    }

    public int getLength() {
        return this.statTypes.length;
    }

    public float getInfo(int type) {
        return info[type];
    }

    public void addInfo(int statType, float add) {
        if(statType != 1) {
        this.info[statType] = Math.max(this.info[statType] + add, min_stat);
    } else {
        this.info[statType] = this.info[statType] + add;
    }
    }

    public void subInfo(int statType, float sub) {
        if(statType != 1) {
            this.info[statType] = Math.max(this.info[statType] - sub, min_stat);
        } else {
            this.info[statType] = this.info[statType] - sub;
        }
    }

    public void setInfo(int statType, float amount) {
        if(statType != 1) {
            this.info[statType] = Math.max(amount, min_stat);
        } else {
            this.info[statType] = amount;
        }
    }

    public StatType[] getStatsTypeArr() {
        return statTypes;
    }

    public float[] getStatsBaseArr() {
        float[] array = new float[statTypes.length];
        for (int i = 0; i < statTypes.length; i++) {
            array[i] = statTypes[i].getBase();
        }
        return array;
    }

    public float[] getStatsLimitedArr() {
        float[] array = new float[statTypes.length];
        for (int i = 0; i < statTypes.length; i++) {
            array[i] = statTypes[i].getLimited();
        }
        return array;
    }

    public float getBaseStat(int type) {
        return statTypes[type].getBase();
    }

    public float getLimitedStat(int type) {
        return statTypes[type].getLimited();
    }

    public void setBaseStat(int type, float value) {
        statTypes[type].setBase(value);
    }

    public void setLimitedStat(int type, float value) {
        statTypes[type].setLimited(value);
    }

    public void addBaseStat(int type, float amount) {
        statTypes[type].setBase(statTypes[type].getBase() + amount);
    }

    public void addLimitedStat(int type, float amount) {
        statTypes[type].setLimited(statTypes[type].getLimited() + amount);
    }

    public void subBaseStat(int type, float amount) {
        statTypes[type].setBase(Math.max((statTypes[type].getBase() - amount), min_stat));
    }


    public void subLimitedStat(int type, float amount) {
        statTypes[type].setLimited(Math.max((statTypes[type].getLimited() - amount), min_stat));
    }

    public void copyFrom(PlayerStats source){
        this.info = Arrays.copyOf(source.info, source.info.length);
        this.statTypes = Arrays.copyOf(source.statTypes, source.statTypes.length);
    }

    public void saveNBTData(CompoundTag nbt) {
        for (StatType stat : statTypes) {
            String name = stat.getName().toLowerCase();
            nbt.putFloat(name, stat.getBase());
            nbt.putFloat("limit_" + name, stat.getLimited());
        }

        // Save info
        nbt.putFloat("freepoints", info[0]);
        nbt.putFloat("classxp", info[1]);
        nbt.putFloat("classLevel", info[2]);
    }

    public void loadNBTData(CompoundTag nbt) {
        //Loading base/limited
        for (StatType saved : statTypes) {
            String name = saved.getName().toLowerCase();
            saved.setBase(nbt.getFloat(name));
            saved.setLimited(nbt.getFloat("limit_" + name));
        }

        //Loading info
        info[0] = nbt.getFloat("freepoints");
        info[1] = nbt.getFloat("classxp");
        info[2] = nbt.getFloat("classLevel");
    }


}
