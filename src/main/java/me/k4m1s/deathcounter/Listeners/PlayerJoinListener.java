package me.k4m1s.deathcounter.Listeners;

import me.k4m1s.deathcounter.Database.DatabaseHelper;
import me.k4m1s.deathcounter.Chat.Messages;
import me.k4m1s.deathcounter.DeathCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        DatabaseHelper.updatePlayerNick(player, result -> {
            if (!result) {
                DatabaseHelper.addPlayerToDatabase(player, result1 -> {
                    if (!result1) {
                        Messages.sendMessage("An error has occurred while adding player " + player.getDisplayName() + " to the database.");
                    }
                });
            }
        });
        if (DeathCounter.getPlugin().getConfig().getBoolean("displaySidebar")) {
            DeathCounter.getSidebarManager().createBoardForPlayer(player);
        }
        DeathCounter.getDeathPlayers().put(player.getUniqueId(), false);
    }
}
