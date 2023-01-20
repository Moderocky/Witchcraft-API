package mx.kenzie.witchcraft.entity;

import org.bukkit.Location;

public interface Grave extends Handle {
    boolean isGrowing();
    
    boolean canGrow();
    
    boolean attemptGrow(org.bukkit.entity.Entity entity);
    
    Location getStart();
}
