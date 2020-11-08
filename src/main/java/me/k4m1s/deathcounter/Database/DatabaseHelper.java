package me.k4m1s.deathcounter.Database;

import me.k4m1s.deathcounter.Chat.Messages;
import me.k4m1s.deathcounter.Database.Interfaces.*;
import me.k4m1s.deathcounter.Database.Models.PlayerDeath;
import me.k4m1s.deathcounter.Database.Models.PlayerDeathCount;
import me.k4m1s.deathcounter.DeathCounter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseHelper {
    public static void getPlayersDeathCount(final int offset, final int page, final IPlayersDeathCount callback) {
        Bukkit.getScheduler().runTaskAsynchronously(DeathCounter.getPlugin(), () -> {
            final List<PlayerDeathCount> deathCount = PlayerDeathCount.getPlayersDeathCount(offset, page);
            Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> {
                if (deathCount == null) callback.onQueryDone(new PlayerDeathCount[0]);
                else {
                    PlayerDeathCount[] data = new PlayerDeathCount[deathCount.size()];
                    data = deathCount.toArray(data);
                    callback.onQueryDone(data);
                }
            });
        });
    }

    public static void getPlayerDeathCount(final OfflinePlayer player, final IPlayerDeathCount callback) {
        Bukkit.getScheduler().runTaskAsynchronously(DeathCounter.getPlugin(), () -> {
            PlayerDeathCount deathCount = PlayerDeathCount.getPlayerDeathCount(player);
            Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> callback.onQueryDone(deathCount));
        });
    }

    public static void getPlayerDeaths(final OfflinePlayer player, final IPlayerDeaths callback) {
        Bukkit.getScheduler().runTaskAsynchronously(DeathCounter.getPlugin(), () -> {
            List<PlayerDeath> deaths = PlayerDeath.getPlayerDeaths(player, 0, 5);
            Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> {
                if (deaths == null) callback.onQueryDone(new PlayerDeath[0]);
                else {
                    PlayerDeath[] data = new PlayerDeath[deaths.size()];
                    data = deaths.toArray(data);
                    callback.onQueryDone(data);
                }
            });
        });
    }

    public static void getPlayerDeaths(final OfflinePlayer player, final int offset, final int limit, final IPlayerDeaths callback) {
        Bukkit.getScheduler().runTaskAsynchronously(DeathCounter.getPlugin(), () -> {
            List<PlayerDeath> deaths = PlayerDeath.getPlayerDeaths(player, offset, limit);
            Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> {
                if (deaths == null) callback.onQueryDone(new PlayerDeath[0]);
                else {
                    PlayerDeath[] data = new PlayerDeath[deaths.size()];
                    data = deaths.toArray(data);
                    callback.onQueryDone(data);
                }
            });
        });
    }

    public static void updatePlayerNick(final Player player, final IDatabaseBool callback) {
        Bukkit.getScheduler().runTaskAsynchronously(DeathCounter.getPlugin(), () -> {
            try {
                DeathCounter.getDatabaseManager().openConnection();
                PreparedStatement nameUpdate = DeathCounter.getDatabaseManager().getConnection().prepareStatement("UPDATE deathcounter_players SET playerName=? WHERE UUID=?");
                nameUpdate.setString(1, player.getName());
                nameUpdate.setString(2, player.getUniqueId().toString());
                final int result = nameUpdate.executeUpdate();
                Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> callback.onQueryDone(result > 0));
            } catch(SQLException e) {
                Messages.sendMessage("&cError has occurred while updating player name.");
                e.printStackTrace();
            }
        });
    }

    public static void updatePlayerNick(final OfflinePlayer player, final IDatabaseBool callback) {
        Bukkit.getScheduler().runTaskAsynchronously(DeathCounter.getPlugin(), () -> {
            try {
                DeathCounter.getDatabaseManager().openConnection();
                PreparedStatement nameUpdate = DeathCounter.getDatabaseManager().getConnection().prepareStatement("UPDATE deathcounter_players SET playerName=? WHERE UUID=?");
                nameUpdate.setString(1, player.getName());
                nameUpdate.setString(2, player.getUniqueId().toString());
                final int result = nameUpdate.executeUpdate();
                Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> callback.onQueryDone(result > 0));
            } catch(SQLException e) {
                Messages.sendMessage("&cError has occurred while updating player name.");
                e.printStackTrace();
            }
        });
    }

    public static void addPlayerToDatabase(final Player player, final IDatabaseBool callback) {
        Bukkit.getScheduler().runTaskAsynchronously(DeathCounter.getPlugin(), () -> {
            try {
                PreparedStatement nameUpdate = DeathCounter.getDatabaseManager().getConnection().prepareStatement("INSERT INTO `deathcounter_players`(`UUID`, `playerName`) VALUES (?,?)");
                nameUpdate.setString(1, player.getUniqueId().toString());
                nameUpdate.setString(2, player.getName());
                final int result = nameUpdate.executeUpdate();
                Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> callback.onQueryDone(result > 0));
            } catch(SQLException e) {
                Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> callback.onQueryDone(false));
                Messages.sendMessage("&cError has occurred while updating player name.");
                e.printStackTrace();
            }
        });
    }

    public static void addPlayerToDatabase(final OfflinePlayer player, final IDatabaseBool callback) {
        Bukkit.getScheduler().runTaskAsynchronously(DeathCounter.getPlugin(), () -> {
            try {
                PreparedStatement nameUpdate = DeathCounter.getDatabaseManager().getConnection().prepareStatement("INSERT INTO `deathcounter_players`(`UUID`, `playerName`) VALUES (?,?)");
                nameUpdate.setString(1, player.getUniqueId().toString());
                nameUpdate.setString(2, player.getName());
                final int result = nameUpdate.executeUpdate();
                Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> callback.onQueryDone(result > 0));
            } catch(SQLException e) {
                Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> callback.onQueryDone(false));
                Messages.sendMessage("&cError has occurred while updating player name.");
                e.printStackTrace();
            }
        });
    }

    public static void addPlayerDeath(final String playerUUID, final String deathCause, final String killerUUID, final String mobName, IDatabaseBool callback) {
        Bukkit.getScheduler().runTaskAsynchronously(DeathCounter.getPlugin(), () -> {
            try {
                PreparedStatement deathInsert = DeathCounter.getDatabaseManager().getConnection().prepareStatement("INSERT INTO `deathcounter_deaths`(`playerUUID`, `method`, `initiator`, `mobName`) VALUES (?, ?, ?, ?)");
                deathInsert.setString(1, playerUUID);
                deathInsert.setString(2, deathCause);
                if (killerUUID!= null && killerUUID.length() > 0) {
                    deathInsert.setString(3, killerUUID);
                } else {
                    deathInsert.setNull(3, java.sql.Types.NULL);
                }

                if (mobName!= null && mobName.length() > 0) {
                    deathInsert.setString(4, mobName);
                } else {
                    deathInsert.setNull(4, java.sql.Types.NULL);
                }
                final int result = deathInsert.executeUpdate();
                Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> callback.onQueryDone(result > 0));
            } catch(SQLException e) {
                Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> callback.onQueryDone(false));
                Messages.sendMessage("&cError has occurred while updating player name.");
                e.printStackTrace();
            }
        });
    }

    public static void removePlayerDeath(final int ID, IDatabaseBool callback) {
        Bukkit.getScheduler().runTaskAsynchronously(DeathCounter.getPlugin(), () -> {
            final boolean result = PlayerDeath.removePlayerDeath(ID);
            Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> callback.onQueryDone(result));
        });
    }

    public static void getPlayersCount(IDatabaseInt callback) {
        Bukkit.getScheduler().runTaskAsynchronously(DeathCounter.getPlugin(), () -> {
            try {
                PreparedStatement playersCount = DeathCounter.getDatabaseManager().getConnection().prepareStatement("SELECT count(*) as count FROM deathcounter_players GROUP BY UUID");
                ResultSet result = playersCount.executeQuery();
                result.next();
                final int resultCount = result.getInt("count");
                Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> callback.onQueryDone(resultCount));
            } catch(SQLException e) {
                Bukkit.getScheduler().runTask(DeathCounter.getPlugin(), () -> {
                    Messages.sendMessage("&cError has occurred while getting players count.");
                    e.printStackTrace();
                    callback.onQueryDone(-1);
                });
            }
        });
    }
}
