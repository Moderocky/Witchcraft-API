package mx.kenzie.witchcraft;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * This library binds to any protection on the world.
 * This will be used to detect wards, etc.
 */
public interface Protection {
    
    static Protection getInstance() {
        return WitchcraftAPI.protection;
    }
    
    default boolean canPlace(LivingEntity entity, Location location, Material material) {
        return this.canPlace(entity, location);
    }
    
    default boolean canPlace(LivingEntity entity, Location location) {
        return this.canBreak(entity, location);
    }
    
    default boolean canBreak(LivingEntity entity, Location location) {
        return true;
    }
    
    default boolean canHurt(LivingEntity entity, Location location, Entity target) {
        return true;
    }
    
    default boolean canTeleport(LivingEntity entity, Location location) {
        return true;
    }
    
}
