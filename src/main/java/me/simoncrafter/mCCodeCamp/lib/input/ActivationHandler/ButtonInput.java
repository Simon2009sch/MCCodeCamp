package me.simoncrafter.mCCodeCamp.lib.input.ActivationHandler;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.ClearCharAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.MessageAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.NoButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.YesButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.YesNoQuestion;
import me.simoncrafter.mCCodeCamp.lib.ConfigManager;
import me.simoncrafter.mCCodeCamp.lib.input.LocationalInput;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ButtonInput extends LocationalInput implements Listener {

    private static final Map<String, ButtonInput> instances = new HashMap<>();
    private static final Map<String, ButtonCallback> callbacks = new HashMap<>();
    private static ButtonInput listener = null;
    private static JavaPlugin plugin = null;

    public ButtonInput(String key, Location location, Map<String, Object> args) {
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
            listener = new ButtonInput("", null, new HashMap<>());
            Bukkit.getPluginManager().registerEvents(listener, p);
        }
    }

    public static void loadFromConfig(YamlConfiguration config) {
        instances.clear();

        if (config == null) {
            return;
        }

        List<Map<?, ?>> list = config.getMapList("inputs.buttons");
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
            ButtonInput input = new ButtonInput(key, loc, args);
            instances.put(key, input);
        }
    }

    public static void registerInput(String key, ButtonCallback callback) {
        callbacks.put(key, callback);
    }

    public static void unregisterInput(String key) {
        callbacks.remove(key);
        ConfigManager.removeButtonInput(key);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        Block clickedBlock = event.getClickedBlock();
        Player player = event.getPlayer();

        for (ButtonInput input : instances.values()) {
            if (blockLocationsMatch(clickedBlock.getLocation(), input.getLocation()) && event.getHand() == EquipmentSlot.HAND && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (Tag.BUTTONS.isTagged(clickedBlock.getType())) {
                    if (((Powerable) clickedBlock.getBlockData()).isPowered()) {
                        return;
                    }
                    event.setCancelled(false);
                } else {
                    event.setCancelled(true);
                }


                if (callbacks.containsKey(input.getKey())) {
                    callbacks.get(input.getKey()).onPress(player, clickedBlock);
                }
                return;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();



        for (ButtonInput input : instances.values()) {
            if (blockLocationsMatch(block.getLocation(), input.getLocation())) {
                event.setCancelled(true);

                if (player.getGameMode() != GameMode.CREATIVE) {
                    return;
                }

                YesNoQuestion question = new YesNoQuestion();
                question.question(Component.text("This block is registered as an input. Break it to remove registration?", NamedTextColor.YELLOW))
                        .yesButton(YesButton.create().addAction(CustomAction.create().action(p -> {
                            block.setType(Material.AIR);
                            ButtonInput.unregisterInput(input.getKey());
                        })).addAction(MessageAction.create(Component.text("Button Removed Successfully!", NamedTextColor.GREEN))))
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
