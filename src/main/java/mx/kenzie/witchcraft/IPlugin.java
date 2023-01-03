package mx.kenzie.witchcraft;

import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface IPlugin extends Plugin {
    
    List<AbstractProjectile> projectiles();
    
    NamespacedKey getKey(String key);
    
    Session get(Player player);
    
    Session getOrCreate(Player player);
    
}
