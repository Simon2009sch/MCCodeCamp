package me.simoncrafter.mCCodeCamp.lib;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private static final String CONFIG_FILEPATH = "internal.yaml";


    private static YamlConfiguration config = new YamlConfiguration();


    // loading files and reading with the config file utility provided by the Bukkit API
    public static void load() {
        File configFile = new File(CONFIG_FILEPATH);
        try {
            config.load(configFile);
        } catch (Exception e) {
            Logs.error("Config file loading has Malfunctioned");
        }
    }

}
