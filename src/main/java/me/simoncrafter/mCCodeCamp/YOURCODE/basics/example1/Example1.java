package me.simoncrafter.mCCodeCamp.YOURCODE.basics.example1;

import me.simoncrafter.mCCodeCamp.lib.Chat;
import me.simoncrafter.mCCodeCamp.lib.input.ActivationHandler.ButtonInput;
import me.simoncrafter.mCCodeCamp.lib.Opsticles.SimpleDoor;
import me.simoncrafter.mCCodeCamp.lib.input.inputSign.SignInput;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Example1 {

    private SimpleDoor door; // this is the door you see before you

    // These lines are necessary. Though it is out of the scope of the basics part.
    // So I wouldn't touch it
    public Example1(SimpleDoor door) {
        ButtonInput.registerInput("myFirstButton", this::buttonPressed);
        ButtonInput.registerInput("mySecondButton", this::secondButtonPressed);
        ButtonInput.registerInput("myThirdButton", this::thirdButtonPressed);
        ButtonInput.registerInput("myFourthButton", this::fourthButtonPressed);
        ButtonInput.registerInput("explosionButton", this::explosionButton);
        SignInput.registerInput("mySign", this::onSignEdit);
        this.door = door;
    }

    private void buttonPressed(Player player, Block button) {
        if (door.isOpen()) {
            door.close();
            //Chat.message("Closing Door", player);
        } else {
            door.open();
            //Chat.message("Opening Door", player);
        }
    }

    private void secondButtonPressed(Player player, Block button) {
        Chat.message("Second button", player);
    }

    private void thirdButtonPressed(Player player, Block button) {
        Chat.message("The Third button was pressed", player);
    }

    private void fourthButtonPressed(Player player, Block button) {
        Chat.message("And finally the fourth button was pressed", player);
    }
    private void onSignEdit(Player player, String[] linesChanged, Sign sign) {
        Chat.message("You have edited the sign!", player);
        for (String s : linesChanged) {
            Chat.message(s, player);
        }
    }

    private void explosionButton(Player player, Block button) {
        button.getWorld().createExplosion(button.getLocation(), 2);
    }
}
