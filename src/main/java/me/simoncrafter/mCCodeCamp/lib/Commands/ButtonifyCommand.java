package me.simoncrafter.mCCodeCamp.lib.Commands;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.ClearCharAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.StringInputAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.StringWithRulesInputAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.MessageAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.NoButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.YesButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.YesNoQuestion;
import me.simoncrafter.mCCodeCamp.lib.ActivationHandler.ActivationEvents;
import me.simoncrafter.mCCodeCamp.lib.PersistantDataTags;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ButtonifyCommand implements CommandExecutor, TabExecutor {

    private Map<Player, String> inputtedIDs = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        //enshure that the executor is a player
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You need to be a player to execute this");
            return false;
        }
        Player player = (Player) commandSender; // Cast the commandsender to the player (Works because the Player is a command sender and thus can be casted to player)
        return addNewButton(player);
    }

    private boolean addNewButton(Player player) {

        CustomAction regiserAction = CustomAction.create().action(p -> {
            if (!inputtedIDs.containsKey(p)) {
                return;
            }
            if (inputtedIDs.get(p).isEmpty()) {
                return;
            }

            // All blocks that are transparrent to the view Raycast
            Set<Material> ignoreBlocks = new HashSet<>();
            ignoreBlocks.add(Material.WATER);
            ignoreBlocks.add(Material.LAVA);
            ignoreBlocks.add(Material.AIR);

            //Spawn marker entity to remember the positions of buttons without having to add development overhead by saving it in config files. AKA I'm lazy
            Location targetBlockLocation = p.getTargetBlock(ignoreBlocks, 5).getLocation();

            // check if block is already registered as button
            if (ActivationEvents.getNearbyButtonEntityMarkerEntities(targetBlockLocation) != null) {
                player.sendMessage(Component.text("This block is already registered as a button! Break it to remove the button registration", NamedTextColor.RED));
                return;
            }

            Marker marker = targetBlockLocation.getWorld().spawn(targetBlockLocation, Marker.class);
            marker.getPersistentDataContainer().set(PersistantDataTags.activationButton, PersistentDataType.STRING, inputtedIDs.get(p)); // adding data tag with ID
            player.sendMessage("Summend new");
        });

        YesNoQuestion confirmPrompt = new YesNoQuestion();
        confirmPrompt.question(Component.text("Do you want to add a block add a interactable button? You will be able to run some code when a player clicks it!"))
                .yesButton(YesButton.create()
                        .addAction(StringWithRulesInputAction.create()
                                .prompt(Component.text("Look at the block you want to be clickable and §6enter a ID in chat§r for the button (You will need to input this in your code, so you can run code if it gets pressed)")
                                        .appendNewline()
                                        .append(Component.text("Enter ID in chat:", NamedTextColor.BLUE)))
                                .onResponse(p -> s1 -> {
                                    inputtedIDs.put(p, s1);
                                })
                                .regexRule("[a-zA-Z0-9]+") // only allow alpanumerical and the standart alphabet wihtout special characters
                                .reTry(true)
                                .reTryMessage(Component.text("You mustn't put any special characters in the ID. Allowed: a-z A-Z 0-9", NamedTextColor.RED))
                                .addSuccessAction(regiserAction)
                        ))
                .noButton(NoButton.create().addAction(ClearCharAction.create()).addAction(MessageAction.create(Component.text("Cancelled"))));
        confirmPrompt.show(player);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
