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
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

import java.awt.*;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class WitherAuraSpell extends AbstractWardSpell {
    protected transient final Color color = new Color(20, 18, 21);
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB)
        .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
        .count(0)
        .force(true);

    public WitherAuraSpell(Map<String, Object> map) {
        super(map);
    }

    public static Location getRandom(Location centre) {
        final Random random = ThreadLocalRandom.current();
        return centre.add((random.nextDouble() - 0.5) * 20, (random.nextDouble() - 0.2) * 8, (random.nextDouble() - 0.5) * 20);
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Random random = ThreadLocalRandom.current();
        final int lifetime = 20 * 30;
        final WardCube entity = this.summonWard(caster, lifetime);
        final Totem cube = WitchcraftAPI.minecraft.getHandle(entity);
        final ParticleBuilder ticker = new ParticleBuilder(Particle.SPELL_MOB)
            .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
            .count(0)
            .force(true);
        final ParticleCreator creator = WitchcraftAPI.client.particles(ticker);
        cube.setMajorTickConsumer(thing -> {
            thing.getWorld().playSound(thing.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.4F, 0.2F);
            for (final Entity found : this.getAffected(caster, entity, true)) {
                WitchcraftAPI.minecraft.damageEntitySafely(found, caster, 0.5 + amplitude, EntityDamageEvent.DamageCause.WITHER);
                for (int i = 0; i < 3; i++) {
                    final Location point = found.getLocation();
                    point.add((random.nextDouble() - 0.5), 0.2, (random.nextDouble() - 0.5));
                    this.builder.location(point).spawn();
                }
            }
            final Location centre = thing.getEyeLocation();
            WitchcraftAPI.executor.submit(() -> this.drawCircle(creator, centre));
        });
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, entity::remove, 20 * 30L);
    }

}
