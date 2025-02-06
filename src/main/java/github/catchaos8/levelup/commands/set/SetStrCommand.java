package github.catchaos8.levelup.commands.set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.lib.SetStats;
import github.catchaos8.levelup.networking.packet.StatDataSyncS2CPacket;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetStrCommand {

    public static final String STRENGTH =       "stat.levelup.str";

    public SetStrCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .requires(commandSource -> commandSource.hasPermission(4))
                .then(Commands.literal("stats")
                .then(Commands.literal("set")
                        .then(Commands.literal("strength")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((this::execute)))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();

        int amount = IntegerArgumentType.getInteger(context, "amount");

        assert player != null;
        if(player.hasPermissions(2)) {
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            stats.setStat(2, amount);
            SetStats.setAttributeStat(amount, 2,player);
            ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);

            player.sendSystemMessage(Component.translatable(STRENGTH).append(Component.literal("" + stats.getStat(2))));
        });
        } else {
            player.sendSystemMessage(Component.translatable("cmd.levelup.noperms"));
        }
        return 1;
    }

}
