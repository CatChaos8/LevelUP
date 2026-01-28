package github.catchaos8.levelup.commands.set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.catchaos8.levelup.lib.DisplayLevelScoreboard;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.StatDataSyncS2CPacket;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class SetClassLevelCommand {
    public static final String CLASSLVL =       "stat.levelup.clvl";

    public SetClassLevelCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .requires(commandSource -> commandSource.hasPermission(4))
                .then(Commands.literal("stats")
                        .then(Commands.literal("set")
                                .then(Commands.literal("classlvl")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .then(Commands.argument("player", EntityArgument.player())
                                                .executes((this::execute))))))));

    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayer();
        Player detected = EntityArgument.getPlayer(context, "player");


        int amount = IntegerArgumentType.getInteger(context, "amount");

        assert player != null;
        if(player.hasPermissions(2)) {
            detected.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                stats.setInfo( 2, amount);
                ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getInfoArr(), stats.getStatsTypeArr()), player);
                float level = stats.getInfo(2);
                DisplayLevelScoreboard.updateLevel(player, level);
                player.sendSystemMessage(Component.translatable(CLASSLVL).append(Component.literal("" + level)));
            });

        } else {
            player.sendSystemMessage(Component.translatable("cmd.levelup.noperms"));
        }

        return 1;
    }

}
