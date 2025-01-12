package github.fixinggithub.levelup.commands.set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import github.fixinggithub.levelup.networking.ModNetwork;
import github.fixinggithub.levelup.networking.packet.StatDataSyncS2CPacket;
import github.fixinggithub.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetConCommand {
    public static final String CONSTITUTION =       "stat.levelup.con";
    public SetConCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .requires(commandSource -> commandSource.hasPermission(4))
                .then(Commands.literal("stats")
                .then(Commands.literal("set")
                        .then(Commands.literal("constitution")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((this::execute)))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();

        int amount = IntegerArgumentType.getInteger(context, "amount");

        assert player != null;
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            stats.setStat(0, amount);
            ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);

            player.sendSystemMessage(Component.translatable(CONSTITUTION).append(Component.literal("" + stats.getStat(0))));
        });

        return 1;
    }

}
