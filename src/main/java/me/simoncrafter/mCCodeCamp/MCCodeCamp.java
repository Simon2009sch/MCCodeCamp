package me.simoncrafter.mCCodeCamp;

import me.simoncrafter.CraftersChatDialogs.InstanceData;
import me.simoncrafter.mCCodeCamp.lib.*;
import me.simoncrafter.mCCodeCamp.lib.Commands.CreateInputCommand;
import me.simoncrafter.mCCodeCamp.lib.Commands.ReloadInterfaceCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.InetSocketAddress;

public final class MCCodeCamp extends JavaPlugin {


    // this is a git modification test

    private static JavaPlugin instance;
    private static boolean reloadOnNextTick = false;
    private com.sun.net.httpserver.HttpServer reloadServer;

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.load();
        registerListeners();
        Bukkit.getPluginCommand("createInput").setExecutor(new CreateInputCommand());
        Bukkit.getPluginCommand("reloadMCCodeCampInterface").setExecutor(new ReloadInterfaceCommand());
        InstanceData.register(this);
        CourseLoader.loadExamples();
        tickLoop();
        startReloadEndpoint();

        Chat.broadcast(Component.text("Your code was successfully loaded!", NamedTextColor.GREEN, TextDecoration.BOLD));
    }

    private void tickLoop() {
        new BukkitRunnable(){
            @Override
            public void run() {
                if (reloadOnNextTick) {
                    reloadOnNextTick = false;
                    ReloadInterfaceCommand.reloadFunction();
                }
            }
        }.runTaskTimer(this, 0, 1);
    }

    private void startReloadEndpoint() {
        try {
            reloadServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(8765), 0);
            reloadServer.createContext("/reload", exchange -> {
                reloadOnNextTick = true;
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
            });
            reloadServer.start();
            Logs.info("Started reload endpoint for quick reloading");
        } catch (Exception e) {
            Logs.warn("There was an error while trying to open the reload endpoint\n" + e);
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
    }

    @Override
    public void onDisable() {
        if (reloadServer != null) {
            reloadServer.stop(0);
            reloadServer = null;
        }
    }

    public static JavaPlugin getInstance() {
        return instance;
    }



}
