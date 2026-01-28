package github.catchaos8.levelup.commands.get;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class GetClassLevelCommand {
    public static final String CLASSLVL =       "stat.levelup.clvl";

    public GetClassLevelCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .then(Commands.literal("stats")
                .then(Commands.literal("get")
                        .then(Commands.literal("classlvl")
                                .then(Commands.argument("player", EntityArgument.player())
                .executes((this::execute)))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayer();
        Player detected = EntityArgument.getPlayer(context, "player");


        assert player != null;
        detected.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(getStats -> {
            player.sendSystemMessage(Component.translatable(CLASSLVL).append(Component.literal("" + getStats.getInfo(2))));
        });

        return 1;
    }

}
