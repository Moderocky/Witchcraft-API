package mx.kenzie.witchcraft.texture;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Texture {
    
    void load();
    
    default void apply(Entity entity) {
        if (entity instanceof Player player) this.apply(player);
        else throw new RuntimeException("Action not supported.");
    }
    
    default void apply(Player player) {
        throw new RuntimeException("Action not supported.");
    }
    
    default void apply(Block block) {
        throw new RuntimeException("Action not supported.");
    }
    
    default void apply(ItemStack item) {
        throw new RuntimeException("Action not supported.");
    }
    
}
