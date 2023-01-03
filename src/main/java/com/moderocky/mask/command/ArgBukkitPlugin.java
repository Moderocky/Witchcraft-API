package com.moderocky.mask.command;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Moderocky
 * @version 1.0.0
 */
public class ArgBukkitPlugin implements Argument<Plugin> {
    
    private String label = "server";
    private boolean required = true;
    
    @Override
    public @NotNull Plugin serialise(String string) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(string);
        if (plugin == null) throw new IllegalArgumentException();
        return plugin;
    }
    
    @Override
    public boolean matches(String string) {
        return (Bukkit.getPluginManager().getPlugin(string) != null);
    }
    
    @Override
    public @NotNull String getName() {
        return label;
    }
    
    @Override
    public @Nullable List<String> getCompletions() {
        List<String> strings = new ArrayList<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            strings.add(plugin.getName());
        }
        return strings;
    }
    
    @Override
    public boolean isPlural() {
        return false;
    }
    
    @Override
    public boolean isRequired() {
        return required;
    }
    
    @Override
    public ArgBukkitPlugin setRequired(boolean boo) {
        required = boo;
        return this;
    }
    
    @Override
    public ArgBukkitPlugin setLabel(@NotNull String label) {
        this.label = label;
        return this;
    }
    
}
