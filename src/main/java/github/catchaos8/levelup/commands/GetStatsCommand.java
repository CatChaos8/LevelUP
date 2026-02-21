package github.catchaos8.levelup.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import github.catchaos8.levelup.registries.ModAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GetStatsCommand {

    private static final Map<String, Integer> STAT_MAP;

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("constitution", 0);
        map.put("con", 0);
        map.put("dexterity", 1);
        map.put("dex", 1);
        map.put("strength", 2);
        map.put("str", 2);
        map.put("vitality", 3);
        map.put("vit", 3);
        map.put("wisdom", 4);
        map.put("wis", 4);
        map.put("intelligence", 5);
        map.put("int", 5);
        map.put("all", -1);
        STAT_MAP = Collections.unmodifiableMap(map);
    }

    private static final String[] STAT_NAMES = {"Constitution", "Dexterity", "Strength", "Vitality", "Wisdom", "Intelligence"};

    private static final SuggestionProvider<CommandSourceStack> STAT_SUGGESTIONS = (context, builder) ->
            SharedSuggestionProvider.suggest(STAT_MAP.keySet(), builder);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("stats")
                        .then(Commands.literal("get")
                                .then(Commands.argument("stat", com.mojang.brigadier.arguments.StringArgumentType.word())
                                        .suggests(STAT_SUGGESTIONS)
                                        .executes(GetStatsCommand::executeSelf)
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(GetStatsCommand::executeOther))))));
    }

    private static int executeSelf(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer executor = context.getSource().getPlayerOrException();
        return execute(context, executor, executor);
    }

    private static int executeOther(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer executor = context.getSource().getPlayerOrException();
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        return execute(context, executor, target);
    }

    private static int execute(CommandContext<CommandSourceStack> context, ServerPlayer executor, ServerPlayer target) {
        String statInput = com.mojang.brigadier.arguments.StringArgumentType.getString(context, "stat").toLowerCase();

        Integer statIndex = STAT_MAP.get(statInput);

        if (statIndex == null) {
            executor.sendSystemMessage(Component.literal("Invalid stat! Use: con, dex, str, vit, wis, int, or all"));
            return 0;
        }

        int[] baseStats = target.getData(ModAttachments.STATS);
        int[] limitedStats = target.getData(ModAttachments.LIMITED_STATS);

        String playerName = target.getName().getString();

        if (statIndex == -1) {
            // Show ALL stats
            executor.sendSystemMessage(Component.literal("=== Stats for " + playerName + " ==="));
            for (int i = 0; i < baseStats.length; i++) {
                executor.sendSystemMessage(Component.literal(
                        STAT_NAMES[i] + ": " + baseStats[i] + " (Limited: " + limitedStats[i] + ")"
                ));
            }
        } else {
            // Show single stat
            executor.sendSystemMessage(Component.literal(
                    playerName + "'s " + STAT_NAMES[statIndex] + ": " + baseStats[statIndex] + " (Limited: " + limitedStats[statIndex] + ")"
            ));
        }

        return 1;
    }
}