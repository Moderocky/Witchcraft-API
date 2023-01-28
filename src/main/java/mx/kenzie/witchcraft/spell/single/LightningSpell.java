package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.entity.Projectile;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class LightningSpell extends AbstractProjectileSpell {
    public LightningSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Projectile createProjectile(LivingEntity caster, float scale, double amplitude, int range) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = amplitude + 3;
        final ParticleBuilder builder = new ParticleBuilder(Particle.ELECTRIC_SPARK)
            .count(0)
            .force(true);
        final ParticleCreator creator = ParticleCreator.of(builder);
        final Location[] prev = {null};
        final Vector direction = caster.getLocation().getDirection().multiply(1.2);
        final Projectile projectile = this.spawnProjectile(caster, direction, 1.0F, range);
        projectile.setDamage(damage);
        projectile.onTick(() -> {
            final Location previous = prev[0],
                here = projectile.getLocation().clone().add(ParticleCreator.random());
            if (previous != null) creator.drawLine(previous, here, 0.2);
            prev[0] = here;
        });
        projectile.onCollide(() -> {
            final Location end = projectile.getLocation();
            final ParticleBuilder thing = new ParticleBuilder(Particle.END_ROD).count(1).force(true).location(end);
            for (int i = 0; i < 10; i++) thing.spawn();
            end.getWorld().playSound(end, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.2F, 1.5F);
        });
        world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 0.6F, 1.4F);
        return projectile;
    }

}
