package me.simoncrafter.mCCodeCamp.lib.Commands;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.ClearCharAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.StringWithRulesInputAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.NoButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.YesButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.YesNoQuestion;
import me.simoncrafter.mCCodeCamp.lib.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CreateInputCommand implements CommandExecutor, TabExecutor {

    private Map<Player, String> inputType = new HashMap<>();
    private Map<Player, String> inputKey = new HashMap<>();
    private Map<Player, Location> inputLocation = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You need to be a player to execute this");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            showTypeSelectionDialog(player);
            return true;
        }

        String type = args[0].toLowerCase();
        if (!type.equals("button") && !type.equals("sign")) {
            player.sendMessage(Component.text("Input type must be 'button' or 'sign'", NamedTextColor.RED));
            return true;
        }

        inputType.put(player, type);

        if (args.length == 1) {
            askForKey(player);
            return true;
        }

        String key = args[1];
        inputKey.put(player, key);

        Location location = null;
        if (args.length >= 4) {
            try {
                double x = Double.parseDouble(args[2]);
                double y = Double.parseDouble(args[3]);
                double z = Double.parseDouble(args[4]);
                String worldName = args.length >= 6 ? args[5] : player.getWorld().getName();
                location = new Location(org.bukkit.Bukkit.getWorld(worldName), x, y, z);
                if (location.getWorld() == null) {
                    player.sendMessage(Component.text("World not found: " + worldName, NamedTextColor.RED));
                    cleanup(player);
                    return true;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(Component.text("Invalid coordinates", NamedTextColor.RED));
                cleanup(player);
                return true;
            }
        } else {
            Set<Material> ignoreBlocks = new HashSet<>();
            ignoreBlocks.add(Material.WATER);
            ignoreBlocks.add(Material.LAVA);
            ignoreBlocks.add(Material.AIR);

            location = player.getTargetBlock(ignoreBlocks, 5).getLocation();
        }

        inputLocation.put(player, location);
        confirmRegistration(player);
        return true;
    }

    private void showTypeSelectionDialog(Player player) {
        YesNoQuestion typeQuestion = new YesNoQuestion();
        typeQuestion.question(Component.text("What input type do you want to create?", NamedTextColor.GOLD))
                .yesButton(YesButton.create()
                        .text(Component.text("Button", NamedTextColor.GREEN))
                        .addAction(CustomAction.create(p -> {
                            inputType.put(p, "button");
                            askForKey(p);
                        })))
                .noButton(NoButton.create()
                        .text(Component.text("Sign", NamedTextColor.BLUE))
                        .addAction(CustomAction.create(p -> {
                            inputType.put(p, "sign");
                            askForKey(p);
                        })));

        typeQuestion.show(player);
    }

    private void askForKey(Player player) {
        String type = inputType.getOrDefault(player, "button");

        StringWithRulesInputAction keyInput = StringWithRulesInputAction.create()
                .prompt(Component.text("Enter a key/ID for this " + type + " input (a-z, A-Z, 0-9 only):", NamedTextColor.BLUE))
                .onResponse(p -> key -> {
                    inputKey.put(p, key);
                    askForLocation(p);
                })
                .regexRule("[a-zA-Z0-9]+")
                .reTry(true)
                .reTryMessage(Component.text("Invalid key. Allowed: a-z A-Z 0-9", NamedTextColor.RED));

        keyInput.run(player);
    }

    private void askForLocation(Player player) {
        String type = inputType.getOrDefault(player, "button");

        YesNoQuestion locationQuestion = new YesNoQuestion();
        locationQuestion.question(Component.text("Look at a block to use raycast, or provide coordinates?", NamedTextColor.GOLD))
                .yesButton(YesButton.create()
                        .text(Component.text("Raycast (look at block)", NamedTextColor.GREEN))
                        .addAction(CustomAction.create(p -> {
                            Set<Material> ignoreBlocks = new HashSet<>();
                            ignoreBlocks.add(Material.WATER);
                            ignoreBlocks.add(Material.LAVA);
                            ignoreBlocks.add(Material.AIR);

                            Location location = p.getTargetBlock(ignoreBlocks, 5).getLocation();
                            inputLocation.put(p, location);
                            confirmRegistration(p);
                        })))
                .noButton(NoButton.create()
                        .text(Component.text("Cancel", NamedTextColor.RED))
                        .addAction(ClearCharAction.create())
                        .addAction(CustomAction.create(p -> cleanup(p))));

        locationQuestion.show(player);
    }

    private void confirmRegistration(Player player) {
        String type = inputType.getOrDefault(player, "button");
        String key = inputKey.getOrDefault(player, "");
        Location location = inputLocation.getOrDefault(player, null);

        if (location == null || location.getWorld() == null) {
            player.sendMessage(Component.text("Error: Invalid location", NamedTextColor.RED));
            cleanup(player);
            return;
        }

        ConfigManager.InputEntry entry = new ConfigManager.InputEntry(
                key,
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                new HashMap<>()
        );

        if (type.equals("button")) {
            ConfigManager.addButtonInput(entry);
            player.sendMessage(Component.text("✓ Button input registered: " + key, NamedTextColor.GREEN));
        } else if (type.equals("sign")) {
            ConfigManager.addSignInput(entry);
            player.sendMessage(Component.text("✓ Sign input registered: " + key, NamedTextColor.GREEN));
        }

        cleanup(player);
    }

    private void cleanup(Player player) {
        inputType.remove(player);
        inputKey.remove(player);
        inputLocation.remove(player);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("button", "sign");
        }
        return List.of();
    }
}
