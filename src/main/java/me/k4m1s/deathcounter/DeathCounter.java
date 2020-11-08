package me.k4m1s.deathcounter;

import me.k4m1s.deathcounter.Chat.Messages;
import me.k4m1s.deathcounter.Commands.DeathCounterCommand;
import me.k4m1s.deathcounter.Commands.DeathCounterTabCompleter;
import me.k4m1s.deathcounter.Config.ConfigManager;
import me.k4m1s.deathcounter.Database.DatabaseHelper;
import me.k4m1s.deathcounter.Database.DatabaseManager;
import me.k4m1s.deathcounter.Database.Models.PlayerDeathCount;
import me.k4m1s.deathcounter.Listeners.*;
import me.k4m1s.deathcounter.Sidebar.SidebarManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class DeathCounter extends JavaPlugin {

    private static DeathCounter instance;
    private static DatabaseManager databaseManager;
    private static SidebarManager sidebarManager;
    private static HashMap<UUID, Boolean> deathPlayers;

    FileConfiguration config;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        config = getConfig();

        if (!ConfigManager.validateConfig(config)) {
            Messages.sendMessage("&cPlugin configuration is invalid!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        databaseManager = new DatabaseManager(config.getString("database.host"), config.getInt("database.port"), config.getString("database.database"), config.getString("database.username"), config.getString("database.password"));
        databaseManager.prepareDatabase();
        deathPlayers = new HashMap<>();
        DatabaseHelper.getPlayersDeathCount(0, 5, playerDeathCount -> {
            if (playerDeathCount == null) return;
            for (PlayerDeathCount deathCount : playerDeathCount) {
                System.out.println(deathCount.getPlayer().getName() + ": " + deathCount.getCount());
            }
        });
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);

        PluginCommand deathcounter = this.getCommand("deathcounter");
        if (deathcounter != null) {
            deathcounter.setExecutor(new DeathCounterCommand());
            deathcounter.setTabCompleter(new DeathCounterTabCompleter());
        }

        if (DeathCounter.getPlugin().getConfig().getBoolean("displaySidebar")) {
            sidebarManager = new SidebarManager();
            getServer().getScheduler().runTaskTimer(this, () -> sidebarManager.update(), 0, 20);
        }

        int pluginId = 9342;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        databaseManager.closeConnection();
    }

    public static DeathCounter getPlugin() { return instance; }

    public static DatabaseManager getDatabaseManager() { return databaseManager; }

    public static SidebarManager getSidebarManager() { return sidebarManager; }

    public static HashMap<UUID, Boolean> getDeathPlayers() {
        return deathPlayers;
    }
}
