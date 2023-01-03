package com.moderocky.mask.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

public class PaginatedGUI extends VisualGUI {
    
    private final List<ItemStack> entries = new ArrayList<>();
    
    private char entryChar;
    
    private BiConsumer<Player, InventoryClickEvent> consumer = null;
    private int pageCount = 0;
    private int page = 0;
    
    public PaginatedGUI(Plugin plugin, InventoryType type, String title) {
        super(plugin, type, title);
    }
    
    public PaginatedGUI(Plugin plugin, int size, String title) {
        super(plugin, size, title);
    }
    
    public BiConsumer<Player, InventoryClickEvent> getEntryConsumer() {
        return consumer;
    }
    
    public void setEntryConsumer(@NotNull BiConsumer<Player, InventoryClickEvent> consumer) {
        this.consumer = consumer;
    }
    
    public void setEntryChar(char c) {
        entryChar = c;
    }
    
    public List<ItemStack> getEntries() {
        return new ArrayList<>(entries);
    }
    
    public void setEntries(Collection<ItemStack> entries) {
        this.entries.clear();
        this.entries.addAll(entries);
    }
    
    public void setEntries(ItemStack... entries) {
        this.entries.clear();
        this.entries.addAll(Arrays.asList(entries));
    }
    
    public BiConsumer<Player, InventoryClickEvent> getPageUp() {
        return (player, event) -> {
            final PaginatedGUI gui = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    gui.next();
                }
            }.runTaskLater(plugin, 1L);
        };
    }
    
    public void next() {
        page = Math.min(pageCount, (page + 1));
        createPage();
    }
    
    private void createPage() {
        clearEntrySlots();
        final int[] ints = getSlots(entryChar);
        Integer[] integers = new Integer[ints.length];
        for (int i = 0; i < ints.length; i++) {
            integers[i] = ints[i];
        }
        Iterator<Integer> iterator = Arrays.asList(integers).iterator();
        for (int i = Math.round(page * ints.length); i < Math.round((page + 1) * getSlots(entryChar).length); i++) {
            if (!iterator.hasNext()) break;
            if (i >= entries.size()) break;
            int j = iterator.next();
            if (j < 0 || j >= inventory.getSize()) continue;
            inventory.setItem(j, entries.get(i));
        }
    }
    
    private void clearEntrySlots() {
        for (int slot : getSlots(entryChar)) {
            inventory.setItem(slot, new ItemStack(Material.AIR));
        }
    }
    
    public BiConsumer<Player, InventoryClickEvent> getPageDown() {
        return (player, event) -> {
            final PaginatedGUI gui = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    gui.prev();
                }
            }.runTaskLater(plugin, 1L);
        };
    }
    
    public void prev() {
        page = Math.max(0, (page - 1));
        createPage();
    }
    
    @Override
    public PaginatedGUI setLayout(String[] layout) {
        return (PaginatedGUI) super.setLayout(layout);
    }
    
    @Override
    public void open(Player player) {
        super.open(player);
    }
    
    @Override
    public void finalise() {
        super.finalise();
        pageCount = (int) Math.ceil((float) entries.size() / getSlots(entryChar).length);
        reset();
    }
    
    @Override
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
        if (map.containsKey(event.getSlot()))
            map.get(event.getSlot()).accept(player, event);
        else if (Arrays.stream(getSlots(entryChar)).anyMatch(i -> i == event.getSlot()))
            if (consumer != null) consumer.accept(player, event);
    }
    
    @Override
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        super.onClose(event);
        reset();
    }
    
    public void reset() {
        page = 0;
        createPage();
    }
    
    public BiConsumer<Player, InventoryClickEvent> getPageReset() {
        return (player, event) -> {
            final PaginatedGUI gui = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    gui.reset();
                }
            }.runTaskLater(plugin, 1L);
        };
    }
    
    private int getFreeSlotCount() {
        int j = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack == null || stack.getType() == Material.AIR) j++;
        }
        return j;
    }
    
    private int[] getFreeSlots() {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack == null || stack.getType() == Material.AIR) slots.add(i);
        }
        int[] ints = new int[slots.size()];
        int i = 0;
        for (Integer slot : slots) {
            ints[i] = slot;
            i++;
        }
        return ints;
    }
    
}
