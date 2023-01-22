package mx.kenzie.witchcraft.spell.single;

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

public class BaneSpell extends AbstractProjectileSpell {
    public BaneSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public Projectile createProjectile(LivingEntity caster, float scale, double amplitude, int range) {
        return this.createProjectile(caster, caster.getLocation().getDirection()
            .multiply(0.6), scale, amplitude, range);
    }
    
    public Projectile createProjectile(LivingEntity caster, Vector direction, float scale, double amplitude, int range) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = amplitude + 4;
        final AbstractClientArmorStand skull = (AbstractClientArmorStand) WitchcraftAPI.client.create(EntityType.ARMOR_STAND);
        skull.invisible = true;
        skull.basePlate = false;
        skull.marker = true;
        skull.small = true;
        skull.headPose[0] = location.getPitch();
        final ParticleCreator creator = ParticleCreator.of(Particle.DRAGON_BREATH.builder().count(0));
        final Projectile projectile = this.spawnProjectile(caster, direction, 1.2F, range);
        projectile.setDamage(damage);
        projectile.onTick(() -> {
            skull.velocity(direction);
            skull.move(projectile.getPotentialNext().add(0, -1.6, 0));
            final Location start = projectile.getPrevious();
            creator.drawPoof(start, 0.25, 4);
        });
        projectile.onCollide(() -> {
            skull.remove();
            world.playSound(projectile.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 0.6F, 0.8F);
            creator.drawPoof(projectile.getLocation(), 1, 20);
        });
        world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 0.75F, 0.9F);
        Location loc = WitchcraftAPI.minecraft.getRelative(projectile.getPotentialNext(), 90, 0, 0.36);
        skull.setLocation(loc.add(0, -1.2, 0).setDirection(direction));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() != location.getWorld()) continue;
            if (player.getLocation().distanceSquared(location) < 120 * 120)
                skull.show(player);
        }
        skull.updateMetadata();
        skull.setHelmet(ItemArchetype.of("darkfire_eye").create());
        return projectile;
    }
    
}
