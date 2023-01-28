package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class CloakOfDarknessSpell extends AbstractSummonSpell {

    transient final ParticleCreator creator = ParticleCreator.of(Particle.FIREWORKS_SPARK);

    public CloakOfDarknessSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return WitchcraftAPI.minecraft.nearbySummons(caster, CustomEntityType.SHADOW) < 1 && super.canCast(caster);
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0.5, 0.2, 0.5);
        WitchcraftAPI.executor.submit(() -> {
            final VectorShape line = creator.createLine(new Vector(0, 1, 0), 2, 0.03);
            final ParticleBuilder builder = creator.getBuilder();
            for (Vector vector : line) {
                final Location point = location.clone().add(vector);
                builder.location(point).spawn();
                WitchcraftAPI.sleep(15);
            }
            CustomEntityType.SHADOW.summon(caster, location);
        });
    }
}
