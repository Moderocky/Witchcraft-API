package mx.kenzie.witchcraft.event;

import mx.kenzie.witchcraft.spell.Spell;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class SpellPreCastEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Spell spell;
    private boolean cancel;
    
    public SpellPreCastEvent(@NotNull final LivingEntity caster, @NotNull final Spell spell) {
        super(caster);
        this.spell = spell;
    }
    
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    @Override
    public @NotNull LivingEntity getEntity() {
        return (LivingEntity) super.getEntity();
    }
    
    public Spell getSpell() {
        return spell;
    }
    
    @Override
    public boolean isCancelled() {
        return cancel;
    }
    
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
    
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
