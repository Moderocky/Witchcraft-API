package mx.kenzie.witchcraft.entity;

import mx.kenzie.witchcraft.entity.handle.Handle;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.function.Function;

public interface Projectile extends Handle {
    double getDamage();
    
    void setDamage(double damage);
    
    void onCollide(Runnable collide);
    
    boolean shouldCollide(Entity entity);
    
    boolean launch();
    
    void setVelocity(Vector velocity);
    
    boolean hasLaunched();
    
    boolean hasCollided();
    
    Location getPrevious();
    
    Location getLocation();
    
    Location getPotentialNext();
    
    org.bukkit.entity.LivingEntity getShooter();
    
    void setCanCollideWith(Function<org.bukkit.entity.Entity, Boolean> function);
    
    void onCollideWithBlock(Function<Block, Boolean> function);
    
    void onCollideWithEntity(Function<org.bukkit.entity.Entity, Boolean> function);
    
    void onTick(Runnable tick);
}
