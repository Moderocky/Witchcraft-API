package mx.kenzie.witchcraft.entity.handle;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface ProjectileHandle extends Handle {
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
    
    void setCanCollideWith(Function<Entity, Boolean> function);
    
    void onCollideWithBlock(BiFunction<Block, BlockFace, Boolean> function);
    
    void onCollideWithEntity(Function<Entity, Boolean> function);
    
    void onTick(Runnable tick);
}
