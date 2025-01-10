package github.catchaos8.levelup.commands.set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.IncreaseStatC2SPacket;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetFreePointsCommand {

    public static final String FREEPOINTS =       "stat.levelup.fp";

    public SetFreePointsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .requires(commandSource -> commandSource.hasPermission(4))
                .then(Commands.literal("stats")
                .then(Commands.literal("set")
                        .then(Commands.literal("freepoints")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((this::execute)))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();

        int amount = IntegerArgumentType.getInteger(context, "amount");

        assert player != null;
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

            stats.setStat(5, amount);
            player.sendSystemMessage(Component.translatable(FREEPOINTS).append(Component.literal("" + stats.getStat(5))));
            ModNetwork.sendToServer(new IncreaseStatC2SPacket(5, 0));
        });

        return 1;
    }

}
