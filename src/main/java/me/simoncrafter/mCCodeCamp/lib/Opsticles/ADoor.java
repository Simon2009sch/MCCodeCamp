package me.simoncrafter.mCCodeCamp.lib.Opsticles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;

public abstract class ADoor {

    protected int doorState = 0;

    protected World world;
    protected Sound openSound;
    protected Sound closeSound;

    public ADoor(World world) {
        this.world = world;
    }

    public void open() {
        doorState = 2;
    }

    public void close() {
        doorState = 0;
    }

    public Sound getOpenSound() {
        return openSound;
    }

    public void setOpenSound(Sound openSound) {
        this.openSound = openSound;
    }

    public Sound getCloseSound() {
        return closeSound;
    }

    public void setCloseSound(Sound closeSound) {
        this.closeSound = closeSound;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public boolean isOpen() {
        return doorState==2;
    }
}
