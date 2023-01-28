package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.entity.handle.Grave;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class RaiseFleshSpell extends AbstractGraveSpell {

    private transient final ParticleCreator creator = ParticleCreator.of(Material.REDSTONE_BLOCK);

    public RaiseFleshSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        for (Grave grave : graves) {
            if (!grave.canGrow()) continue;
            final Location location = grave.getStart();
            final Entity entity = CustomEntityType.ZOMBIE_SUMMON.summon(caster, location);
            grave.attemptGrow(entity);
            this.creator.createSpiral(
                location.setDirection(new Vector(0, 1, 0)),
                0.8, 2, 10, 2
            ).draw(location, 50);
            break;
        }
    }
}
