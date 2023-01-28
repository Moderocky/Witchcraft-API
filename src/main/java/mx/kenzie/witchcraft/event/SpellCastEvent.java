package mx.kenzie.witchcraft.event;

import mx.kenzie.witchcraft.spell.Spell;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class SpellCastEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Spell spell;

    public SpellCastEvent(@NotNull final LivingEntity caster, @NotNull final Spell spell) {
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

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
