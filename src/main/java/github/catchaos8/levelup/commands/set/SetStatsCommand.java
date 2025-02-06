package github.catchaos8.levelup.commands.set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import github.catchaos8.levelup.lib.ModNetwork;
import github.catchaos8.levelup.lib.SetStats;
import github.catchaos8.levelup.networking.packet.StatDataSyncS2CPacket;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetStatsCommand {

    public static final String CONSTITUTION =   "stat.levelup.con";
    public static final String DEXTERITY =      "stat.levelup.dex";
    public static final String STRENGTH =       "stat.levelup.str";
    public static final String VITALITY =   "stat.levelup.vit";
    public static final String ENDURANCE =      "stat.levelup.end";
    public static final String FREEPOINTS =     "stat.levelup.fp";
    public static final String CLASSXP =        "stat.levelup.cxp";
    public static final String CLASSLVL =       "stat.levelup.clvl";


    public SetStatsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .requires(commandSource -> commandSource.hasPermission(4))
                .then(Commands.literal("stats")
                .then(Commands.literal("set")
                        .then(Commands.argument("stat", IntegerArgumentType.integer(0,7))
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((this::execute)))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        int stat = IntegerArgumentType.getInteger(context, "stat");
        int amount = IntegerArgumentType.getInteger(context, "amount");


        assert player != null;

        if(player.hasPermissions(2)) {
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

            stats.setStat(stat, amount);
            SetStats.setAttributeStat(amount, stat, player);

            if(stat == 0) {
                player.sendSystemMessage(Component.translatable(CONSTITUTION).append(Component.literal("" + stats.getStat(0))));
            } else if(stat == 1) {
                player.sendSystemMessage(Component.translatable(DEXTERITY).append(Component.literal("" + stats.getStat(1))));
            } else if(stat == 2) {
                player.sendSystemMessage(Component.translatable(STRENGTH).append(Component.literal("" + stats.getStat(2))));
            } else if(stat == 3) {
                player.sendSystemMessage(Component.translatable(VITALITY).append(Component.literal("" + stats.getStat(3))));
            } else if(stat == 4) {
                player.sendSystemMessage(Component.translatable(ENDURANCE).append(Component.literal("" + stats.getStat(4))));
            } else if(stat == 5) {
                player.sendSystemMessage(Component.translatable(FREEPOINTS).append(Component.literal("" + stats.getStat(5))));
            } else if(stat == 6) {
                player.sendSystemMessage(Component.translatable(CLASSXP).append(Component.literal("" + stats.getStat(6))));
            } else if(stat == 7) {
                player.sendSystemMessage(Component.translatable(CLASSLVL).append(Component.literal("" + stats.getStat(7))));
            }
            ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);

        });
    } else {
        player.sendSystemMessage(Component.translatable("cmd.levelup.noperms"));
    }

        return 1;
    }

}
