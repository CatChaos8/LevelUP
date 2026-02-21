package github.catchaos8.levelup.items;

import github.catchaos8.levelup.networking.PacketFunctions;
import github.catchaos8.levelup.registries.ModAttachments;
import github.catchaos8.levelup.util.MakeAttributeModifiers;
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

            ModAttachments.ProgressData oldProgress = player.getData(ModAttachments.PROGRESS);

            int total = 0;

            int[] base = player.getData(ModAttachments.STATS);
            int[] limited = player.getData(ModAttachments.LIMITED_STATS);
            for (int i = 0; i < base.length; i++) {
                total += base[i];
                base[i] = 0;
                limited[i] = 0;
            }

            ModAttachments.ProgressData newProgress = new ModAttachments.ProgressData(oldProgress.level(), oldProgress.xp(), oldProgress.freePoints() + total);

            player.setData(ModAttachments.STATS, base);
            player.setData(ModAttachments.LIMITED_STATS, limited);
            player.setData(ModAttachments.PROGRESS, newProgress);
            MakeAttributeModifiers.makeModifiers((ServerPlayer) player);
            PacketFunctions.syncToPlayer((ServerPlayer) player);
            player.heal(0.1f);

            if(total <= 0) {
                player.sendSystemMessage(Component.translatable("item.levelup.used_orb_of_reconstruction_fail"));
                return InteractionResultHolder.fail(item);
            }
            player.sendSystemMessage(Component.translatable("item.levelup.used_orb_of_reconstruction"));
            item.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
    }


}
