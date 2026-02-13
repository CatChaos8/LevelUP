package github.catchaos8.levelup.commands.get;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

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
                                        .then(Commands.argument("player", EntityArgument.player())
                                        .executes((this::execute)))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayer();
        Player detected = EntityArgument.getPlayer(context, "player");
        int stat = IntegerArgumentType.getInteger(context, "stat");

        assert player != null;
        detected.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(getStats -> {
            if(stat == 0) {
                player.sendSystemMessage(Component.translatable(CONSTITUTION).append(Component.literal("" + getStats.getBaseStat(0))));
            } else if(stat == 1) {
                player.sendSystemMessage(Component.translatable(DEXTERITY).append(Component.literal("" + getStats.getBaseStat(1))));
            } else if(stat == 2) {
                player.sendSystemMessage(Component.translatable(STRENGTH).append(Component.literal("" + getStats.getBaseStat(2))));
            } else if(stat == 3) {
                player.sendSystemMessage(Component.translatable(VITALITY).append(Component.literal("" + getStats.getBaseStat(3))));
            } else if(stat == 4) {
                player.sendSystemMessage(Component.translatable(ENDURANCE).append(Component.literal("" + getStats.getBaseStat(4))));
            } else if(stat == 5) {
                player.sendSystemMessage(Component.translatable(FREEPOINTS).append(Component.literal("" + getStats.getInfo(0))));
            } else if(stat == 6) {
                player.sendSystemMessage(Component.translatable(CLASSXP).append(Component.literal("" + getStats.getInfo(1))));
            } else if(stat == 7) {
                player.sendSystemMessage(Component.translatable(CLASSLVL).append(Component.literal("" + getStats.getInfo(2))));
            }


        });

        return 1;
    }

}
