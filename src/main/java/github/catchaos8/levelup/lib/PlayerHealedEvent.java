package github.catchaos8.levelup.lib;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class PlayerHealedEvent extends Event {
    private final Player player;
    private final Float healedAmount;

    public PlayerHealedEvent(Player player, float healedAmount) {
        this.player = player;
        this.healedAmount = healedAmount;
    }

    public Player getPlayer() {
        return player;
    }

    public Float getHealedAmount() {
        return healedAmount;
    }
}
