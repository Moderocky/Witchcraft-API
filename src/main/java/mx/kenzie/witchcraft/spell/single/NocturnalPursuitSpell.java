package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class NocturnalPursuitSpell extends AbstractSummonSpell {
    
    public NocturnalPursuitSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0.5, 0.2, 0.5);
        WitchcraftAPI.client.particles(Particle.ASH)
            .playSpiral(location.setDirection(new Vector(0, 1, 0)), 0.5, 1, 10, 2);
        CustomEntityType.BAT_SUMMON.summon(caster, location);
    }
}
