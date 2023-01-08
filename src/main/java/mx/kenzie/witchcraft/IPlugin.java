package mx.kenzie.witchcraft;

import mx.kenzie.witchcraft.data.Position;
import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface IPlugin extends Plugin {
    
    static IPlugin getInstance() {
        return WitchcraftAPI.plugin;
    }
    
    /**
     * This is the off-thread projectile ray trace ticker.
     */
    List<AbstractProjectile> projectiles();
    
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
    
}
