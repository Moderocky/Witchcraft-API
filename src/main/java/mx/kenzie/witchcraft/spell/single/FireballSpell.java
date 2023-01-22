package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.entity.Projectile;
import mx.kenzie.witchcraft.entity.client.AbstractClientArmorStand;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Map;

public class FireballSpell extends AbstractProjectileSpell {
    public FireballSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public Projectile createProjectile(LivingEntity caster, float scale, double amplitude, int range) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final Vector direction = location.getDirection().normalize().multiply(0.5);
        final double damage = amplitude + 3;
        final AbstractClientArmorStand skull = (AbstractClientArmorStand) WitchcraftAPI.client.create(EntityType.ARMOR_STAND);
        skull.invisible = true;
        skull.basePlate = false;
        skull.marker = true;
        skull.small = true;
        skull.headPose[0] = location.getPitch();
        final ParticleBuilder builder = new ParticleBuilder(Particle.FLAME)
            .location(location)
            .count(0)
            .extra(0.2)
            .force(true);
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        final ParticleCreator lava = WitchcraftAPI.client.particles(new ParticleBuilder(Particle.LAVA)
            .location(location)
            .count(0)
            .force(true));
        final Projectile projectile = this.spawnProjectile(caster, direction, 1.2F, range);
        projectile.setDamage(damage);
        projectile.onTick(() -> {
            skull.velocity(direction);
            skull.move(projectile.getPotentialNext().add(0, -1.4, 0));
            final Location start = projectile.getPrevious();
            creator.drawPoof(start, 0.4, 4);
            lava.drawPoof(start, 0.2, 1);
        });
        projectile.onCollide(() -> {
            skull.remove();
            if (projectile.getSource() != null)
                projectile.getLocation().createExplosion(projectile.getSource(), 1.5F * scale, false, false);
            projectile.getLocation().createExplosion(1.5F * scale, false, false);
        });
        world.playSound(projectile.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.75F, 0.9F);
        final Location next = WitchcraftAPI.minecraft.getRelative(projectile.getPotentialNext(), 90, 0, 0.36);
        skull.setLocation(next.add(0, -1.2, 0));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() != location.getWorld()) continue;
            if (player.getLocation().distanceSquared(location) < 120 * 120)
                skull.show(player);
        }
        skull.updateMetadata();
        skull.setHelmet(ItemArchetype.of("magma_ore").create());
        return projectile;
    }
}
