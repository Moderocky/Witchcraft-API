package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.Projectile;
import mx.kenzie.witchcraft.entity.client.AbstractClientArmorStand;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.awt.Color;
import java.util.Map;

public class WitherBlastSpell extends AbstractProjectileSpell {
    public WitherBlastSpell(Map<String, Object> map) {
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
        final Color color = new Color(20, 18, 21);
        final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB)
            .location(location)
            .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
            .count(0)
            .force(true);
        final ParticleCreator creator = ParticleCreator.of(builder);
        final Projectile projectile = this.spawnProjectile(caster, direction, 1.2F, range);
        projectile.setDamage(damage);
        projectile.onTick(() -> {
            skull.velocity(direction);
            skull.move(projectile.getPotentialNext().add(0, -1.2, 0));
            final Location start = projectile.getPrevious();
            creator.drawPoof(start, 0.4, 4);
        });
        projectile.onCollide(() -> {
            skull.remove();
            if (projectile.getShooter() != null)
                projectile.getLocation().createExplosion(projectile.getShooter(), 1.5F * scale, false, false);
            projectile.getLocation().createExplosion(1.5F * scale, false, false);
        });
        world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 0.75F, 0.9F);
        Location loc = WitchcraftAPI.minecraft.getRelative(projectile.getPotentialNext(), 90, 0, 0.36);
        skull.setLocation(loc.add(0, -1.2, 0));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distanceSquared(location) < 50 * 50)
                skull.show(player);
        }
        skull.updateMetadata();
        skull.setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
        return projectile;
    }
}
