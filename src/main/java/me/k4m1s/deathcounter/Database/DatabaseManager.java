package me.k4m1s.deathcounter.Database;

import java.sql.*;

import me.k4m1s.deathcounter.Chat.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class DatabaseManager {

    private final String sHost;
    private final String sDatabase;
    private final String sUser;
    private final String sPassword;
    private final int iPort;

    private Connection connection;

    public DatabaseManager(String host, int port, String database, String user, String password) {
        this.sHost = host;
        this.iPort = port;
        this.sDatabase = database;
        this.sUser = user;
        this.sPassword = password;
    }

    /**
     * Opens connection to database.
     */
    public void openConnection() {
        try {
            if (connection != null && !connection.isClosed()) return;
            connection = DriverManager.getConnection("jdbc:mysql://" + this.sHost + ":" + this.iPort + "/" + this.sDatabase, this.sUser, this.sPassword);
        } catch(SQLException e) {
            Messages.sendMessage("&cMySQL connection failed!");
            e.printStackTrace();
        }
    }

    /**
     * Closes connection to database.
     */
    public void closeConnection() {
        try {
            if (connection == null || connection.isClosed()) return;
            connection.close();
        } catch(SQLException e) {
            Messages.sendMessage("&cMySQL close connection failed!");
            e.printStackTrace();
        }
    }

    /**
     * Returns MySQL database connection.
     * @return Connection
     *  MySQL database connection.
     */
    public Connection getConnection() { return connection; }

    /**
     * Creates required tables in MySQL database.
     */
    public void prepareDatabase() {
        try {
            this.openConnection();
            PreparedStatement createPlayersTable = connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS deathcounter_players(" +
                    "UUID varchar(37) NOT NULL," +
                    "playerName varchar(64) NOT NULL," +
                    "PRIMARY KEY(uuid)" +
                ") ENGINE=INNODB CHARACTER SET utf8mb4;"
            );
            createPlayersTable.execute();
            this.closeConnection();
        } catch (SQLException e) {
            Messages.sendMessage("&cError has occurred while creating deathcounter_players MySQL table!");
            e.printStackTrace();
        }

        try {
            this.openConnection();
            PreparedStatement createDeathsTable = connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS deathcounter_deaths(" +
                    "ID INT NOT NULL auto_increment," +
                    "playerUUID varchar(37) NOT NULL," +
                    "method varchar(64)," +
                    "initiator varchar(37) NULL," +
                    "mobName varchar(64) NULL," +
                    "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+
                    "PRIMARY KEY(ID)," +
                    "FOREIGN KEY(playerUUID) REFERENCES deathcounter_players(UUID) ON DELETE CASCADE," +
                    "FOREIGN KEY(initiator) REFERENCES deathcounter_players(UUID) ON DELETE SET NULL" +
                ") ENGINE=INNODB CHARACTER SET utf8mb4;"
            );
            createDeathsTable.execute();
            this.closeConnection();
        } catch (SQLException e) {
            Messages.sendMessage("&cError has occurred while creating deathcounter_deaths MySQL table!");
            e.printStackTrace();
        }

        for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            DatabaseHelper.updatePlayerNick(player, result -> {
                if (!result) {
                    DatabaseHelper.addPlayerToDatabase(player, result1 -> {
                        if (!result1) {
                            Messages.sendMessage("An error has occurred while adding player " + player.getName() + " to the database.");
                        }
                    });
                }
            });
        }
    }
}
