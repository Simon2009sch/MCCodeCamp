package me.simoncrafter.mCCodeCamp.lib;

import me.simoncrafter.mCCodeCamp.MCCodeCamp;
import me.simoncrafter.mCCodeCamp.lib.input.ActivationHandler.ButtonInput;
import me.simoncrafter.mCCodeCamp.lib.input.inputSign.SignInput;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static final String CONFIG_FILEPATH = "internal.yaml";
    private static YamlConfiguration config = new YamlConfiguration();
    private static File configFile;

    private static final String autoReloadConfigString = "autoreload";
    private static boolean autoReload = false;
    private static final String unlockedModeConfigString = "unlockedMode";
    private static boolean unlockedMode = false;

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
        unlockedMode = config.getBoolean(unlockedModeConfigString, false);

        ButtonInput.initialize(MCCodeCamp.getInstance(), config);
        SignInput.initialize(MCCodeCamp.getInstance(), config);
    }

    public static boolean isAutoReload() {
        return autoReload;
    }

    public static void setAutoReload(boolean value) {
        autoReload = value;
        config.set(autoReloadConfigString, value);
        save();
    }

    public static boolean isUnlockedMode() {
        return unlockedMode;
    }

    public static void setUnlockedMode(boolean unlockedMode) {
        ConfigManager.unlockedMode = unlockedMode;
    }

    public record InputEntry(String key, String world, double x, double y, double z, Map<String, Object> args) {}

    public static void addButtonInput(InputEntry entry) {
        Map<String, Object> entryMap = new HashMap<>();
        entryMap.put("key", entry.key);
        entryMap.put("world", entry.world);
        entryMap.put("x", entry.x);
        entryMap.put("y", entry.y);
        entryMap.put("z", entry.z);
        entryMap.put("args", entry.args);

        java.util.List<Map<?, ?>> list = config.getMapList("inputs.buttons");
        list.add(entryMap);
        config.set("inputs.buttons", list);
        save();
        ButtonInput.loadFromConfig(config);
    }

    public static void removeButtonInput(String key) {
        java.util.List<Map<?, ?>> list = config.getMapList("inputs.buttons");
        if (list == null || list.isEmpty()) return;

        list.removeIf(item -> key.equals(item.get("key")));
        config.set("inputs.buttons", list);
        save();
        ButtonInput.loadFromConfig(config);
    }

    public static void addSignInput(InputEntry entry) {
        Map<String, Object> entryMap = new HashMap<>();
        entryMap.put("key", entry.key);
        entryMap.put("world", entry.world);
        entryMap.put("x", entry.x);
        entryMap.put("y", entry.y);
        entryMap.put("z", entry.z);
        entryMap.put("args", entry.args);

        java.util.List<Map<?, ?>> list = config.getMapList("inputs.signs");
        list.add(entryMap);
        config.set("inputs.signs", list);
        save();
        SignInput.loadFromConfig(config);
    }

    public static void removeSignInput(String key) {
        java.util.List<Map<?, ?>> list = config.getMapList("inputs.signs");
        if (list == null || list.isEmpty()) return;

        list.removeIf(item -> key.equals(item.get("key")));
        config.set("inputs.signs", list);
        save();
        SignInput.loadFromConfig(config);
    }

    private static void save() {
        try {
            config.save(configFile);
        } catch (Exception e) {
            Logs.error("Config file saving has Malfunctioned");
        }
    }
}