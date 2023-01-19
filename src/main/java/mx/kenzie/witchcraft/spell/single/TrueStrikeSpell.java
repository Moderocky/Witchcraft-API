package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.entity.Projectile;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class TrueStrikeSpell extends AbstractProjectileSpell {
    public TrueStrikeSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public Projectile createProjectile(LivingEntity caster, float scale, double amplitude, int range) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = 1 + amplitude;
        final Vector direction = location.getDirection().multiply(0.8);
        final ParticleBuilder builder = Particle.BLOCK_CRACK.builder().data(Material.ICE.createBlockData()).count(0);
        final Projectile projectile = this.spawnProjectile(caster, direction, 0.5F, range);
        world.playSound(location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.6F, 0.8F);
        projectile.setDamage(damage);
        projectile.onTick(() -> world.spawnParticle(Particle.BLOCK_CRACK, projectile.getLocation(), 0, Material.ICE.createBlockData()));
        projectile.onCollide(() -> {
            world.playSound(projectile.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.8F, 0.4F);
            ParticleCreator.of(builder).drawPoof(projectile.getLocation(), 0.5, 6);
        });
        return projectile;
    }
}
