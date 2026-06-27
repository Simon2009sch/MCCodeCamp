package me.simoncrafter.mCCodeCamp.courseBackEnd.basics.example1;

import me.simoncrafter.mCCodeCamp.YOURCODE.basics.example1.Example1;
import me.simoncrafter.mCCodeCamp.lib.ICourseExample;
import me.simoncrafter.mCCodeCamp.lib.Opsticles.SimpleDoor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;

public class BackendExample1 implements ICourseExample {

    Example1 workingArea;
    SimpleDoor door;

    public BackendExample1() {
    }

    @Override
    public void onLoad() {
        door = new SimpleDoor(Bukkit.getWorld("world"), new Vector(230, 82, 58), new Vector(236, 87, 58), Material.DIAMOND_BLOCK);
        workingArea = new Example1(door);
    }

    @Override
    public void command(String cmd) {

    }

    @Override
    public String getName() {
        return "Example1";
    }
}
