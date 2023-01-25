package mx.kenzie.witchcraft.entity.goal;

import org.jetbrains.annotations.NotNull;

public abstract class Behaviour<EntityType> {
    
    public static final Behaviour<?> EMPTY = new Behaviour<>() {};
    
    public EntityType entity;
    
    public void tick() {
    }
    
    public void click(PlayerInteraction interaction) {
    }
    
    public final @NotNull EntityType getEntity() {
        return entity;
    }
    
}
