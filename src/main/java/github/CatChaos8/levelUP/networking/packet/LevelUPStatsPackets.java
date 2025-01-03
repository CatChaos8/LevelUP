package github.catchaos8.levelup.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LevelUPStatsPackets {
    public LevelUPStatsPackets() {

    }

    public LevelUPStatsPackets(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //This is the server side part I think
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
        });
        return true;
    }

}
