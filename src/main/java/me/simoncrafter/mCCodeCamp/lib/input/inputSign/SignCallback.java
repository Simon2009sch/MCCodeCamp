package me.simoncrafter.mCCodeCamp.lib.input.inputSign;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface SignCallback {
    void onEdit(Player player, String[] linesChanged, Sign sign);
}
