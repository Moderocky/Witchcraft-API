package mx.kenzie.witchcraft.entity.goal;

import org.jetbrains.annotations.NotNull;

public class Behaviour<EntityType> {
    
    public static final Behaviour<?> EMPTY = new Behaviour<>();
    
    public EntityType entity;
    
    public void tick() {
    }
    
    public void click(PlayerInteraction.Position interaction) {
        this.click((PlayerInteraction) interaction);
    }
    
    public void click(PlayerInteraction interaction) {
        interaction.setResult(Interaction.Result.PASS);
    }
    
    public void discard() {
    }
    
    public void collide(PushInteraction interaction) {
        interaction.setResult(Interaction.Result.SUCCESS);
    }
    
    public final @NotNull EntityType getEntity() {
        return entity;
    }
    
    public static <EntityType> Behaviour<EntityType> empty(EntityType entity) {
        final Behaviour<EntityType> behaviour = new Behaviour<>();
        behaviour.entity = entity;
        return behaviour;
    }
    
}
