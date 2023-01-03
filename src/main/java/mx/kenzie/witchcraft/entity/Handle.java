package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.Entity;

import java.util.UUID;

public interface Handle {
    
    Entity getBukkitEntity();
    
    boolean isTicking();
    
    String getTypeId();
    
    UUID getUUID();
    
}
