package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.Projectile;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.Map;

public class ShadowbeamSpell extends AbstractProjectileSpell {

    protected transient final Color color = new Color(178, 0, 169);
    protected transient final ParticleCreator creator = ParticleCreator.of(Particle.REDSTONE.builder().count(0)
        .force(true)).color(color);

    public ShadowbeamSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Projectile createProjectile(LivingEntity caster, float scale, double amplitude, int range) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = 3 + amplitude;
        final Vector direction = location.getDirection().multiply(2.2);
        final Projectile projectile = this.spawnProjectile(caster, direction, 1.1F, 35 + range);
        projectile.setDamage(damage);
        projectile.onCollideWithBlock((block, face) -> {
            final Vector normal = new Vector(face.getModX(), face.getModY(), face.getModZ());
            final Vector direct = projectile.getVelocity();
            final double speed = direct.length();
            double dot = direct.dot(normal);
            final Vector reflection = normal.clone().multiply(2).multiply(dot);
            final Vector result = direct.clone().subtract(reflection).normalize().multiply(speed);
            projectile.setVelocity(result);
            return false;
        });
        projectile.onTick(() -> creator.drawLine(projectile.getPrevious(), projectile.getLocation(), 0.2));
        projectile.onCollide(() -> {
            projectile.getWorld().playSound(projectile.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 0.4F, 0.2F);
            final Location here = projectile.getLocation();
            final Vector vector = projectile.getVelocity();
            WitchcraftAPI.executor.submit(() -> {
                for (int i = 1; i < 4; i++) {
                    final double radius = (i * 3.0) / 10;
                    this.creator.createCircle(vector, radius, i * 12).draw(here);
                    WitchcraftAPI.sleep(120);
                }
            });
        });
        world.playSound(location, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 0.4F, 2.0F);
        return projectile;
    }
}
