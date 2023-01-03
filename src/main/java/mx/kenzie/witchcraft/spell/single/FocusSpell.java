package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FocusSpell extends StandardSpell {
    public FocusSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Set<Entity> entities = new HashSet<>(caster.getLocation().getNearbyEntities(12, 6, 12));
        entities.removeIf(entity -> {
            if (!(entity instanceof LivingEntity living)) return true;
            return !WitchcraftAPI.minecraft.isPet(caster, living);
        });
        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (!WitchcraftAPI.minecraft.isPet(caster, living)) continue;
            // todo make target
        }
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
}
