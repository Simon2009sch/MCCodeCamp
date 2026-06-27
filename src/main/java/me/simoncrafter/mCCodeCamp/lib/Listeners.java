package me.simoncrafter.mCCodeCamp.lib;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listeners implements org.bukkit.event.Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (event.getPlayer().getAddress().getAddress().getHostAddress().equals("127.0.0.1")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + event.getPlayer().getName() + " parent set student");
        }

        if (!ConfigManager.isUnlockedMode()) {
            event.getPlayer().setOp(false);
        }
    }

}
