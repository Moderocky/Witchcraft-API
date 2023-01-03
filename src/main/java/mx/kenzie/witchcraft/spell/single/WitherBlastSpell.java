package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.client.AbstractClientArmorStand;
import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import mx.kenzie.witchcraft.spell.projectile.MagicProjectile;
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
    public AbstractProjectile createProjectile(LivingEntity caster, float scale, double amplitude) {
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
        return new MagicProjectile(caster, location, damage) {
            
            @Override
            public void onTick() {
                skull.velocity(direction);
                skull.move(getPotentialNext().add(0, -1, 0));
                drawLine(builder, getPrevious(), getLocation(), 0.2);
            }
            
            @Override
            public void onLaunch() {
                world.playSound(getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.75F, 0.9F);
                Location loc = WitchcraftAPI.minecraft.getRelative(getPotentialNext(), 90, 0, 0.36);
                skull.setLocation(loc.add(0, -1, 0));
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getLocation().distanceSquared(location) < 50 * 50)
                        skull.show(player);
                }
                skull.updateMetadata();
                skull.setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
            }
            
            @Override
            public void onRemove() {
                skull.remove();
            }
            
            @Override
            public void explode(final Location location1) {
                sync(() -> {
                    if (getSource() != null)
                        return location1.createExplosion(getSource(), 1.5F * scale, false, false);
                    return location1.createExplosion(1.5F * scale, false, false);
                });
            }
        }.setDiameter(1.0);
    }
}
