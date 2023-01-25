package mx.kenzie.witchcraft.entity.goal;

import org.bukkit.entity.Entity;

public class PushInteraction extends Interaction {
    protected final Entity entity;
    
    public PushInteraction(Entity entity) {
        this.entity = entity;
    }
    
}
