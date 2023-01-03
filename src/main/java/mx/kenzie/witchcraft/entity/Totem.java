package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.LivingEntity;

import java.util.function.Consumer;

public interface Totem extends Summon, Handle {
    
    void setMajorTickConsumer(Consumer<LivingEntity> consumer);
    
}
