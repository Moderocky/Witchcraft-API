package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public interface Summon extends Handle {
    
    UUID getOwnerUUID();
    
    void setOwner(UUID uuid);
    
    Entity getBukkitEntity();
    
    LivingEntity getBukkitOwner();
    
}
