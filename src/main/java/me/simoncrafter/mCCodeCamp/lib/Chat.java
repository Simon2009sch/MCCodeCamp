package me.simoncrafter.mCCodeCamp.lib;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Chat {

    public static void broadcast(String message) {
        Bukkit.broadcast(Component.text(message));
    }
    public static void broadcast(Component message) {
        Bukkit.broadcast(message);
    }

    public static void message(String message, Player... players) {
        for (Player p : players) {
            p.sendMessage(Component.text(message));
        }
    }

    public static void message(Component message, Player... players) {
        for (Player p : players) {
            p.sendMessage(message);
        }
    }

}
