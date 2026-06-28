package me.simoncrafter.mCCodeCamp.lib.WorldEdit;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SchematicUtil {

    private static File schematicFolder;

    public static void initialize(JavaPlugin plugin) {
        schematicFolder = new File(plugin.getDataFolder(), "schem");
        if (!schematicFolder.exists()) {
            schematicFolder.mkdirs();
        }
    }

    public static class PlayerSelection {
        public List<Vector> corners;
        public Region boundingBox;

        public PlayerSelection(List<Vector> corners, Region boundingBox) {
            this.corners = corners;
            this.boundingBox = boundingBox;
        }
    }

    public static PlayerSelection getPlayerSelection(Player player) throws Exception {
        com.sk89q.worldedit.entity.Player wePlayer = BukkitAdapter.adapt(player);
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(wePlayer);
        Region selection = session.getSelection(wePlayer.getWorld());

        BlockVector3 min = selection.getMinimumPoint();
        BlockVector3 max = selection.getMaximumPoint();

        List<Vector> corners = List.of(
            new Vector(min.x(), min.y(), min.z()),
            new Vector(max.x(), max.y(), max.z())
        );

        return new PlayerSelection(corners, selection);
    }

    public static CompletableFuture<Void> saveSchematic(String path, Clipboard clipboard) {
        return CompletableFuture.runAsync(() -> {
            try {
                File schematicFile = new File(schematicFolder, path.endsWith(".schem") ? path : path + ".schem");
                schematicFile.getParentFile().mkdirs();

                ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
                if (format == null) {
                    throw new IOException("Unknown schematic format for: " + schematicFile.getName());
                }

                try (FileOutputStream fos = new FileOutputStream(schematicFile);
                     ClipboardWriter writer = format.getWriter(fos)) {
                    writer.write(clipboard);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static CompletableFuture<Clipboard> loadSchematic(String path) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                File schematicFile = new File(schematicFolder, path.endsWith(".schem") ? path : path + ".schem");

                if (!schematicFile.exists()) {
                    throw new IOException("Schematic file not found: " + schematicFile.getAbsolutePath());
                }

                ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
                if (format == null) {
                    throw new IOException("Unknown schematic format for: " + schematicFile.getName());
                }

                try (FileInputStream fis = new FileInputStream(schematicFile);
                     ClipboardReader reader = format.getReader(fis)) {
                    return reader.read();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static CompletableFuture<List<String>> listSchematics() {
        return CompletableFuture.supplyAsync(() -> {
            List<String> schematics = new ArrayList<>();
            listSchematicsRecursive(schematicFolder, "", schematics);
            return schematics;
        });
    }

    private static void listSchematicsRecursive(File directory, String prefix, List<String> result) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            String relativePath = prefix.isEmpty() ? file.getName() : prefix + "/" + file.getName();
            if (file.isDirectory()) {
                listSchematicsRecursive(file, relativePath, result);
            } else if (file.getName().endsWith(".schem")) {
                result.add(relativePath.replace(".schem", ""));
            }
        }
    }
}
