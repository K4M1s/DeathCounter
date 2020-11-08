package me.k4m1s.deathcounter.Listeners;

import me.k4m1s.deathcounter.DeathCounter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        DeathCounter.getDeathPlayers().put(e.getPlayer().getUniqueId(), false);
    }
}
