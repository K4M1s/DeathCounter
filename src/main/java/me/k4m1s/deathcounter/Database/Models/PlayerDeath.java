package me.k4m1s.deathcounter.Database.Models;

import me.k4m1s.deathcounter.Chat.Messages;
import me.k4m1s.deathcounter.DeathCounter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDeath {
    private final OfflinePlayer player;
    private final DamageCause damageCause;
    private final Timestamp deathTime;
    private final String mobName;
    private final OfflinePlayer killer;

    public PlayerDeath(String playerUUID, String sDamageCause, Timestamp lDeathTime, String killerUUID, String mob) {
        player = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
        damageCause = DamageCause.valueOf(sDamageCause);
        deathTime = lDeathTime;
        killer = (killerUUID == null) ? null : Bukkit.getOfflinePlayer(UUID.fromString(killerUUID));
        mobName = mob;
    }

    /**
     * Returns the player who died.
     *
     * @return OfflinePlayer
     * The player who died.
     */
    public OfflinePlayer getPlayer() { return player; }

    /**
     * Return the cause of death.
     *
     * @return DamageCause
     *  The cause of death.
     */
    public DamageCause getDamageCause() { return damageCause; }

    /**
     * Returns the time when player died.
     *
     * @return Timestamp
     *  The time when player died.
     */
    public Timestamp getDeathTime() { return deathTime; }

    /**
     * Returns the player killer if any.
     *
     * @return OfflinePlayer.
     *  Killer of died player. Null if none.
     */
    public OfflinePlayer getKiller() { return killer; }

    /**
     * Returns mob that killed player if any.
     *
     * @return String
     *  Mob name. Null if none
     */
    public String getMobName() { return mobName; }

    /**
     * Gets list of deaths count for every player in database.
     *
     * @return List<PlayerDeathCount>
     *  List of players and their death count.
     */
    public static List<PlayerDeath> getPlayerDeaths(OfflinePlayer player, int offset, int limit) {
        DeathCounter.getDatabaseManager().openConnection();
        try {
            PreparedStatement playerDeaths = DeathCounter.getDatabaseManager().getConnection().prepareStatement("SELECT `ID`, `playerUUID`, `method`, `initiator`, `createdAt`, `mobName` FROM `deathcounter_deaths` WHERE playerUUID=? LIMIT " + offset + ", " + limit);
            playerDeaths.setString(1, player.getUniqueId().toString());
            ResultSet result = playerDeaths.executeQuery();
            List<PlayerDeath> data = new ArrayList<>();
            while(result.next()) {
                data.add(new PlayerDeath(result.getString("playerUUID"), result.getString("method"), result.getTimestamp("createdAt"), result.getString("initiator"), result.getString("mobName")));
            }
            DeathCounter.getDatabaseManager().closeConnection();
            return data;
        } catch(SQLException e) {
            Messages.sendMessage("&cError has occurred while getting players death count.");
            e.printStackTrace();
        }
        DeathCounter.getDatabaseManager().closeConnection();
        return null;
    }

    /**
     * Removes player death from database.
     * @param ID Death ID.
     * @return boolean
     *  True if success, False otherwise.
     */
    public static boolean removePlayerDeath(int ID) {
        DeathCounter.getDatabaseManager().openConnection();
        try {
            PreparedStatement removeDeath = DeathCounter.getDatabaseManager().getConnection().prepareStatement("DELETE FROM deathcounter_deaths WHERE ID=?");
            removeDeath.setInt(1, ID);
            int result = removeDeath.executeUpdate();
            return result > 0;
        } catch(SQLException e) {
            Messages.sendMessage("&cError has occurred while removing player death.");
            e.printStackTrace();
        }
        return false;
    }
}
