package me.k4m1s.deathcounter.Database.Models;

import me.k4m1s.deathcounter.Chat.Messages;
import me.k4m1s.deathcounter.DeathCounter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDeathCount {
    private final OfflinePlayer player;
    private final int iCount;

    public PlayerDeathCount(String playerUUID, int count) {
        player = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
        iCount = count;
    }

    /**
     * Returns the player who died.
     *
     * @return OfflinePlayer
     * The player who died.
     */
    public OfflinePlayer getPlayer() { return player; }

    /**
     * Return count of player death.
     *
     * @return int
     *  Player death count.
     */
    public int getCount() { return iCount; }

    /**
     * Gets list of deaths count for every player in database.
     *
     * @return List<PlayerDeathCount>
     *  List of players and their death count.
     */
    public static List<PlayerDeathCount> getPlayersDeathCount(int offset, int limit) {
        DeathCounter.getDatabaseManager().openConnection();
        try {
            PreparedStatement playersDeathCount = DeathCounter.getDatabaseManager().getConnection().prepareStatement("SELECT playerUUID, count(*) count FROM deathcounter_deaths GROUP BY playerUUID ORDER BY count(*) DESC LIMIT " + offset + ", " + limit);
            ResultSet result = playersDeathCount.executeQuery();
            List<PlayerDeathCount> data = new ArrayList<>();
            while(result.next()) {
                data.add(new PlayerDeathCount(result.getString("playerUUID"), result.getInt("count")));
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
     * Gets death count for given player.
     *
     * @param player Player of which the death count should be returned.
     * @return PlayerDeathCount
     *  Player death count.
     */
    public static PlayerDeathCount getPlayerDeathCount(OfflinePlayer player) {
        DeathCounter.getDatabaseManager().openConnection();
        try {
            PreparedStatement playerDeathCount = DeathCounter.getDatabaseManager().getConnection().prepareStatement("SELECT playerUUID, count(*) count FROM deathcounter_deaths WHERE playerUUID=? GROUP BY playerUUID ORDER BY count(*)");
            playerDeathCount.setString(1, player.getUniqueId().toString());
            ResultSet result = playerDeathCount.executeQuery();
            if (result.next()) {
                PlayerDeathCount pdc = new PlayerDeathCount(result.getString("playerUUID"), result.getInt("count"));
                DeathCounter.getDatabaseManager().closeConnection();
                return pdc;
            }
        } catch(SQLException e) {
            Messages.sendMessage("&cError has occurred while getting players death count.");
            e.printStackTrace();
        }
        DeathCounter.getDatabaseManager().closeConnection();
        return null;
    }
}
