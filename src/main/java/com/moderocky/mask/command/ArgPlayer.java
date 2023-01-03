package com.moderocky.mask.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Moderocky
 * @version 1.0.0
 */
public class ArgPlayer implements Argument<Player> {
    
    private String label = "player";
    private boolean required = true;
    
    @Override
    public @NotNull Player serialise(String string) {
        Player player = Bukkit.getPlayer(string);
        if (player == null) throw new IllegalArgumentException();
        return player;
    }
    
    @Override
    public boolean matches(String string) {
        return (Bukkit.getPlayer(string) != null);
    }
    
    @Override
    public @NotNull String getName() {
        return label;
    }
    
    @Override
    public @Nullable List<String> getCompletions() {
        List<String> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getName()));
        return players;
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
    public ArgPlayer setRequired(boolean boo) {
        required = boo;
        return this;
    }
    
    @Override
    public ArgPlayer setLabel(@NotNull String label) {
        this.label = label;
        return this;
    }
    
    @Override
    public Pattern getPattern() {
        return Pattern.compile("^(\\S+)$");
    }
    
}
