package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.entity.client.AbstractClientArmorStand;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import mx.kenzie.witchcraft.spell.projectile.MagicProjectile;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FireballSpell extends AbstractProjectileSpell {
    public FireballSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public AbstractProjectile createProjectile(LivingEntity caster, float scale, double amplitude) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final Vector direction = location.getDirection().normalize().multiply(0.5);
        final double damage = amplitude + 3;
        final AbstractClientArmorStand skull = (AbstractClientArmorStand) WitchcraftAPI.client.create(EntityType.ARMOR_STAND);
        final Random random = ThreadLocalRandom.current();
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
        final ParticleBuilder lava = new ParticleBuilder(Particle.LAVA)
            .location(location)
            .count(0)
            .force(true);
        return new MagicProjectile(caster, location, damage) {
            
            @Override
            public void onTick() {
                skull.velocity(direction);
                skull.move(getPotentialNext().add(0, -1, 0));
                final Location start = getPrevious();
                final VectorShape line = creator.createLine(start, getLocation(), 0.4);
                this.drawLine(lava, start, getLocation(), 0.8);
                for (Vector vector : line) {
                    vector.add(new Vector((random.nextDouble() - 0.5) * 0.3, (random.nextDouble() - 0.5) * 0.3, (random.nextDouble() - 0.5) * 0.3));
                    builder.location(start.clone().add(vector)).spawn();
                }
            }
            
            @Override
            public void onLaunch() {
                world.playSound(getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.75F, 0.9F);
                Location loc = WitchcraftAPI.minecraft.getRelative(getPotentialNext(), 90, 0, 0.36);
                skull.setLocation(loc.add(0, -1, 0));
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getLocation().distanceSquared(location) < 50 * 50)
                        skull.show(player);
                }
                skull.updateMetadata();
                skull.setHelmet(ItemArchetype.of("magma_ore").create());
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
        }.setDiameter(1.2);
    }
}
