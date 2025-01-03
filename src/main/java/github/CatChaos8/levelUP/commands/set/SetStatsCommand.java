package github.catchaos8.levelup.commands.set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetStatsCommand {

    public static final String CONSTITUTION =   "levelup.stat.con";
    public static final String DEXTERITY =      "levelup.stat.dex";
    public static final String STRENGTH =       "levelup.stat.str";
    public static final String INTELLIGENCE =   "levelup.stat.int";
    public static final String ENDURANCE =      "levelup.stat.end";
    public static final String FREEPOINTS =     "levelup.stat.fp";
    public static final String CLASSXP =        "levelup.stat.cxp";
    public static final String CLASSLVL =       "levelup.stat.clvl";


    public SetStatsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .then(Commands.literal("stats")
                .then(Commands.literal("set")
                        .then(Commands.argument("stat", IntegerArgumentType.integer(0,7))
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((this::execute)))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayer();
        int stat = IntegerArgumentType.getInteger(context, "stat");
        int amount = IntegerArgumentType.getInteger(context, "amount");


        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

            stats.setStat(stat, amount);

            if(stat == 0) {
                player.sendSystemMessage(Component.translatable(CONSTITUTION).append(Component.literal("" + stats.getStat(0))));
            } else if(stat == 1) {
                player.sendSystemMessage(Component.translatable(DEXTERITY).append(Component.literal("" + stats.getStat(1))));
            } else if(stat == 2) {
                player.sendSystemMessage(Component.translatable(STRENGTH).append(Component.literal("" + stats.getStat(2))));
            } else if(stat == 3) {
                player.sendSystemMessage(Component.translatable(INTELLIGENCE).append(Component.literal("" + stats.getStat(3))));
            } else if(stat == 4) {
                player.sendSystemMessage(Component.translatable(ENDURANCE).append(Component.literal("" + stats.getStat(4))));
            } else if(stat == 5) {
                player.sendSystemMessage(Component.translatable(FREEPOINTS).append(Component.literal("" + stats.getStat(5))));
            } else if(stat == 6) {
                player.sendSystemMessage(Component.translatable(CLASSXP).append(Component.literal("" + stats.getStat(6))));
            } else if(stat == 7) {
                player.sendSystemMessage(Component.translatable(CLASSLVL).append(Component.literal("" + stats.getStat(7))));
            }


        });

        return 1;
    }

}
