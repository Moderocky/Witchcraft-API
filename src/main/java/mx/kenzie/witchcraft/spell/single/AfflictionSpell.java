package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.Projectile;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.Map;

public class AfflictionSpell extends AbstractProjectileSpell {
    
    protected transient final Color color = new Color(43, 243, 21);
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB)
        .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
        .count(0)
        .force(true);
    
    public AfflictionSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public Projectile createProjectile(LivingEntity caster, float scale, double amplitude, int range) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        final Vector direction = location.getDirection().multiply(0.9);
        final PotionEffect effect = new PotionEffect(PotionEffectType.POISON, 20 * 6, (int) Math.ceil(amplitude), false, true, true);
        final Projectile projectile = this.spawnProjectile(caster, direction, 1.2F, range);
        projectile.onTick(() -> creator.drawPoof(projectile.getLocation(), 0.5, 3));
        projectile.onCollideWithEntity(entity -> {
            world.playSound(projectile.getLocation(), Sound.ENTITY_SILVERFISH_HURT, 0.8F, 0.4F);
            if (entity instanceof LivingEntity living)
                living.addPotionEffect(effect);
            return true;
        });
        projectile.onCollide(() -> creator.drawPoof(projectile.getLocation(), 1, 12));
        world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 0.6F, 1.4F);
        return projectile;
    }
}
