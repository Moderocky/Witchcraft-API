package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.Hammer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CircleOfDefianceSpell extends AbstractWarhammerSpell {
    public transient final ParticleBuilder builder = new ParticleBuilder(Particle.CAMPFIRE_COSY_SMOKE)
        .count(1).force(true);

    public CircleOfDefianceSpell(Map<String, Object> map) {
        super(map);
    }

    public static Location getRandom(Location centre) {
        final Random random = ThreadLocalRandom.current();
        return centre.add((random.nextDouble() - 0.5) * 20, (random.nextDouble() - 0.2) * 8, (random.nextDouble() - 0.5) * 20);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final int lifetime = 20 * 30;
        final Hammer hammer = this.summonHammer(caster, range);
        final PotionEffect effect = new PotionEffect(PotionEffectType.ABSORPTION, lifetime, (int) (2 + amplitude), true, false, false);
        if (!caster.hasPotionEffect(PotionEffectType.ABSORPTION)) caster.addPotionEffect(effect);
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
                if (found == thing) continue;
                if (!(found instanceof LivingEntity living)) continue;
                if (living.hasPotionEffect(PotionEffectType.ABSORPTION)) continue;
                if (!WitchcraftAPI.minecraft.isAlly(found, caster)) continue;
                living.addPotionEffect(effect);
            }
        });
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, hammer::remove, lifetime);
    }

}
