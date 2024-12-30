package github.catchaos8.levelup.stats;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class playerStatsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<playerConstitution> PLAYER_CONSTITUTION = CapabilityManager.get(new CapabilityToken<playerConstitution>() { });

    private playerConstitution constitution = null;
    private final LazyOptional<playerConstitution> optional = LazyOptional.of(this::createPlayerConstitution);

    private playerConstitution createPlayerConstitution() {
        if (this.constitution == null) {
            this.constitution = new playerConstitution();
        }
        return this.constitution;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_CONSTITUTION) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerConstitution().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerConstitution().loadNBTData(nbt);
    }
}
