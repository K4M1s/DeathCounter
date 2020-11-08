package me.k4m1s.deathcounter.Config;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    /**
     * Verifies if all needed data is present in config file.
     * @param config Config variable
     * @return boolean
     *  True if all data is present, False otherwise.
     */
    public static boolean validateConfig(FileConfiguration config) {
        if(config.getString("database.host") == null) return false;
        if(config.getInt("database.port") == 0) return false;
        if(config.getString("database.username") == null) return false;
        if(config.getString("database.password") == null) return false;
        return config.getString("database.database") != null;
    }
}
