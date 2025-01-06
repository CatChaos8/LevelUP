package github.catchaos8.levelup.commands.get;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class GetAllStatsCommand {

    public static final String CONSTITUTION =   "stat.levelup.con";
    public static final String DEXTERITY =      "stat.levelup.dex";
    public static final String STRENGTH =       "stat.levelup.str";
    public static final String VITALITY  =      "stat.levelup.vit";
    public static final String ENDURANCE =      "stat.levelup.end";
    public static final String FREEPOINTS =     "stat.levelup.fp";
    public static final String CLASSXP =        "stat.levelup.cxp";
    public static final String CLASSLVL =       "stat.levelup.clvl";



    public GetAllStatsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .then(Commands.literal("stats")
                .then(Commands.literal("get")
                        .then(Commands.literal("all")
                .executes((this::execute))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayer();

        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(getStats -> {
            player.sendSystemMessage(Component.literal(("Your stats are: ")));
            player.sendSystemMessage(Component.translatable(CONSTITUTION).append(Component.literal("" + getStats.getStat(0))));
            player.sendSystemMessage(Component.translatable(DEXTERITY).append(Component.literal("" + getStats.getStat(1))));
            player.sendSystemMessage(Component.translatable(STRENGTH).append(Component.literal("" + getStats.getStat(2))));
            player.sendSystemMessage(Component.translatable(VITALITY).append(Component.literal("" + getStats.getStat(3))));
            player.sendSystemMessage(Component.translatable(ENDURANCE).append(Component.literal("" + getStats.getStat(4))));
            player.sendSystemMessage(Component.translatable(FREEPOINTS).append(Component.literal("" + getStats.getStat(5))));
            player.sendSystemMessage(Component.translatable(CLASSXP).append(Component.literal("" + getStats.getStat(6))));
            player.sendSystemMessage(Component.translatable(CLASSLVL).append(Component.literal("" + getStats.getStat(7))));

        });

        return 1;
    }

}
