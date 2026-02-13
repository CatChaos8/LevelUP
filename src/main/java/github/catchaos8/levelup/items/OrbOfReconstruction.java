package github.catchaos8.levelup.items;

import github.catchaos8.levelup.lib.SetStats;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.StatDataSyncS2CPacket;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class OrbOfReconstruction extends Item {
    public OrbOfReconstruction(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        player.getCooldowns().addCooldown(this, 10*20);

        if(!level.isClientSide) {


            float[] total = {0};

            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                float[] statsBaseArr = stats.getStatsBaseArr();

                total[0] = 0;
                for(int i = 0; i < statsBaseArr.length; i++) {
                    total[0] += statsBaseArr[i];

                    stats.setBaseStat(i, 0);
                    stats.setLimitedStat(i, 0);
                    SetStats.setAttributeStat(0, i, (ServerPlayer) player);
                }

                stats.addInfo(0, total[0]);
                ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getInfoArr(), stats.getStatsTypeArr()), (ServerPlayer) player);
            });

            player.heal(0.1f);

            if(total[0] <= 0) {
                player.sendSystemMessage(Component.translatable("item.levelup.used_orb_of_reconstruction_fail"));
                return InteractionResultHolder.fail(item);
            }
            player.sendSystemMessage(Component.translatable("item.levelup.used_orb_of_reconstruction"));
            item.shrink(1);

        }




        return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
    }

}
