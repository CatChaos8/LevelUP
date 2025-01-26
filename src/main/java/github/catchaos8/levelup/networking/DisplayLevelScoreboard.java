package github.catchaos8.levelup.networking;

import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class DisplayLevelScoreboard {
    public static final String LEVEL_OBJECTIVE = "classlvl";

    public static void initializeScoreboard(ServerScoreboard scoreboard) {
        if(scoreboard.getObjective(LEVEL_OBJECTIVE) == null) {
            scoreboard.addObjective(
                    LEVEL_OBJECTIVE,
                    ObjectiveCriteria.DUMMY,
                    Component.translatable("scoreboard.levelup.lvl"),
                    ObjectiveCriteria.RenderType.INTEGER
            );
        }
    }

    public static void updateLevel(ServerPlayer player, int playerLevel) {

        ServerLevel serverLevel = player.serverLevel();
        ServerScoreboard scoreboard = serverLevel.getScoreboard();

        Objective levelObjective = scoreboard.getObjective(LEVEL_OBJECTIVE);
        assert levelObjective != null;
        scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), levelObjective).setScore(playerLevel);

    }
}
