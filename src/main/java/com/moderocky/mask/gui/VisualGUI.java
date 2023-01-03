package com.moderocky.mask.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 */
public class VisualGUI implements InventoryGUI, Listener {
    
    protected final Inventory inventory;
    protected final List<Player> players = new ArrayList<>();
    protected final TreeMap<Integer, BiConsumer<Player, InventoryClickEvent>> map = new TreeMap<>();
    protected final HashMap<Character, ItemStack> layoutMap = new HashMap<>();
    protected final boolean editable;
    protected final Plugin plugin;
    protected final List<Consumer<?>> preActionConsumers = new ArrayList<>();
    
    protected boolean finished = false;
    protected String[] layout = new String[0];
    
    public VisualGUI(Plugin plugin, InventoryType type, String title) {
        this.plugin = plugin;
        if (!type.isCreatable()) throw new IllegalArgumentException();
        this.inventory = Bukkit.createInventory(null, type, title);
        this.editable = false;
    }
    
    public VisualGUI(Plugin plugin, int size, String title) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null, size, title);
        this.editable = false;
    }
    
    public String[] getLayout() {
        return layout;
    }
    
    public VisualGUI setLayout(String[] layout) {
        if (!finished)
            this.layout = layout;
        return this;
    }
    
    public @NotNull ItemStack getReference(char ch) {
        return layoutMap.getOrDefault(ch, new ItemStack(Material.AIR));
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
    
    public VisualGUI createButton(char ch, @NotNull ItemStack itemStack, @NotNull BiConsumer<Player, InventoryClickEvent> consumer) {
        preActionConsumers.add(o -> {
            for (int slot : getSlots(ch)) {
                map.put(Math.max(0, Math.min(inventory.getSize(), slot)), consumer);
                inventory.setItem(Math.max(0, Math.min(inventory.getSize(), slot)), itemStack);
            }
        });
        return this;
    }
    
    public int[] getSlots(char ch) {
        List<Integer> integers = new ArrayList<>();
        char[] chars = String.join("", layout).toCharArray();
        int n = 0;
        for (char c : chars) {
            if (c == ch) integers.add(n);
            n++;
        }
        int[] ints = new int[integers.size()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = integers.get(i);
        }
        return ints;
    }
    
    public VisualGUI createTile(char ch, ItemStack itemStack, boolean stealable) {
        preActionConsumers.add(o -> {
            BiConsumer<Player, InventoryClickEvent> consumer = (player, event) -> event.setCancelled(!stealable);
            for (int slot : getSlots(ch)) {
                map.put(Math.max(0, Math.min(inventory.getSize(), slot)), consumer);
                inventory.setItem(Math.max(0, Math.min(inventory.getSize(), slot)), itemStack);
            }
            
        });
        return this;
    }
    
    public VisualGUI createTile(char ch, @NotNull ItemStack itemStack) {
        layoutMap.put(ch, itemStack);
        preActionConsumers.add(o -> {
            for (int slot : getSlots(ch)) {
                inventory.setItem(Math.max(0, Math.min(inventory.getSize(), slot)), itemStack);
            }
            
        });
        return this;
    }
    
    @Override
    public void open(Player player) {
        if (!finished) finalise();
        if (!hasListener()) register();
        player.openInventory(inventory);
        players.add(player);
    }
    
    public void finalise() {
        map.clear();
        inventory.clear();
        for (Consumer<?> consumer : preActionConsumers) {
            consumer.accept(null);
        }
        finished = true;
    }
    
    protected boolean hasListener() {
        for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin)) {
            if (listener.getListener() == this) return true;
        }
        return false;
    }
    
    private void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public boolean isOpen(Player player) {
        return players.contains(player);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(InventoryClickEvent event) {
        if (event.isCancelled()) return;
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
    
    protected void remove() {
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
