package github.fixinggithub.levelup.commands.get;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import github.fixinggithub.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class GetStatsCommand {

    public static final String CONSTITUTION =   "stat.levelup.con";
    public static final String DEXTERITY =      "stat.levelup.dex";
    public static final String STRENGTH =       "stat.levelup.str";
    public static final String VITALITY =       "stat.levelup.vit";
    public static final String ENDURANCE =      "stat.levelup.end";
    public static final String FREEPOINTS =     "stat.levelup.fp";
    public static final String CLASSXP =        "stat.levelup.cxp";
    public static final String CLASSLVL =       "stat.levelup.clvl";

    public GetStatsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .then(Commands.literal("stats")
                        .then(Commands.literal("get")
                                .then(Commands.argument("stat", IntegerArgumentType.integer(0,7))
                                        .executes((this::execute))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        int stat = IntegerArgumentType.getInteger(context, "stat");

        assert player != null;
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(getStats -> {
            if(stat == 0) {
                player.sendSystemMessage(Component.translatable(CONSTITUTION).append(Component.literal("" + getStats.getStat(0))));
            } else if(stat == 1) {
                player.sendSystemMessage(Component.translatable(DEXTERITY).append(Component.literal("" + getStats.getStat(1))));
            } else if(stat == 2) {
                player.sendSystemMessage(Component.translatable(STRENGTH).append(Component.literal("" + getStats.getStat(2))));
            } else if(stat == 3) {
                player.sendSystemMessage(Component.translatable(VITALITY).append(Component.literal("" + getStats.getStat(3))));
            } else if(stat == 4) {
                player.sendSystemMessage(Component.translatable(ENDURANCE).append(Component.literal("" + getStats.getStat(4))));
            } else if(stat == 5) {
                player.sendSystemMessage(Component.translatable(FREEPOINTS).append(Component.literal("" + getStats.getStat(5))));
            } else if(stat == 6) {
                player.sendSystemMessage(Component.translatable(CLASSXP).append(Component.literal("" + getStats.getStat(6))));
            } else if(stat == 7) {
                player.sendSystemMessage(Component.translatable(CLASSLVL).append(Component.literal("" + getStats.getStat(7))));
            }


        });

        return 1;
    }

}
