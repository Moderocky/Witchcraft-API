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

public class RainstormSpell extends AbstractTargetedSpell {
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.ELECTRIC_SPARK).count(0)
        .force(true);
    
    public RainstormSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Target trace = this.getTarget(caster, range);
        if (trace == null) return;
        final Location target = trace.target();
        final Location cloud = target.clone().add(0, 5, 0).setDirection(new Vector(0, -1, 0));
        final ParticleCreator white = WitchcraftAPI.client.particles(Particle.CLOUD.builder()
            .count(0)), smoke = WitchcraftAPI.client.particles(Particle.CAMPFIRE_COSY_SMOKE.builder().count(0));
        final ParticleBuilder ticker = new ParticleBuilder(Particle.FALLING_DRIPSTONE_WATER).count(0).force(true);
        final ParticleCreator creator = WitchcraftAPI.client.particles(ticker);
        white.createPoof(3, 0.2, 40).draw(cloud);
        white.createPoof(2, 0.3, 20).draw(cloud);
        smoke.createPoof(1.8, 0.2, 20).draw(cloud);
        for (Entity entity : target.getNearbyEntities(3, 2, 3))
            WitchcraftAPI.minecraft.damageEntitySafely(entity, caster, 1.8 + amplitude, EntityDamageEvent.DamageCause.MAGIC);
        cloud.getWorld().playSound(cloud, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.6F, 0.6F);
        WitchcraftAPI.executor.submit(() -> {
            for (int i = 0; i < 6; i++) {
                creator.createPoof(3, 0.5, 10).draw(cloud);
                WitchcraftAPI.sleep(50);
            }
        });
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
}
