package mx.kenzie.witchcraft.entity;

import mx.kenzie.witchcraft.data.WarlockDeity;

import java.util.UUID;

public interface Demon extends Handle {
    
    
    void setDeity(WarlockDeity deity);
    
    WarlockDeity getDeity();
    
    UUID getOwnerID();
    
    void setOwner(org.bukkit.entity.Entity entity);
}