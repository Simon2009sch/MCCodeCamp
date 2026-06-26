package me.simoncrafter.mCCodeCamp.lib.ActivationHandler;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface IButtonActivated {

    default void buttonPressed(Player player, Block button) {

    }

}
