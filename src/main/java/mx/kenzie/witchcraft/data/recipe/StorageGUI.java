package mx.kenzie.witchcraft.data.recipe;

import com.moderocky.mask.gui.PaginatedGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

public class StorageGUI extends PaginatedGUI {

    public StorageGUI(Plugin plugin, InventoryType type, String title) {
        super(plugin, type, title);
    }

    public StorageGUI(Plugin plugin, int size, String title) {
        super(plugin, size, title);
        this.inventory.setMaxStackSize(127);
    }

    @Override
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        super.onClick(event);
        if (players.isEmpty()) return;
        final Player player = (Player) event.getWhoClicked();
        if (!players.contains(player)) return;
        event.setCancelled(true);
        this.onInventoryClick(event);
    }

    public void onInventoryClick(InventoryClickEvent event) {

    }

    @Override
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        super.onClose(event);
    }
}
