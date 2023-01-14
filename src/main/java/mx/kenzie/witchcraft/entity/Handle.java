package mx.kenzie.witchcraft.entity;

import mx.kenzie.witchcraft.data.entity.CustomEntityType;
import org.bukkit.entity.Entity;

import java.util.UUID;

public interface Handle {
    
    Entity getBukkitEntity();
    
    boolean isTicking();
    
    default String getTypeId() {
        return this.getKind().key;
    }
    
    CustomEntityType getKind();
    
    UUID getUUID();
    
    default boolean spawnGrave() {
        return !(this instanceof Summon || this instanceof MalleablePortal || this instanceof NoSpawnGrave);
    }
    
}
