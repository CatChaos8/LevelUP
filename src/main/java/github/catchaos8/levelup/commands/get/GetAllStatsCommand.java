package github.catchaos8.levelup.commands.get;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

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
                                        .then(Commands.argument("player", EntityArgument.player())
                                        .executes((this::execute)))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer commander = context.getSource().getPlayer();

        Player detected = EntityArgument.getPlayer(context, "player");

        assert commander != null;

        detected.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(getStats -> {
            commander.sendSystemMessage(Component.literal(("Your stats are: ")));
            commander.sendSystemMessage(Component.translatable(CONSTITUTION).append(Component.literal("" + getStats.getBaseStat(0))));
            commander.sendSystemMessage(Component.translatable(DEXTERITY).append(Component.literal("" + getStats.getBaseStat(1))));
            commander.sendSystemMessage(Component.translatable(STRENGTH).append(Component.literal("" + getStats.getBaseStat(2))));
            commander.sendSystemMessage(Component.translatable(VITALITY).append(Component.literal("" + getStats.getBaseStat(3))));
            commander.sendSystemMessage(Component.translatable(ENDURANCE).append(Component.literal("" + getStats.getBaseStat(4))));
            commander.sendSystemMessage(Component.translatable(WISDOM).append(Component.literal("" + getStats.getBaseStat(5))));
            commander.sendSystemMessage(Component.translatable(INTELLIGENCE).append(Component.literal("" + getStats.getBaseStat(6))));
            commander.sendSystemMessage(Component.translatable(FREEPOINTS).append(Component.literal("" + getStats.getInfo(0))));
            commander.sendSystemMessage(Component.translatable(CLASSXP).append(Component.literal("" + getStats.getInfo(1))));
            commander.sendSystemMessage(Component.translatable(CLASSLVL).append(Component.literal("" + getStats.getInfo(2))));

        });

        return 1;
    }

}
