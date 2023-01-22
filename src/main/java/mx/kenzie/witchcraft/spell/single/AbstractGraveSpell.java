package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.entity.handle.Grave;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;

public abstract class AbstractGraveSpell extends AbstractSummonSpell {
    
    protected List<Grave> graves;
    
    public AbstractGraveSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        this.graves = getGraves(caster, 20);
        if (Minecraft.getInstance().nearbySummons(caster, null) >= maxSummonCount(caster)) return false;
        return !graves.isEmpty();
    }
    
    protected static List<Grave> getGraves(LivingEntity caster, int range) {
        return Minecraft.getInstance().nearbyGraves(caster.getLocation(), range, range / 3.0);
    }
    
}
