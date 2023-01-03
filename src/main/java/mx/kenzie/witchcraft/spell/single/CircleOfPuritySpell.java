package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.Hammer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CircleOfPuritySpell extends AbstractWarhammerSpell {
    public transient final ParticleBuilder builder = new ParticleBuilder(Particle.FLAME)
        .count(1).force(true);
    
    public CircleOfPuritySpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final LivingEntity entity = this.summonHammer(caster, range);
        final Hammer hammer = WitchcraftAPI.minecraft.getHandle(entity);
        hammer.setMinorTickConsumer(thing -> {
            for (int i = 0; i < 4; i++) {
                final Location point = getRandom(thing.getLocation());
                if (!thing.hasLineOfSight(point)) continue;
                this.builder.location(point).spawn();
                break;
            }
        });
        hammer.setMajorTickConsumer(thing -> {
            for (final Entity found : thing.getNearbyEntities(10, 5, 10)) {
                WitchcraftAPI.minecraft.damageEntitySafely(found, caster, 0.2 + amplitude, EntityDamageEvent.DamageCause.MAGIC);
            }
        });
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, entity::remove, 20 * 30L);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    public static Location getRandom(Location centre) {
        final Random random = ThreadLocalRandom.current();
        return centre.add((random.nextDouble() - 0.5) * 20, (random.nextDouble() - 0.2) * 8, (random.nextDouble() - 0.5) * 20);
    }
    
}
