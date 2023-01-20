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
import org.bukkit.util.Vector;

import java.util.Map;

public class StrikeSpell extends AbstractProjectileSpell {
    private transient final ParticleBuilder builder = new ParticleBuilder(Particle.WHITE_ASH)
        .count(3).offset(0.1, 0.1, 0.1).force(true);
    
    public StrikeSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public Projectile createProjectile(LivingEntity caster, float scale, double amplitude, int range) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = 1 + amplitude;
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        final Vector direction = location.getDirection().multiply(0.9);
        final Projectile projectile = this.spawnProjectile(caster, direction, 0.8F, range);
        world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 0.6F, 1.2F);
        projectile.setDamage(damage);
        projectile.onTick(() -> builder.location(projectile.getLocation()).spawn());
        projectile.onCollide(() -> {
            world.playSound(projectile.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.8F, 0.4F);
            creator.drawPoof(projectile.getLocation(), 0.5, 6);
        });
        return projectile;
    }
}
