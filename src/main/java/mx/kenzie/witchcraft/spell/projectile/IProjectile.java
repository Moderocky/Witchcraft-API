package mx.kenzie.witchcraft.spell.projectile;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public interface IProjectile {
    
    void onLaunch();
    
    void onRemove();
    
    void onTick();
    
    void onCollide(Entity entity);
    
    void onCollide(Block block);
    
    BoundingBox getBoundingBox();
    
    Location getLocation();
    
    Location getPrevious();
    
    Location getPotentialNext();
    
    Vector getMotion();
    
    @Nullable Entity getSource();
    
    boolean hasCollided();
    
}
