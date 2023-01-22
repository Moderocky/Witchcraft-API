package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public interface Owned extends CustomEntity {
    
    UUID getOwnerID();
    
    void setOwner(UUID uuid);
    
    LivingEntity getOwningEntity();
    
}
