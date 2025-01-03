package github.catchaos8.levelup.commands.get;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class GetConCommand {
    public static final String CONSTITUTION =       "levelup.stat.con";
    public GetConCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .then(Commands.literal("stats")
                .then(Commands.literal("get")
                        .then(Commands.literal("constitution")
                .executes((this::execute))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayer();

        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(getStats -> {
            player.sendSystemMessage(Component.translatable(CONSTITUTION).append(Component.literal("" + getStats.getStat(0))));
        });

        return 1;
    }

}
