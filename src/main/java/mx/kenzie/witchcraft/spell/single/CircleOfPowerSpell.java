package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.modifier.Modifier;
import mx.kenzie.witchcraft.entity.Hammer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CircleOfPowerSpell extends AbstractWarhammerSpell {
    public transient final ParticleBuilder builder = new ParticleBuilder(Particle.CRIT_MAGIC)
        .count(1).force(true);

    public CircleOfPowerSpell(Map<String, Object> map) {
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
        final int lifetime = 20 * 30, bonus = Math.max(1, Math.min(4, (int) (amplitude - 1)));
        final String id = "power_circle_spell";
        final long end = System.currentTimeMillis() + (lifetime * 50);
        final Modifier modifier = new Modifier(Modifier.Type.AMPLITUDE, bonus, end);
        final Hammer hammer = this.summonHammer(caster, range);
        hammer.setMinorTickConsumer(thing -> {
            for (int i = 0; i < 2; i++) {
                final Location point = getRandom(thing.getLocation());
                this.builder.location(point).spawn();
            }
        });
        hammer.setMajorTickConsumer(thing -> {
            for (final Entity found : thing.getNearbyEntities(10, 5, 10)) {
                if (found == thing) continue;
                if (!(found instanceof Player player)) continue;
                if (!WitchcraftAPI.minecraft.isAlly(found, caster)) continue;
                final PlayerData data = PlayerData.getData(player);
                data.temporary.modifiers.put(id, modifier);
            }
        });
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, hammer::remove, lifetime);
    }

}
