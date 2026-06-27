package me.simoncrafter.mCCodeCamp.lib.Opsticles;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.util.Vector;

public abstract class ASqareDoor extends ADoor {

    protected Vector pos1;
    protected Vector pos2;

    public ASqareDoor(World world, Vector pos1, Vector pos2) {
        super(world);
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public Vector getPos1() {
        return pos1;
    }

    public void setPos1(Vector pos1) {
        this.pos1 = pos1;
    }

    public Vector getPos2() {
        return pos2;
    }

    public void setPos2(Vector pos2) {
        this.pos2 = pos2;
    }

    protected Vector getMinimumCoordinatePoint(Vector pos1, Vector pos2) {
        return new Vector(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
    }

    protected Vector getMaximumCoordinatePoint(Vector pos1, Vector pos2) {
        return new Vector(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
    }
}
