package github.catchaos8.levelup.lib;

import github.catchaos8.levelup.config.LevelUPCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class DisplayLevelScoreboard {
    public static final String LEVEL_OBJECTIVE = "classlvl";

    // Initialize the scoreboard
    public static void initializeScoreboard(ServerScoreboard scoreboard) {
        if (scoreboard.getObjective(LEVEL_OBJECTIVE) == null) {
            // Add the objective to the scoreboard
            scoreboard.addObjective(
                    LEVEL_OBJECTIVE,
                    ObjectiveCriteria.DUMMY,
                    Component.translatable("scoreboard.levelup.lvl"), // Display name of the objective
                    ObjectiveCriteria.RenderType.INTEGER // Render as an integer (for player level, for example)
            );
        }

        // Get the objective after creation
        Objective levelObjective = scoreboard.getObjective(LEVEL_OBJECTIVE);


        // Set the objective to display in the slots
        if(LevelUPCommonConfig.DISPLAY_LEVEL_UNDER_NAME.get()) {
            scoreboard.setDisplayObjective(ServerScoreboard.DISPLAY_SLOT_BELOW_NAME, levelObjective);
        } else {
            if (scoreboard.getDisplayObjective(Scoreboard.DISPLAY_SLOT_BELOW_NAME) == levelObjective) {
                scoreboard.setDisplayObjective(ServerScoreboard.DISPLAY_SLOT_BELOW_NAME, null);
            }
        }

        if(LevelUPCommonConfig.DISPLAY_LEVEL_BESIDE_NAME_IN_PLAYER_LIST.get()) {
            scoreboard.setDisplayObjective(ServerScoreboard.DISPLAY_SLOT_LIST, levelObjective);
        } else {
            if (scoreboard.getDisplayObjective(Scoreboard.DISPLAY_SLOT_LIST) == levelObjective) {
                scoreboard.setDisplayObjective(ServerScoreboard.DISPLAY_SLOT_LIST, null);
            }
        }

        if(LevelUPCommonConfig.DISPLAY_LEVEL_UNDER_NAME_IN_SIDEBAR.get()) {
            scoreboard.setDisplayObjective(ServerScoreboard.DISPLAY_SLOT_SIDEBAR, levelObjective);
        } else {
            if (scoreboard.getDisplayObjective(Scoreboard.DISPLAY_SLOT_SIDEBAR) == levelObjective) {
                scoreboard.setDisplayObjective(ServerScoreboard.DISPLAY_SLOT_SIDEBAR, null);
            }
        }

    }

    // Update the player's level on the scoreboard/custom name
    public static void updateLevel(ServerPlayer player, int playerLevel) {
        ServerLevel serverLevel = player.serverLevel();
        ServerScoreboard scoreboard = serverLevel.getScoreboard();

        // Get the objective for the level
        Objective levelObjective = scoreboard.getObjective(LEVEL_OBJECTIVE);

        // If the objective is valid, update the player's score with the level text
        if (levelObjective != null) {
            // Set the score for the player to be the level with the prefix
            scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), levelObjective).setScore(playerLevel);
        }
    }

    public static void setName(PlayerEvent.TabListNameFormat event) {
        MutableComponent name = Component.empty();
        name.append((event.getDisplayName() != null ? event.getEntity().getDisplayName() : event.getEntity().getName()));

        name.append(Component.translatable("name.levelup.short").withStyle(ChatFormatting.GREEN));

        event.setDisplayName(name);
    }
}
