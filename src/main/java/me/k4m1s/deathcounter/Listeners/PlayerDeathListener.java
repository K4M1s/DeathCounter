package me.k4m1s.deathcounter.Listeners;

import me.k4m1s.deathcounter.Chat.Messages;
import me.k4m1s.deathcounter.Database.DatabaseHelper;
import me.k4m1s.deathcounter.DeathCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (DeathCounter.getDeathPlayers().get(player.getUniqueId())) return;

        EntityDamageEvent ede = player.getLastDamageCause();
        if (ede == null) return;
        EntityDamageEvent.DamageCause dc = ede.getCause();

        DeathCounter.getDeathPlayers().put(player.getUniqueId(), true);

        DatabaseHelper.addPlayerDeath(player.getUniqueId().toString(), dc.toString(), null, null, result -> {
            if (!result) {
                Messages.sendMessage("An error has occurred while adding " + player.getDisplayName() + " death to the database");
            }
        });
    }
}
