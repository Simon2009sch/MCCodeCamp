package me.simoncrafter.mCCodeCamp.lib.Opsticles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;

public abstract class ADoor {

    public void open() {

    }

    public void close() {

    }

    public Location getCenterLocation() {
        return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
    }

    public Location getPos1() {
        return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
    }
    public Location getPos2() {
        return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
    }
    void setOpenSound() {

    }
    public Sound getOpenSound() {
        return null;
    }


}
