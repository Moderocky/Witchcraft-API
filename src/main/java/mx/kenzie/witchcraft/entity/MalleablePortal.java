package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public interface MalleablePortal extends Handle {
    
    Vector getFacing();
    
    void setFacing(Vector vector);
    
    void setCollideConsumer(Consumer<Entity> collideConsumer);
    
}
