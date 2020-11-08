package me.k4m1s.deathcounter.Listeners;

import me.k4m1s.deathcounter.Chat.Messages;
import me.k4m1s.deathcounter.Database.DatabaseHelper;
import me.k4m1s.deathcounter.DeathCounter;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if(player.getHealth() <= event.getDamage()){
                if (DeathCounter.getDeathPlayers().get(player.getUniqueId())) return;
                EntityDamageEvent ede = player.getLastDamageCause();
                if (ede == null) {
                    return;
                }
                EntityDamageEvent.DamageCause dc = ede.getCause();

                if (damager instanceof Arrow) {
                    ProjectileSource shooter = ((Arrow) damager).getShooter();
                    if (shooter instanceof Player) {
                        damager = (Player) shooter;
                    } else if (shooter instanceof Monster) {
                        damager = (Monster) shooter;
                    }
                } else if(damager instanceof ThrownPotion) {
                    // There should be code checking who thrown the potion, but idk how...
                }

                String killerUUID = (damager instanceof Player) ? damager.getUniqueId().toString() : null;
                String mobName = (damager instanceof Monster) ? damager.getName() : null;

                DeathCounter.getDeathPlayers().put(player.getUniqueId(), true);
                        Messages.sendMessage(player.getDisplayName() + " zginął przez EntityByEntityDamage");

                DatabaseHelper.addPlayerDeath(player.getUniqueId().toString(), dc.toString(), killerUUID, mobName, result -> {
                    if (result) {
                        Messages.sendMessage("An error has occurred while adding " + player.getDisplayName() + " death to the database");
                    }
                });
            }
        }
    }
}
