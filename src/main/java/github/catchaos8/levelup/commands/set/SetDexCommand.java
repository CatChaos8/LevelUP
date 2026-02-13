package github.catchaos8.levelup.commands.set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.catchaos8.levelup.lib.SetStats;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.StatDataSyncS2CPacket;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetDexCommand {

    public static final String DEXTERITY =       "stat.levelup.dex";

    public SetDexCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .requires(commandSource -> commandSource.hasPermission(4))
                .then(Commands.literal("stats")
                .then(Commands.literal("set")
                        .then(Commands.literal("dexterity")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .then(Commands.argument("player", EntityArgument.player())
                                        .executes((this::execute))))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayer();
        ServerPlayer detected = EntityArgument.getPlayer(context, "player");

        int amount = IntegerArgumentType.getInteger(context, "amount");

        assert player != null;
        if(player.hasPermissions(2)) {
            detected.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                stats.setLimitedStat(1, stats.getLimitedStat(1)-stats.getBaseStat(1) + amount);
                stats.setBaseStat(1, amount);
                SetStats.setAttributeStat(amount, 1 ,detected);
                ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getInfoArr(), stats.getStatsTypeArr()), detected);

                player.sendSystemMessage(Component.translatable(DEXTERITY).append(Component.literal("" + stats.getBaseStat(1))));
            });
        } else {
            player.sendSystemMessage(Component.translatable("cmd.levelup.noperms"));
        }

        return 1;
    }

}
