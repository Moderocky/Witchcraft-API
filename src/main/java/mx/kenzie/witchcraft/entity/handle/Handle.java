package mx.kenzie.witchcraft.entity.handle;

import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.entity.NoSpawnGrave;
import mx.kenzie.witchcraft.entity.Portal;
import mx.kenzie.witchcraft.entity.Summon;
import org.bukkit.entity.Entity;

import java.util.UUID;

public interface Handle {
    
    default String getTypeId() {
        return this.getKind().key;
    }
    
    CustomEntityType<?> getKind();
    
    default UUID getUniqueId() {
        return this.getBukkitEntity().getUniqueId();
    }
    
    Entity getBukkitEntity();
    
    default boolean shouldSpawnGrave() {
        return !(this instanceof Summon || this instanceof Portal || this instanceof NoSpawnGrave);
    }
    
}
