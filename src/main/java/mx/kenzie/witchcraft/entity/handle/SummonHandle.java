package mx.kenzie.witchcraft.entity.handle;

import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public interface SummonHandle extends Handle {
    
    UUID getOwnerID();
    
    void setOwner(UUID uuid);
    
    LivingEntity getOwningEntity();
    
    void setTarget(LivingEntity target);
    
}
