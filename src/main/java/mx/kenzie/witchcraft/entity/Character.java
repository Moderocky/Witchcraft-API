package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;

public interface Character extends NPC, CustomEntity, LivingEntity {
    
    boolean isNamed();
    
}
