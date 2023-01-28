package mx.kenzie.witchcraft;

import mx.kenzie.witchcraft.ward.WardInstance;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

/**
 * This library binds to any protection on the world.
 * This will be used to detect wards, etc.
 */
public interface Protection {

    static Protection getInstance() {
        return WitchcraftAPI.protection;
    }

    default boolean canPlace(LivingEntity entity, Location location, Material material) {
        for (WardInstance instance : this.getWardsAt(location)) if (!instance.canPlace(entity, material)) return false;
        return true;
    }

    Collection<WardInstance> getWardsAt(Location location);

    default boolean canPlace(LivingEntity entity, Location location) {
        for (WardInstance instance : this.getWardsAt(location)) if (!instance.canPlace(entity)) return false;
        return true;
    }

    default boolean canBreak(LivingEntity entity, Location location) {
        for (WardInstance instance : this.getWardsAt(location)) if (!instance.canBreak(entity)) return false;
        return true;
    }

    default boolean canHurt(LivingEntity entity, Location location, Entity target) {
        for (WardInstance instance : this.getWardsAt(location)) if (!instance.canHurt(entity, target)) return false;
        return true;
    }

    default boolean canTeleport(LivingEntity entity, Location location) {
        for (WardInstance instance : this.getWardsAt(location)) if (!instance.canTeleport(entity)) return false;
        return true;
    }

    void registerWard(WardInstance instance);

    void discard(WardInstance instance);

}
