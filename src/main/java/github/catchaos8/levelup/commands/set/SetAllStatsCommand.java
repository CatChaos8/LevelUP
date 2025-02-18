package github.catchaos8.levelup.commands.set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import github.catchaos8.levelup.lib.DisplayLevelScoreboard;
import github.catchaos8.levelup.lib.SetStats;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.StatDataSyncS2CPacket;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetAllStatsCommand {

    public static final String CONSTITUTION =   "stat.levelup.con";
    public static final String DEXTERITY =      "stat.levelup.dex";
    public static final String STRENGTH =       "stat.levelup.str";
    public static final String VITALITY =       "stat.levelup.vit";
    public static final String ENDURANCE =      "stat.levelup.end";
    public static final String FREEPOINTS =     "stat.levelup.fp";
    public static final String CLASSXP =        "stat.levelup.cxp";
    public static final String CLASSLVL =       "stat.levelup.clvl";



    public SetAllStatsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                        .requires(commandSource -> commandSource.hasPermission(2))
                .then(Commands.literal("stats")
                        .then(Commands.literal("set")
                                .then(Commands.literal("all")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .executes((this::execute)))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();

        int amount = IntegerArgumentType.getInteger(context, "amount");

        assert player != null;

        if(player.hasPermissions(2)) {

            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stat -> {

                for (int i = 0; i <= 7; i++) {
                    stat.setStat(i, amount);
                    SetStats.setAttributeStat(amount, i,player);
                }
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    int level = stats.getStat(7);
                    DisplayLevelScoreboard.updateLevel(player, level);
                });

                player.sendSystemMessage(Component.literal(("Your stats are: ")));
                player.sendSystemMessage(Component.translatable(CONSTITUTION).append(Component.literal("" + stat.getStat(0))));
                player.sendSystemMessage(Component.translatable(DEXTERITY).append(Component.literal("" + stat.getStat(1))));
                player.sendSystemMessage(Component.translatable(STRENGTH).append(Component.literal("" + stat.getStat(2))));
                player.sendSystemMessage(Component.translatable(VITALITY).append(Component.literal("" + stat.getStat(3))));
                player.sendSystemMessage(Component.translatable(ENDURANCE).append(Component.literal("" + stat.getStat(4))));
                player.sendSystemMessage(Component.translatable(FREEPOINTS).append(Component.literal("" + stat.getStat(5))));
                player.sendSystemMessage(Component.translatable(CLASSXP).append(Component.literal("" + stat.getStat(6))));
                player.sendSystemMessage(Component.translatable(CLASSLVL).append(Component.literal("" + stat.getStat(7))));

                ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stat.getStatArr()), player);
            });

        } else {
            player.sendSystemMessage(Component.translatable("cmd.levelup.noperms"));
        }
        return 1;
    }

}
