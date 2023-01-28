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

public class SpiritRaySpell extends AbstractProjectileSpell {
    private transient final ParticleBuilder builder = new ParticleBuilder(Particle.DRIPPING_OBSIDIAN_TEAR)
        .force(true)
        .count(0);

    public SpiritRaySpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Projectile createProjectile(LivingEntity caster, float scale, double amplitude, int range) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = 4 + amplitude;
        final Vector direction = location.getDirection().multiply(2.0);
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        final Projectile projectile = this.spawnProjectile(caster, direction, 0.8F, range);
        projectile.setDamage(damage);
        LightRaySpell.drawSpiralPart(world, creator, projectile);
        world.playSound(caster.getEyeLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.8F, 1.2F);
        return projectile;
    }
}
