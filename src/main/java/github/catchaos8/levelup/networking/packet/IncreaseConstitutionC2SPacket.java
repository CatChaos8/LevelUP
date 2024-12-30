package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class IncreaseConstitutionC2SPacket {
    public static final String MESSAGE_INCREASE_CON = "message.levelup.increase_con";

    public IncreaseConstitutionC2SPacket() {

    }

    public IncreaseConstitutionC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //This is the server side part I think
            ServerPlayer player = context.getSender();

            //Check if freestats is > 0
            if(hasEnoughPoints(player)) {
                //Increase Constitution

                player.getCapability(PlayerStatsProvider.PLAYER_CONSTITUTION).ifPresent(constitution -> {
                    constitution.addCons(1);
                    player.sendSystemMessage(Component.literal("Current Constitution " + constitution.getCons())
                            .withStyle(ChatFormatting.GREEN));
                });
                //Set Modifier to stats based on constitution

                //Tell player Constitution has increased & constitution
                player.sendSystemMessage(Component.translatable(MESSAGE_INCREASE_CON));
            } else {
                //say not enough points

                //tell playerconstitution
            }

        });
        return true;
    }

    private boolean hasEnoughPoints(ServerPlayer player) {
        return true;
    }

}
