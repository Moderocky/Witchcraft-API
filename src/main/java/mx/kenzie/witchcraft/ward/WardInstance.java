package mx.kenzie.witchcraft.ward;

import mx.kenzie.witchcraft.Protection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public abstract class WardInstance {
    protected final long expiry;
    
    protected WardInstance(long expiry) {
        this.expiry = expiry;
    }
    
    public long expiresAt() {
        return expiry;
    }
    
    public boolean expired() {
        if (expiry < 1) return false;
        return System.currentTimeMillis() >= expiry;
    }
    
    public boolean isOwner(LivingEntity entity) {
        return false;
    }
    
    public boolean permits(LivingEntity entity) {
        return false;
    }
    
    public void discard() {
        Protection.getInstance().discard(this);
    }
    
    public abstract UUID getWorld();
    
    public abstract boolean includes(Location location);
    
    public boolean permanent() {
        return expiry > 0;
    }
    
    public boolean canPlace(LivingEntity entity, Material material) {
        return this.canPlace(entity);
    }
    
    public boolean canPlace(LivingEntity entity) {
        return this.canBreak(entity);
    }
    
    public boolean canBreak(LivingEntity entity) {
        return true;
    }
    
    public boolean canHurt(LivingEntity entity, Entity target) {
        return true;
    }
    
    public boolean canTeleport(LivingEntity entity) {
        return true;
    }
    
}
