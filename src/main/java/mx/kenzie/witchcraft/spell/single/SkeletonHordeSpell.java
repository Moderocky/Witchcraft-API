package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.entity.Grave;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class SkeletonHordeSpell extends AbstractGraveSpell {
    
    private transient final ParticleCreator creator = ParticleCreator.of(Material.REDSTONE_BLOCK);
    
    public SkeletonHordeSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        int summons = summonCount(caster);
        for (Grave grave : graves) {
            if (summons >= maxSummonCount(caster)) break;
            if (!grave.canGrow()) continue;
            final Location location = grave.getBukkitEntity().getLocation();
            final Entity entity = CustomEntityType.SKELETON_SUMMON.summon(caster, location);
            summons++;
            grave.attemptGrow(entity);
            this.creator.createSpiral(
                location.setDirection(new Vector(0, 1, 0)),
                0.8, 2, 10, 2
            ).draw(location, 50);
        }
    }
}
