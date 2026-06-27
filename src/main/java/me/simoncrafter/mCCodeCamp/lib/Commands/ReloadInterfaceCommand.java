package me.simoncrafter.mCCodeCamp.lib.Commands;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.ClearCharAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.MessageAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.GenericQuestion;
import me.simoncrafter.mCCodeCamp.MCCodeCamp;
import me.simoncrafter.mCCodeCamp.lib.Chat;
import me.simoncrafter.mCCodeCamp.lib.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReloadInterfaceCommand implements CommandExecutor, TabExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        reloadFunction();
        return true;
    }

    public static void reloadFunction() {
        if (ConfigManager.isAutoReload()) {
            Bukkit.getScheduler().runTask(MCCodeCamp.getInstance(), ReloadInterfaceCommand::reloadPluginWithPlugman);
            return;
        }

        Bukkit.getScheduler().runTask(MCCodeCamp.getInstance(), () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                GenericQuestion question = GenericQuestion.create(
                        Component.text("You have rebuilt your code. Reload to apply changes?"));
                question.addButton(
                                Button.create()
                                        .text(Component.text("[Reload]", NamedTextColor.GREEN))
                                        .addAction(CustomAction.create(pl -> reloadPluginWithPlugman()))
                        )
                        .addButton(Button.create()
                                .text(Component.text("[Cancel]", NamedTextColor.RED))
                                .addAction(ClearCharAction.create())
                                .addAction(MessageAction.create(Component.text("Canceled", NamedTextColor.RED)))
                        )
                        .addButton(Button.create()
                                .text(Component.text("[Enable Auto Reload]"))
                                .addAction(CustomAction.create(pl -> {
                                    ConfigManager.setAutoReload(true);
                                    reloadPluginWithPlugman();
                                }))
                        );
                // show to player - replace with however CraftersChatDialogs sends questions
                question.show(p);
            }
        });
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }

    private static void reloadPluginWithPlugman() {
        Chat.broadcast(Component.text("Reloading your code...", NamedTextColor.GOLD));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman reload MCCodeCamp");
    }
}
