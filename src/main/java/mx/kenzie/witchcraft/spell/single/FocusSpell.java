package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.Summon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FocusSpell extends AbstractTargetedSpell {
    public FocusSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Target target = this.getTarget(caster, range, false);
        if (target == null) return;
        if (!(target.entity() instanceof LivingEntity thing)) return;
        final Set<Entity> entities = new HashSet<>(caster.getLocation().getNearbyEntities(12, 6, 12));
        entities.removeIf(entity -> {
            if (!(entity instanceof LivingEntity living)) return true;
            return !WitchcraftAPI.minecraft.isPet(caster, living);
        });
        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity)) continue;
            final Summon summon = WitchcraftAPI.minecraft.getAsSummon(entity);
            if (summon == null) continue;
            if (summon.getOwnerID() != caster.getUniqueId()) continue;
            summon.setTarget(thing);
        }
    }
}
