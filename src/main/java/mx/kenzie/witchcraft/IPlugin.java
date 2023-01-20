package mx.kenzie.witchcraft;

import com.moderocky.mask.command.Commander;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.Position;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface IPlugin extends Plugin {
    
    static IPlugin getInstance() {
        return WitchcraftAPI.plugin;
    }
    
    void syncToDiscord(PlayerData data);
    
    ColorProfile getColors();
    
    /**
     * Create a key.
     */
    NamespacedKey getKey(String key);
    
    /**
     * Get the spell pattern session for a player.
     */
    Session get(Player player);
    
    /**
     * Get/create a spell pattern session for a player.
     */
    Session getOrCreate(Player player);
    
    /**
     * Get the main respawn location.
     */
    Position getSpawn();
    
    Component getCommandHelpMessage(Commander<?> commander);
}
