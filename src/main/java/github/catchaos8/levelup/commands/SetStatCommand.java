package github.catchaos8.levelup.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import github.catchaos8.levelup.registries.ModAttachments;
import github.catchaos8.levelup.util.MakeAttributeModifiers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

import static github.catchaos8.levelup.networking.PacketFunctions.syncToPlayer;

public class SetStatCommand {

    private static final Map<String, Integer> STAT_MAP = Map.ofEntries(
            Map.entry("constitution", 0), Map.entry("con", 0),
            Map.entry("dexterity", 1), Map.entry("dex", 1),
            Map.entry("strength", 2), Map.entry("str", 2),
            Map.entry("vitality", 3), Map.entry("vit", 3),
            Map.entry("wisdom", 4), Map.entry("wis", 4),
            Map.entry("intelligence", 5), Map.entry("int", 5),
            Map.entry("all",-1)
    );

    private static final String[] STAT_NAMES = {"Consitution", "Dexterity", "Strength", "Vitality", "Wisdom", "Intelligence"};

    private static final SuggestionProvider<CommandSourceStack> STAT_SUGGESTIONS = (context, builder) ->
        SharedSuggestionProvider.suggest(STAT_MAP.keySet(), builder);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levelup")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("stats")
                        .then(Commands.literal("set")
                                .then(Commands.argument("stat", com.mojang.brigadier.arguments.StringArgumentType.word())
                                        .suggests(STAT_SUGGESTIONS)
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .executes(SetStatCommand::executeSelf)
                                                .then(Commands.argument("player", EntityArgument.player())
                                                        .executes(SetStatCommand::executeOther)))))));
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
        int amount = IntegerArgumentType.getInteger(context, "amount");

        // Get stat index from map
        Integer statIndex = STAT_MAP.get(statInput);

        if (statIndex == null) {
            executor.sendSystemMessage(Component.literal("Invalid stat! Use: con, dex, str, vit, wis, int, or all"));
            return 0;
        }

        // Get current stats
        int[] baseStats = target.getData(ModAttachments.STATS).clone();
        int[] limitedStats = target.getData(ModAttachments.LIMITED_STATS).clone();

        if (statIndex == -1) {
            // Set all stats
            for (int i = 0; i < baseStats.length; i++) {
                int difference = amount - baseStats[i];
                baseStats[i] = amount;
                limitedStats[i] = Math.max(0, limitedStats[i] + difference);
            }


            executor.sendSystemMessage(Component.literal("Set all stats to " + amount + " for " + target.getName().getString()));
        } else {
            // Set single stat
            int difference = amount - baseStats[statIndex];
            baseStats[statIndex] = amount;
            limitedStats[statIndex] = Math.max(0, limitedStats[statIndex] + difference);


            executor.sendSystemMessage(Component.literal("Set " + STAT_NAMES[statIndex] + " to " + amount + " for " + target.getName().getString()));
        }

        // Save and sync
        target.setData(ModAttachments.STATS, baseStats);
        target.setData(ModAttachments.LIMITED_STATS, limitedStats);
        MakeAttributeModifiers.makeModifiers(target);
        syncToPlayer(target);

        return 1;
    }
}

