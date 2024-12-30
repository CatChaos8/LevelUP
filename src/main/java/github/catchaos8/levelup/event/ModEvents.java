package github.catchaos8.levelup.event;

import github.catchaos8.levelup.levelUP;
import github.catchaos8.levelup.stats.playerConstitution;
import github.catchaos8.levelup.stats.playerStatsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(playerStatsProvider.PLAYER_CONSTITUTION).isPresent()) {
                event.addCapability(new ResourceLocation(levelUP.MOD_ID, "properties"), new playerStatsProvider());
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(playerStatsProvider.PLAYER_CONSTITUTION).ifPresent(oldStore -> {
                event.getEntity().getCapability(playerStatsProvider.PLAYER_CONSTITUTION).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
    @SubscribeEvent
    public static void onRegisterCapabilites(RegisterCapabilitiesEvent event) {
        event.register(playerConstitution.class);
    }
}
