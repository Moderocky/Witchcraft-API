package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;

import java.util.function.Consumer;

public interface Hammer extends Totem, ArmorStand {
    
    void setMinorTickConsumer(Consumer<LivingEntity> consumer);
    
}
