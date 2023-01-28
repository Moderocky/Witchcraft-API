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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.util.Map;

public class GravityBubbleSpell extends AbstractWardSpell {
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.SOUL_FIRE_FLAME)
        .count(0)
        .force(true);

    protected transient final Color color = new Color(27, 160, 255);
    protected transient final ParticleBuilder bubble = new ParticleBuilder(Particle.SPELL_MOB)
        .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
        .count(0)
        .force(true);

    public GravityBubbleSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final int lifetime = 20 * 30;
        final WardCube entity = this.summonWard(caster, lifetime);
        final Totem cube = WitchcraftAPI.minecraft.getHandle(entity);
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        final ParticleCreator bubbles = WitchcraftAPI.client.particles(bubble);
        final Location centre = caster.getEyeLocation();
        final PotionEffect effect = new PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 3, 1, true, false, false);
        final PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, 20 * 3, 1, true, false, false);
        cube.setMajorTickConsumer(thing -> {
            centre.getWorld().playSound(centre, Sound.BLOCK_BEACON_AMBIENT, 0.7F, 0.7F);
            WitchcraftAPI.executor.submit(() -> this.drawCircle(creator, centre));
            for (LivingEntity living : this.getAffected(caster, entity, true)) {
                bubbles.drawPoof(living.getLocation(), 0.6, 8);
                living.removePotionEffect(PotionEffectType.SLOW_FALLING);
                living.addPotionEffect(effect);
                living.removePotionEffect(PotionEffectType.JUMP);
                living.addPotionEffect(jump);
            }
        });
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, entity::remove, lifetime);
    }

}
