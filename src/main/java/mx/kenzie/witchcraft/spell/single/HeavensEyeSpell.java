package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.Totem;
import mx.kenzie.witchcraft.entity.WardCube;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

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
        final int lifetime = 20 * 30;
        final WardCube entity = this.summonWard(caster, lifetime);
        final Totem cube = WitchcraftAPI.minecraft.getHandle(entity);
        final ParticleBuilder ticker = new ParticleBuilder(Particle.ELECTRIC_SPARK)
            .count(0)
            .force(true);
        final ParticleCreator creator = WitchcraftAPI.client.particles(ticker);
        final Location centre = caster.getEyeLocation();
        cube.setMajorTickConsumer(thing -> {
            centre.getWorld().playSound(centre, Sound.BLOCK_BEACON_AMBIENT, 0.7F, 1.8F);
            WitchcraftAPI.executor.submit(() -> this.drawCircle(creator, centre));
            final List<LivingEntity> list = this.getAffected(caster, entity, false);
            if (list.isEmpty()) return;
            final LivingEntity target = list.get(random.nextInt(list.size()));
            creator.drawLightning(thing.getEyeLocation(), target.getEyeLocation(), 0.15);
            WitchcraftAPI.minecraft.damageEntitySafely(target, caster, 0.5 + amplitude, EntityDamageEvent.DamageCause.MAGIC);
        });
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, entity::remove, lifetime);
    }
    
}
