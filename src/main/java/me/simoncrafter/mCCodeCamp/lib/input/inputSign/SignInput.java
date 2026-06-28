package me.simoncrafter.mCCodeCamp.lib.input.inputSign;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.ClearCharAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.MessageAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.NoButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.YesButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.YesNoQuestion;
import me.simoncrafter.mCCodeCamp.lib.input.ActivationHandler.ButtonInput;
import me.simoncrafter.mCCodeCamp.lib.input.LocationalInput;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class SignInput extends LocationalInput implements Listener {

    private static final Map<String, SignInput> instances = new HashMap<>();
    private static final Map<String, SignCallback> callbacks = new HashMap<>();
    private static SignInput listener = null;
    private static JavaPlugin plugin = null;

    public SignInput(String key, Location location, Map<String, Object> args) {
        super(key, location, args);
    }

    public static void initialize(JavaPlugin p, YamlConfiguration config) {
        if (config == null) {
            instances.clear();
            callbacks.clear();
            return;
        }

        plugin = p;
        loadFromConfig(config);

        if (listener == null) {
            listener = new SignInput("", null, new HashMap<>());
            Bukkit.getPluginManager().registerEvents(listener, p);
        }
    }

    public static void loadFromConfig(YamlConfiguration config) {
        instances.clear();

        if (config == null) {
            return;
        }

        List<Map<?, ?>> list = config.getMapList("inputs.signs");
        if (list == null || list.isEmpty()) {
            return;
        }

        for (Map<?, ?> entry : list) {
            String key = (String) entry.get("key");
            String world = (String) entry.get("world");
            Object xObj = entry.get("x");
            Object yObj = entry.get("y");
            Object zObj = entry.get("z");

            if (key == null || world == null || xObj == null || yObj == null || zObj == null) {
                continue;
            }

            double x = ((Number) xObj).doubleValue();
            double y = ((Number) yObj).doubleValue();
            double z = ((Number) zObj).doubleValue();
            Object argsObj = entry.get("args");
            Map<String, Object> args = argsObj instanceof Map ? (Map<String, Object>) argsObj : new HashMap<>();

            Location loc = new Location(Bukkit.getWorld(world), x, y, z);
            SignInput input = new SignInput(key, loc, args);
            instances.put(key, input);
        }
    }

    public static void registerInput(String key, SignCallback callback) {
        callbacks.put(key, callback);
    }

    public static void unregisterInput(String key) {
        callbacks.remove(key);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Sign sign = (Sign) event.getBlock().getState();
        Player player = event.getPlayer();

        for (SignInput input : instances.values()) {
            if (blockLocationsMatch(sign.getLocation(), input.getLocation())) {
                String[] frontStrings = new String[4];

                for (int i = 0; i < 4; i++) {
                    frontStrings[i] = event.getLine(i);
                }

                if (callbacks.containsKey(input.getKey())) {
                    callbacks.get(input.getKey()).onEdit(player, frontStrings, sign);
                }
                return;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();



        for (SignInput input : instances.values()) {
            if (blockLocationsMatch(block.getLocation(), input.getLocation())) {
                event.setCancelled(true);

                if (player.getGameMode() != GameMode.CREATIVE) {
                    return;
                }

                YesNoQuestion question = new YesNoQuestion();
                question.question(Component.text("This block is registered as an input. Break it to remove registration?", NamedTextColor.YELLOW))
                        .yesButton(YesButton.create().addAction(CustomAction.create().action(p -> {
                            block.setType(Material.AIR);
                            SignInput.unregisterInput(input.getKey());
                        })).addAction(MessageAction.create(Component.text("Sign Removed Successfully!", NamedTextColor.GREEN))))
                        .noButton(NoButton.create().addAction(MessageAction.create(Component.text("Cancelled Removal")))).clearChat(false);
                question.show(player);
                return;
            }
        }
    }

    private static boolean blockLocationsMatch(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockY() == loc2.getBlockY()
                && loc1.getBlockZ() == loc2.getBlockZ()
                && Objects.equals(loc1.getWorld(), loc2.getWorld());
    }
}
