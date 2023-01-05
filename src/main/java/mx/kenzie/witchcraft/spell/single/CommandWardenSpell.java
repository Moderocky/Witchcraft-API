package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class CommandWardenSpell extends AbstractSummonSpell {
    
    public CommandWardenSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return WitchcraftAPI.minecraft.nearbySummons(caster, "warp_warden_summon") < 1 && super.canCast(caster);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0.5, 0.2, 0.5);
        WitchcraftAPI.client.particles(Particle.SOUL).playSpiral(
            location.setDirection(new Vector(0, 1, 0)),
            1.1, 3, 20, 3
        );
        WitchcraftAPI.minecraft.summonWarpWarden(caster, location);
    }
}