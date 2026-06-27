package me.simoncrafter.mCCodeCamp.YOURCODE.basics.example1;

import me.simoncrafter.mCCodeCamp.lib.ActivationHandler.ActivationEvents;
import me.simoncrafter.mCCodeCamp.lib.ActivationHandler.IButtonActivated;
import me.simoncrafter.mCCodeCamp.lib.Chat;
import me.simoncrafter.mCCodeCamp.lib.Opsticles.SimpleDoor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Example1 implements IButtonActivated {

    private SimpleDoor door; // this is the door you see before you

    // These lines are necessary. Though it is out of the scope of the basics part.
    // So I wouldn't touch it
    public Example1(SimpleDoor door) {
        ActivationEvents.registerNewActivationButton("myFirstButton", this);
        this.door = door;
    }



    @Override // this override is also out of scope of the basics
    public void buttonPressed(Player player, Block button) {

        if (door.isOpen()) {
            door.close();
            //Chat.message("Closing Door", player);
        } else {
            door.open();
            //Chat.message("Opening Door", player);
        }
    }
}
