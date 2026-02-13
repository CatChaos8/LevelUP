package github.catchaos8.levelup.stats;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.common.capabilities.Capability;
import net.neoforged.common.capabilities.CapabilityManager;
import net.neoforged.common.capabilities.CapabilityToken;
import net.neoforged.common.capabilities.ICapabilityProvider;
import net.neoforged.common.util.INBTSerializable;
import net.neoforged.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerStatsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerStats> PLAYER_STATS = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });

    private PlayerStats stats = null;
    private final LazyOptional<PlayerStats> optional = LazyOptional.of(this::createPlayerStats);

    private PlayerStats createPlayerStats() {
        if (this.stats == null) {
            this.stats = new PlayerStats();
        }
        return this.stats;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_STATS) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerStats().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerStats().loadNBTData(nbt);
    }
}
