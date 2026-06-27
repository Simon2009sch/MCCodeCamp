package me.simoncrafter.mCCodeCamp.lib.ActivationHandler;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.ClearCharAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.MessageAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfirmQuestion;
import me.simoncrafter.mCCodeCamp.lib.PersistantDataTags;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ActivationEvents implements Listener {

    private static Map<String, IButtonActivated> buttonMap = new HashMap<>();
    private static boolean ableToBreakButtonBlock = false;

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        Entity entity = getNearbyButtonEntityMarkerEntities(event.getClickedBlock().getLocation());
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && entity != null && event.getHand() == EquipmentSlot.HAND) {
            String id = entity.getPersistentDataContainer().get(PersistantDataTags.activationButton, PersistentDataType.STRING);
            if (!buttonMap.containsKey(id)) {
                return;
            }
            buttonMap.get(id).buttonPressed(event.getPlayer(), event.getClickedBlock());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreakBlockEvent(BlockBreakEvent event) {
        Location blockLocation = event.getBlock().getLocation();
        Player player = event.getPlayer();
        Entity entity = getNearbyButtonEntityMarkerEntities(blockLocation);
        if (entity != null && !ableToBreakButtonBlock) {
            event.setCancelled(true);
            ConfirmQuestion confirmQuestion = ConfirmQuestion.create(Component.text("Do you really want to destroy this button block?", NamedTextColor.RED))
                    .addConfirmAction(CustomAction.create(p -> {
                        entity.remove();
                        p.breakBlock(event.getBlock());
                    }))
                    .addConfirmAction(ClearCharAction.create())
                    .addCancelAction(ClearCharAction.create())
                    .addCancelAction(MessageAction.create(Component.text("Canceled", NamedTextColor.RED)))
                    .removeExitButton();
            confirmQuestion.show(player);
        }
    }

    public static Entity getNearbyButtonEntityMarkerEntities(Location location) {
        Collection<Entity> nearbyEntities = location.getNearbyEntities(0.01, 0.01, 0.01);
        for (Entity e : nearbyEntities) {
            if (e.getPersistentDataContainer().has(PersistantDataTags.activationButton) && e.getType() == EntityType.MARKER) {
                return e;
            }
        }
        return null;
    }

    public static void registerNewActivationButton(String id, IButtonActivated button) {
        buttonMap.put(id, button);
    }

    public static void unregisterActivationButton(String id) {

    }

}
