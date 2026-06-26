package me.simoncrafter.mCCodeCamp.lib.ActivationHandler;

import me.simoncrafter.mCCodeCamp.lib.PersistantDataTags;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ActivationEvents implements EventListener {

    private static Map<Long, IButtonActivated> buttonMap = new HashMap<>();


    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Entity entity = getNearbyButtonEntityMarkerEntities(event.getClickedBlock().getLocation());
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && entity != null) {
            long id = entity.getPersistentDataContainer().get(PersistantDataTags.activationButton, PersistentDataType.LONG);
            if (!buttonMap.containsKey(id)) {
                return;
            }
            buttonMap.get(id).buttonPressed(event.getPlayer(), event.getClickedBlock());
        }
    }

    private Entity getNearbyButtonEntityMarkerEntities(Location location) {
        Collection<Entity> nearbyEntities = location.getNearbyEntities(0.01, 0.01, 0.01);
        for (Entity e : nearbyEntities) {
            if (e.getPersistentDataContainer().has(PersistantDataTags.activationButton)) {
                return e;
            }
        }
        return null;
    }

}
