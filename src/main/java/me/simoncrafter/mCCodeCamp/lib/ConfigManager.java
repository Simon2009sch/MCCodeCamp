package me.simoncrafter.mCCodeCamp.lib;

import me.simoncrafter.mCCodeCamp.MCCodeCamp;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private static final String CONFIG_FILEPATH = "internal.yaml";
    private static YamlConfiguration config = new YamlConfiguration();
    private static File configFile;

    private static final String autoReloadConfigString = "autoreload";
    private static boolean autoReload = false;

    public static void load() {
        configFile = new File(MCCodeCamp.getInstance().getDataFolder(), CONFIG_FILEPATH);
        try {
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            }
            config.load(configFile);
        } catch (Exception e) {
            Logs.error("Config file loading has Malfunctioned");
        }

        autoReload = config.getBoolean(autoReloadConfigString, false);
    }

    public static boolean isAutoReload() {
        return autoReload;
    }

    public static void setAutoReload(boolean value) {
        autoReload = value;
        config.set(autoReloadConfigString, value);
        save();
    }

    private static void save() {
        try {
            config.save(configFile);
        } catch (Exception e) {
            Logs.error("Config file saving has Malfunctioned");
        }
    }
}