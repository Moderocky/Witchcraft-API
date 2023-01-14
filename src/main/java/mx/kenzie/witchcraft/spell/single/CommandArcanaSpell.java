package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.entity.CustomEntityType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class CommandArcanaSpell extends AbstractSummonSpell {
    
    public CommandArcanaSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return WitchcraftAPI.minecraft.nearbySummons(caster, CustomEntityType.ARCANA_SUMMON) < 1 && super.canCast(caster);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0.5, 0.2, 0.5);
        WitchcraftAPI.client.particles(Particle.SOUL_FIRE_FLAME.builder().count(0)).playSpiral(
            location.setDirection(new Vector(0, 1, 0)),
            0.6, 2, 10, 1
        );
        WitchcraftAPI.minecraft.summonArcana(caster, location);
    }
}
