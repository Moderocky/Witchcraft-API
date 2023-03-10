package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
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
import java.util.List;
import java.util.Map;

public class PlagueSpell extends AbstractWardSpell {
    protected transient final Color color = new Color(76, 255, 27);
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.WATER_WAKE)
        .count(0)
        .force(true);
    protected transient final ParticleBuilder bubble = new ParticleBuilder(Particle.SPELL_MOB)
        .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
        .count(0)
        .force(true);

    public PlagueSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final int lifetime = 20 * 30;
        final WardCube cube = this.summonWard(caster, lifetime);
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        final ParticleCreator bubbles = WitchcraftAPI.client.particles(bubble);
        final Location centre = caster.getEyeLocation();
        final PotionEffect effect = new PotionEffect(PotionEffectType.POISON, 20 * 3, 0, true, true, true);
        cube.setMajorTickConsumer(thing -> {
            centre.getWorld().playSound(centre, Sound.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, 0.6F, 0.5F);
            WitchcraftAPI.executor.submit(() -> this.drawCircle(creator, centre));
            final List<LivingEntity> list = this.getAffected(caster, cube, false);
            if (list.isEmpty()) return;
            for (LivingEntity living : list) {
                bubbles.drawPoof(living.getLocation(), 0.6, 8);
                living.removePotionEffect(PotionEffectType.POISON);
                living.addPotionEffect(effect);
            }
        });
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, cube::remove, lifetime);
    }

}
