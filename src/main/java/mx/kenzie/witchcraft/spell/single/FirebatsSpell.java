package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class FirebatsSpell extends AbstractSummonSpell {
    
    public FirebatsSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return WitchcraftAPI.minecraft.nearbySummons(caster, CustomEntityType.HELL_BAT_SUMMON) < 1 && super.canCast(caster);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0.5, 0.5, 0.5);
        ParticleCreator.of(Particle.SPELL_WITCH).drawPoof(location, 0.4, 10);
        CustomEntityType.HELL_BAT_SUMMON.summon(caster, location);
    }
}
