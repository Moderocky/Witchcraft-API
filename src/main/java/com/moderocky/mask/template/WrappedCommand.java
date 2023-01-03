package com.moderocky.mask.template;

import mx.kenzie.mirror.Mirror;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public interface WrappedCommand extends CommandExecutor, TabCompleter {
    
    default @NotNull BaseComponent[] getHelpMessage(int i, @NotNull String[] args) {
        PluginCommand command = Bukkit.getPluginCommand(getCommand());
        if (command == null)
            return new ComponentBuilder("No instructions found.").color(ChatColor.GRAY).create();
        return new ComponentBuilder("Usage: ").color(ChatColor.WHITE).append(command.getUsage()).color(ChatColor.GRAY)
            .create();
    }
    
    @NotNull
    String getCommand();
    
    @Override
    default @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> strings = getCompletions(args.length, sender, command, alias, args);
        if (strings == null) return null;
        final List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[args.length - 1], strings, completions);
        Collections.sort(completions);
        return completions;
    }
    
    default @Nullable List<String> getCompletions(int i, @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return getCompletions(i);
    }
    
    default @Nullable List<String> getCompletions(int i) {
        return null;
    }
    
    /**
     * This registers your command :)
     *
     * @param plugin Your plugin
     */
    default void register(Plugin plugin) {
        WrappedCommand command = this;
        try {
            Constructor<PluginCommand> commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            if (!commandConstructor.isAccessible())
                commandConstructor.setAccessible(true);
            PluginCommand pluginCommand = commandConstructor.newInstance(command.getCommand(), plugin);
            pluginCommand.setAliases(command.getAliases());
            pluginCommand.setDescription(command.getDescription());
            pluginCommand.setPermission(command.getPermission());
            pluginCommand.setPermissionMessage(command.getPermissionMessage());
            pluginCommand.setUsage(command.getUsage());
            pluginCommand.register(getCommandMap());
            if (getCommandMap().register(command.getCommand(), plugin.getName(), pluginCommand)) {
                pluginCommand.setExecutor(command);
                pluginCommand.setTabCompleter(command);
            } else {
                Command com = getCommandMap().getCommand(pluginCommand.getName());
                if (com instanceof PluginCommand) {
                    ((PluginCommand) com).setExecutor(command);
                    ((PluginCommand) com).setTabCompleter(command);
                }
                Bukkit.getLogger().log(Level.WARNING, "A command '/" + command.getCommand() + "' is already defined!");
                Bukkit.getLogger().log(Level.WARNING, "As this cannot be replaced, the executor will be overridden.");
                Bukkit.getLogger()
                    .log(Level.WARNING, "To avoid this warning, please do not add WrappedCommands to your plugin.yml.");
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @return the aliases, or new ArrayList if none
     */
    @NotNull List<String> getAliases();
    
    /**
     * @return A description of your command
     */
    @NotNull String getDescription();
    
    /**
     * @return The command's permission
     */
    @Nullable String getPermission();
    
    /**
     * @return The no-permission message
     */
    @Nullable String getPermissionMessage();
    
    /**
     * @return The 'correct usage' string
     */
    @NotNull String getUsage();
    
    default CommandMap getCommandMap() {
        return Mirror.of(Bukkit.getServer()).<CommandMap>field("commandMap").get();
    }
    
    @Override
    boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
}
