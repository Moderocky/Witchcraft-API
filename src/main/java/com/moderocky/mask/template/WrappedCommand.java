package com.moderocky.mask.template;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

    default @NotNull Component getHelpMessage(int i, @NotNull String[] args) {
        final PluginCommand command = Bukkit.getPluginCommand(this.getCommand());
        if (command == null) return Component.text("No instructions found.", NamedTextColor.GRAY);
        return Component.text("Usage: " + command.getUsage(), NamedTextColor.GRAY);
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
    @SuppressWarnings("deprecation")
    default void register(Plugin plugin) {
        try {
            final Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            if (!constructor.isAccessible()) constructor.setAccessible(true);
            final PluginCommand command = constructor.newInstance(this.getCommand(), plugin);
            command.setAliases(this.getAliases());
            command.setDescription(this.getDescription());
            command.setPermission(this.getPermission());
            command.permissionMessage(this.getPermissionMessage());
            command.setUsage(this.getUsage());
            command.register(this.getCommandMap());
            if (this.getCommandMap().register(this.getCommand(), plugin.getName(), command)) {
                command.setExecutor(this);
                command.setTabCompleter(this);
            } else {
                final Command current = this.getCommandMap().getCommand(command.getName());
                if (current instanceof PluginCommand found) {
                    found.setExecutor(this);
                    found.setTabCompleter(this);
                    found.setAliases(this.getAliases());
                    found.setDescription(this.getDescription());
                    found.setPermission(this.getPermission());
                    found.permissionMessage(this.getPermissionMessage());
                    found.setUsage(this.getUsage());
                }
                Bukkit.getLogger().log(Level.WARNING, "A command '/" + this.getCommand() + "' is already defined!");
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
    @Nullable Component getPermissionMessage();

    /**
     * @return The 'correct usage' string
     */
    @NotNull String getUsage();

    default CommandMap getCommandMap() {
        return Bukkit.getCommandMap();
    }

    @Override
    boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
}
