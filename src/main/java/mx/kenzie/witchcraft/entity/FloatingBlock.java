package mx.kenzie.witchcraft.entity;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

public interface FloatingBlock extends CustomEntity, ArmorStand, NoSpawnGrave {
    
    void display(Material material);
    
    void display(Block block);
    
}
