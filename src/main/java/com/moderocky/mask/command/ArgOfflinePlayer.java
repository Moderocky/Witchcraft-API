package com.moderocky.mask.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Moderocky
 * @version 1.0.0
 */
public class ArgOfflinePlayer implements Argument<OfflinePlayer> {

    private String label = "player/uuid";
    private boolean required = true;

    @Override
    public @NotNull OfflinePlayer serialise(String string) {
        OfflinePlayer player = Bukkit.getPlayer(string);
        if (player == null) {
            try {
                UUID uuid = UUID.fromString(string);
                player = Bukkit.getOfflinePlayer(uuid);
            } catch (Throwable throwable) {
                player = Bukkit.getOfflinePlayer(string);
            }
        }
        return player;
    }

    @Override
    public boolean matches(String string) {
        if (Bukkit.getPlayer(string) != null) return true;
        try {
            UUID uuid = UUID.fromString(string);
            Bukkit.getOfflinePlayer(uuid);
            return true;
        } catch (Throwable e) {
            return false;
        }
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
    public ArgOfflinePlayer setRequired(boolean boo) {
        required = boo;
        return this;
    }

    @Override
    public ArgOfflinePlayer setLabel(@NotNull String label) {
        this.label = label;
        return this;
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile("^(\\S+)$");
    }

}
