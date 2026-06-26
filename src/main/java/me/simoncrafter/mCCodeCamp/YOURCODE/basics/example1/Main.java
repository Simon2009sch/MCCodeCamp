package me.simoncrafter.mCCodeCamp.YOURCODE.basics.example1;

import me.simoncrafter.mCCodeCamp.lib.ActivationHandler.IButtonActivated;
import me.simoncrafter.mCCodeCamp.lib.Chat;
import me.simoncrafter.mCCodeCamp.lib.Logs;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Main implements IButtonActivated {

    // This line is necessary. Though it is out of the scope of the basics part.
    // So I wouldn't touch it
    public Main() {}



    @Override // this override is also out of scope of the basics
    public void buttonPressed(Player player, Block button) {
        Chat.message("You just pressed the button that was hooked up to the code");
        Logs.info("The button was pressed!"); // equivalent to System.out.println()
    }
}
