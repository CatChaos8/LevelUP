package github.catchaos8.levelup.commands.get;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
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
    public static final String WISDOM =         "stat.levelup.wis";
    public static final String INTELLIGENCE =   "stat.levelup.int";



    public GetAllStatsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .then(Commands.literal("stats")
                        .then(Commands.literal("get")
                                .then(Commands.literal("all")
                                        .executes((this::execute))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();

        assert player != null;
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(getStats -> {
            player.sendSystemMessage(Component.literal(("Your stats are: ")));
            player.sendSystemMessage(Component.translatable(CONSTITUTION).append(Component.literal("" + getStats.getBaseStat(0))));
            player.sendSystemMessage(Component.translatable(DEXTERITY).append(Component.literal("" + getStats.getBaseStat(1))));
            player.sendSystemMessage(Component.translatable(STRENGTH).append(Component.literal("" + getStats.getBaseStat(2))));
            player.sendSystemMessage(Component.translatable(VITALITY).append(Component.literal("" + getStats.getBaseStat(3))));
            player.sendSystemMessage(Component.translatable(ENDURANCE).append(Component.literal("" + getStats.getBaseStat(4))));
            player.sendSystemMessage(Component.translatable(WISDOM).append(Component.literal("" + getStats.getBaseStat(5))));
            player.sendSystemMessage(Component.translatable(INTELLIGENCE).append(Component.literal("" + getStats.getBaseStat(6))));
            player.sendSystemMessage(Component.translatable(FREEPOINTS).append(Component.literal("" + getStats.getInfo(0))));
            player.sendSystemMessage(Component.translatable(CLASSXP).append(Component.literal("" + getStats.getInfo(1))));
            player.sendSystemMessage(Component.translatable(CLASSLVL).append(Component.literal("" + getStats.getInfo(2))));

        });

        return 1;
    }

}
