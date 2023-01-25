package mx.kenzie.witchcraft.entity;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

public interface FloatingBlock extends CustomEntity, ArmorStand, NoSpawnGrave {
    
    enum Type {
        STATIC, ROTATING
    }
    
    void display(Material material);
    
    void display(Block block);
    
    ParticleBuilder getBuilder();
    
}
