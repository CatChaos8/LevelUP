package github.catchaos8.levelup.items;

import github.catchaos8.levelup.networking.PacketFunctions;
import github.catchaos8.levelup.registries.ModAttachments;
import github.catchaos8.levelup.registries.ModAttributes;
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

public class OrbOfTheVoid extends Item {
    public OrbOfTheVoid(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        player.getCooldowns().addCooldown(this, 10*20);

        if(!level.isClientSide) {
            float total = 0;
            int[] statsBaseArr = player.getData(ModAttachments.STATS);
            int[] newLimited = player.getData(ModAttachments.LIMITED_STATS);
            ModAttachments.ProgressData progressData = new ModAttachments.ProgressData(0, 0.0, 0.0);

            for(int i = 0; i < statsBaseArr.length; i++) {
                total += statsBaseArr[i];

                statsBaseArr[i] = 0;
                newLimited[i] = 0;
            }

            player.setData(ModAttachments.PROGRESS, progressData);
            player.setData(ModAttachments.LIMITED_STATS, newLimited);
            player.setData(ModAttachments.STATS, statsBaseArr);
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
