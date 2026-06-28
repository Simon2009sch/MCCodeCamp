package me.simoncrafter.mCCodeCamp.lib.input.ActivationHandler;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface ButtonCallback {
    void onPress(Player player, Block block);
}
