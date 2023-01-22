package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.Entity;

import java.util.UUID;

public interface Handle {
    
    Entity getBukkitEntity();
    
    default String getTypeId() {
        return this.getKind().key;
    }
    
    CustomEntityType getKind();
    
    default UUID getUniqueId() {
        return this.getBukkitEntity().getUniqueId();
    }
    
    default boolean shouldSpawnGrave() {
        return !(this instanceof Summon || this instanceof MalleablePortal || this instanceof NoSpawnGrave);
    }
    
}
