package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Map;

public class ThunderboltSpell extends AbstractTargetedSpell {
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.ELECTRIC_SPARK).count(0)
        .force(true);
    
    public ThunderboltSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final AbstractTargetedSpell.Target trace = this.getTarget(caster, range);
        if (trace == null) return;
        final Location target = trace.target();
        final Entity found = trace.entity();
        final Location cloud = target.clone().add(0, 5, 0).setDirection(new Vector(0, -1, 0));
        final ParticleCreator white = WitchcraftAPI.client.particles(Particle.CLOUD.builder().count(0)),
            smoke = WitchcraftAPI.client.particles(Particle.CAMPFIRE_COSY_SMOKE.builder().count(0));
        final ParticleBuilder ticker = new ParticleBuilder(Particle.ELECTRIC_SPARK)
            .count(0)
            .force(true);
        final ParticleCreator creator = WitchcraftAPI.client.particles(ticker);
        white.createPlate(cloud.getDirection(), 3, 50).draw(cloud);
        white.createPoof(1.6, 0.2, 20).draw(cloud.clone().add(ParticleCreator.random()));
        smoke.createPoof(1.6, 0.2, 20).draw(cloud.clone().add(ParticleCreator.random()));
        if (found != null)
            WitchcraftAPI.minecraft.damageEntitySafely(found, caster, 1.8 + amplitude, EntityDamageEvent.DamageCause.MAGIC);
        cloud.getWorld().playSound(cloud, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0F, 1.1F);
        WitchcraftAPI.executor.submit(() -> {
            creator.createLightning(cloud, target, 0.15).draw(cloud);
            WitchcraftAPI.sleep(800);
            creator.createLightning(cloud, target, 0.15).draw(cloud);
        });
    }
}
