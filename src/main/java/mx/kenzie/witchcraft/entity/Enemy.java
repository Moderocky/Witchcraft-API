package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.LivingEntity;

public interface Enemy extends Handle {
    
    LivingEntity getBukkitEntity();
    
}
