package me.k4m1s.deathcounter.Listeners;

import me.k4m1s.deathcounter.DeathCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (DeathCounter.getPlugin().getConfig().getBoolean("displaySidebar")) {
            DeathCounter.getSidebarManager().removePlayerBoard(player);
        }
        DeathCounter.getDeathPlayers().remove(player.getUniqueId());
    }
}
