package com.moderocky.mask.gui;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Consumer;

public class BookGUI implements TextGUI, Listener {
    
    private final BookMeta meta;
    private final String bookCode = UUID.randomUUID().getMostSignificantBits() + "";
    private final List<Player> players = new ArrayList<>();
    private final TreeMap<String, Consumer<Player>> map = new TreeMap<>();
    private final TreeMap<String, Boolean> reopenMap = new TreeMap<>();
    
    private final Plugin plugin;
    
    public BookGUI(Plugin plugin, String title) {
        this.plugin = plugin;
        this.meta = (BookMeta) new ItemStack(Material.WRITTEN_BOOK).getItemMeta();
    }
    
    private void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public Listener getListener() {
        return this;
    }
    
    public void reset() {
        meta.setPages(new ArrayList<>());
    }
    
    public void addPage(@NotNull BaseComponent[] page) {
        meta.spigot().addPage(page);
    }
    
    public void addPages(@NotNull BaseComponent[]... pages) {
        meta.spigot().addPage(pages);
    }
    
    public void addPages(@NotNull List<BaseComponent[]> pages) {
        meta.spigot().addPage(pages.toArray(new BaseComponent[0][]));
    }
    
    public ClickEvent createButton(@NotNull Consumer<Player> consumer) {
        return createButton(consumer, true);
    }
    
    public ClickEvent createButton(@NotNull Consumer<Player> consumer, boolean reopen) {
        String key = UUID.randomUUID().toString();
        map.put(key, consumer);
        reopenMap.put(key, reopen);
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/!bk " + bookCode + " " + key);
    }
    
    public BaseComponent[] page(BaseComponent[]... lines) {
        ComponentBuilder builder = new ComponentBuilder("");
        boolean first = true;
        for (BaseComponent[] line : lines) {
            builder.append(line);
            if (!first) builder.append(System.lineSeparator());
            if (first) first = false;
        }
        return builder.create();
    }
    
    public void setPages(@NotNull BaseComponent[]... pages) {
        meta.spigot().setPages(pages);
    }
    
    public void setPages(@NotNull List<BaseComponent[]> pages) {
        meta.spigot().setPages(pages);
    }
    
    public ClickEvent createButton(int page) {
        return new ClickEvent(ClickEvent.Action.CHANGE_PAGE, page + "");
    }
    
    @EventHandler
    public void onClick(PlayerCommandPreprocessEvent event) {
        String string = event.getMessage();
        if (!string.contains("!bk") || string.split(" ").length < 3) return;
        if (!string.split(" ")[1].equalsIgnoreCase(bookCode)) return;
        if (!map.containsKey(string.split(" ")[2])) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        String key = string.split(" ")[2];
        boolean boo = reopenMap.getOrDefault(key, false);
        Consumer<Player> consumer = map.get(key);
        consumer.accept(player);
        if (boo) Bukkit.getScheduler().runTaskLater(plugin, () -> open(player), 0);
    }
    
    @Override
    public void open(Player player) {
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
        itemStack.setItemMeta(meta);
        player.openBook(itemStack);
    }
    
    @Override
    public boolean isOpen(Player player) {
        return false;
    }
    
    private void remove() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (players.isEmpty() && hasListener()) unregister();
            }
        }.runTaskLater(plugin, 80);
    }
    
    private boolean hasListener() {
        for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin)) {
            if (listener.getListener() == this) return true;
        }
        return false;
    }
    
    public void unregister() {
        HandlerList.unregisterAll(this);
    }
    
}
