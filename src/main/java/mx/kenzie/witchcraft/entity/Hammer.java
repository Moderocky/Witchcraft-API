package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.LivingEntity;

import java.util.function.Consumer;

public interface Hammer extends Totem, Handle {
    
    void setMinorTickConsumer(Consumer<LivingEntity> consumer);
    
}
