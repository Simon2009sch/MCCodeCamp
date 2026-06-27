package me.simoncrafter.mCCodeCamp.lib.Opsticles;

import me.simoncrafter.mCCodeCamp.lib.Chat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class SimpleDoor extends ASqareDoor {

    private Material closedblock;
    private Material openBlock;

    public SimpleDoor(World world, Vector pos1, Vector pos2, Material closedblock, Material openBlock) {
        super(world, pos1, pos2);
        this.closedblock = closedblock;
        this.openBlock = openBlock;
    }
    public SimpleDoor(World world, Vector pos1, Vector pos2, Material closedblock) {
        super(world, pos1, pos2);
        this.closedblock = closedblock;
        this.openBlock = Material.AIR;
    }

    @Override
    public void open() {
        super.open();
        Vector min = getMinimumCoordinatePoint(pos1, pos2);
        Vector diff = getMaximumCoordinatePoint(pos1, pos2).subtract(min);
        loopTroughBlocks(diff, min, world, openBlock);

    }

    @Override
    public void close() {
        super.close();
        Vector min = getMinimumCoordinatePoint(pos1, pos2);
        Vector diff = getMaximumCoordinatePoint(pos1, pos2).subtract(min);
        loopTroughBlocks(diff, min, world, closedblock);

    }

    private static void loopTroughBlocks(Vector area, Vector offset, World world, Material block) {
        for (int x = 0; x <= area.getBlockX(); x++) {
            for (int y = 0; y <= area.getBlockY(); y++) {
                for (int z = 0; z <= area.getBlockZ(); z++) {
                    Location loc = new Location(world, x + offset.getBlockX(), y + offset.getBlockY(), z + offset.getBlockZ());
                    loc.getBlock().setType(block);
                }
            }
        }
    }
}
