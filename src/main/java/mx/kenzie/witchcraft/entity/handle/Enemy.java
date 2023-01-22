package mx.kenzie.witchcraft.entity.handle;

import org.bukkit.entity.LivingEntity;

public interface Enemy extends Handle {
    
    LivingEntity getBukkitEntity();
    
}
