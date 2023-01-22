package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class PlantGuardianSpell extends AbstractSummonSpell {
    
    protected transient final ParticleCreator creator = ParticleCreator.of(Particle.COMPOSTER);
    
    public PlantGuardianSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return WitchcraftAPI.minecraft.nearbySummons(caster, CustomEntityType.PLANT_GUARDIAN_SUMMON) < 2 && super.canCast(caster);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0.5, 0.2, 0.5);
        this.creator.createSpiral(
            location.setDirection(new Vector(0, 1, 0)),
            0.8, 2, 10, 2
        ).draw(50);
        CustomEntityType.PLANT_GUARDIAN_SUMMON.summon(caster, location);
    }
}
