package me.simoncrafter.mCCodeCamp.lib.input;

import org.bukkit.Location;

import java.util.Map;

public class LocationalInput extends AbstractInput {

    protected Location location;

    public LocationalInput(String key, Location location, Map<String, Object> args) {
        super(key, args);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
