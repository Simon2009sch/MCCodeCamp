package me.simoncrafter.mCCodeCamp;

import me.simoncrafter.mCCodeCamp.lib.ConfigManager;
import me.simoncrafter.mCCodeCamp.lib.Logs;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.util.logging.Logger;

public final class MCCodeCamp extends JavaPlugin {

    private static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.load();
    }

    @Override
    public void onDisable() {

    }

    public static JavaPlugin getInstance() {
        return instance;
    }
}
