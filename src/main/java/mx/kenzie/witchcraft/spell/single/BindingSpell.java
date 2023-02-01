package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.entity.Projectile;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.Map;

public class BindingSpell extends AbstractProjectileSpell {

    protected transient final Color color = new Color(164, 58, 58);
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB)
        .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
        .count(0)
        .force(true);

    public BindingSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Projectile createProjectile(LivingEntity caster, float scale, double amplitude, int range) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        final Vector direction = location.getDirection().multiply(1.6);
        final Projectile projectile = this.spawnProjectile(caster, direction, 1.2F, range);
        projectile.onTick(() -> creator.drawPoof(projectile.getLocation(), 0.3, 3));
        projectile.onCollideWithEntity(entity -> {
            if (!(entity instanceof LivingEntity living)) return false;
            living.setVelocity(new Vector());
            final Location point = living.getLocation().toCenterLocation().add(0, -0.5, 0);
            final Mob mob = CustomEntityType.DEMON_TRAP.spawn(point);
            mob.setTarget(living);
            return true;
        });
        projectile.onCollide(() -> {
            world.playSound(projectile.getLocation(), Sound.BLOCK_CHAIN_PLACE, 2.0F, 1.0F);
            creator.drawPoof(projectile.getLocation(), 1, 12);
        });
        world.playSound(location, Sound.BLOCK_CHAIN_HIT, 0.8F, 1.0F);
        return projectile;
    }
}
