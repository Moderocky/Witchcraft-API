package mx.kenzie.witchcraft.spell.single;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public abstract class AbstractEntitySpell extends AbstractTargetedSpell {
    protected transient LivingEntity target;
    
    public AbstractEntitySpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        final Target target = this.getTarget(caster, 25, false, Mode.SAFE_TO_TARGET);
        if (target == null) return false;
        final Entity entity = target.entity();
        if (!(entity instanceof LivingEntity living)) return false;
        this.target = living;
        return true;
    }
}
