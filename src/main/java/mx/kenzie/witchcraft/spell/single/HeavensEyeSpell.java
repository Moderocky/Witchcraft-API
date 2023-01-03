package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.Totem;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class HeavensEyeSpell extends AbstractWardSpell {
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.ELECTRIC_SPARK)
        .count(0)
        .force(true);
    
    public HeavensEyeSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Random random = ThreadLocalRandom.current();
        final Location spawn = caster.getLocation().getBlock().getLocation().add(0.5, 3, 0.5);
        final LivingEntity entity = WitchcraftAPI.minecraft.summonWardCube(caster, spawn);
        final Totem cube = WitchcraftAPI.minecraft.getHandle(entity);
        final ParticleBuilder ticker = new ParticleBuilder(Particle.ELECTRIC_SPARK)
            .count(0)
            .force(true);
        final ParticleCreator creator = WitchcraftAPI.client.particles(ticker);
        final Location centre = caster.getEyeLocation();
        cube.setMajorTickConsumer(thing -> {
            WitchcraftAPI.executor.submit(() -> {
                final int particles = 80;
                final VectorShape circle = creator.createCircle(new Vector(0, -1, 0), 10, particles);
                for (Vector vector : circle) {
                    final Location point = centre.clone().add(vector);
                    this.builder.location(point).spawn();
                    try {
                        Thread.sleep(4000 / particles); // 4 rings drawing at a time
                    } catch (InterruptedException ignored) {}
                }
            });
            final List<Entity> list = new ArrayList<>(centre.getNearbyLivingEntities(10, 5));
            list.removeIf(found -> {
                if (thing == found) return true;
                return WitchcraftAPI.minecraft.isAlly(found, caster);
            });
            if (list.isEmpty()) return;
            final Entity target = list.get(random.nextInt(list.size()));
            if (!(target instanceof LivingEntity living)) return;
            creator.drawLightning(thing.getEyeLocation(), living.getEyeLocation(), 0.15);
            WitchcraftAPI.minecraft.damageEntitySafely(target, caster, 0.5 + amplitude, EntityDamageEvent.DamageCause.MAGIC);
        });
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, entity::remove, 20 * 30L);
    }
    
}
