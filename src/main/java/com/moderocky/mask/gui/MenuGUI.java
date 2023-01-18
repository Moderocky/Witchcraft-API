package com.moderocky.mask.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiConsumer;

/**
 *
 */
public class MenuGUI implements InventoryGUI, Listener {
    
    private final Inventory inventory;
    private final List<Player> players = new ArrayList<>();
    private final TreeMap<Integer, BiConsumer<Player, InventoryClickEvent>> map = new TreeMap<>();
    private final boolean editable;
    private final Plugin plugin;
    
    public MenuGUI(Plugin plugin, InventoryType type, String title) {
        this.plugin = plugin;
        if (!type.isCreatable()) throw new IllegalArgumentException();
        this.inventory = Bukkit.createInventory(null, type, title);
        this.editable = false;
    }
    
    public MenuGUI(Plugin plugin, int size, String title) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null, size, title);
        this.editable = false;
    }
    
    @Override
    public int getSize() {
        return inventory.getSize();
    }
    
    @Override
    public boolean isEditable() {
        return editable;
    }
    
    @Override
    public InventoryType getType() {
        return inventory.getType();
    }
    
    public MenuGUI createButton(int slot, ItemStack itemStack, BiConsumer<Player, InventoryClickEvent> consumer) {
        map.put(slot, consumer);
        inventory.setItem(slot, itemStack);
        return this;
    }
    
    public MenuGUI createTile(int slot, ItemStack itemStack, boolean stealable) {
        BiConsumer<Player, InventoryClickEvent> consumer = (player, event) -> event.setCancelled(!stealable);
        map.put(slot, consumer);
        inventory.setItem(slot, itemStack);
        return this;
    }
    
    public MenuGUI createTile(ItemStack itemStack, Integer... integers) {
        BiConsumer<Player, InventoryClickEvent> consumer = (player, event) -> event.setCancelled(true);
        for (Integer slot : integers) {
            map.put(slot, consumer);
            inventory.setItem(slot, itemStack);
        }
        return this;
    }
    
    @Override
    public void open(Player player) {
        if (!hasListener()) register();
        player.openInventory(inventory);
        players.add(player);
    }
    
    @Override
    public boolean isOpen(Player player) {
        return players.contains(player);
    }
    
    private boolean hasListener() {
        for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin)) {
            if (listener.getListener() == this) return true;
        }
        return false;
    }
    
    private void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (event.getInventory() != inventory) return;
        if (players.isEmpty()) return;
        Player player = (Player) event.getWhoClicked();
        if (!players.contains(player)) return;
        Inventory clicked = event.getClickedInventory();
        if (clicked == null) return;
        if (!clicked.equals(inventory)) return;
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) return;
        if (!editable) event.setCancelled(true);
        if (!map.containsKey(event.getSlot())) return;
        map.get(event.getSlot()).accept(player, event);
    }
    
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (players.isEmpty()) return;
        Player player = (Player) event.getPlayer();
        if (event.getInventory() != inventory) return;
        if (!players.contains(player)) return;
        players.remove(player);
        remove();
    }
    
    private void remove() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (players.isEmpty() && hasListener()) unregister();
            }
        }.runTaskLater(plugin, 80);
    }
    
    public void unregister() {
        HandlerList.unregisterAll(this);
    }
    
}
