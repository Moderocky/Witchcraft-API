package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LightningSpell extends AbstractProjectileSpell {
    public LightningSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public AbstractProjectile createProjectile(LivingEntity caster, float scale, double amplitude) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = amplitude + 3;
        final ParticleBuilder builder = new ParticleBuilder(Particle.ELECTRIC_SPARK)
            .count(0)
            .force(true);
        final Random random = ThreadLocalRandom.current();
        final Location[] prev = {null};
        final Vector direction = caster.getLocation().getDirection().normalize().multiply(1.2);
        return new AbstractProjectile(caster, location) {
            
            double prevRandom = 0.0;
            
            @Override
            public void onLaunch() {
                world.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.7F, 2.0F);
            }
            
            @Override
            public void onTick() {
                Location previous = prev[0];
                Location location = getLocation().clone().add(random(), random(), random());
                if (previous != null)
                    drawLine(builder, previous, location, 0.2);
                prev[0] = location;
            }
            
            private double random() {
                double d = 0;
                while ((d < 0.5 && d > -0.5) || ((d * prevRandom) < -1 || (d * prevRandom) > 1)) {
                    d = ((random.nextDouble() - 0.5) * 1.4);
                }
                prevRandom = d;
                return d;
            }
            
            @Override
            public void onCollide(Entity entity) {
                if (!(entity instanceof final LivingEntity living)) return;
                if (this.hasCollided()) return;
                this.sync(() -> {
                    WitchcraftAPI.minecraft.damageEntity(living, getSource(), damage, EntityDamageEvent.DamageCause.MAGIC);
                    return true;
                });
                explode(this.getLocation());
            }
            
            @Override
            public void onCollide(Block block) {
                explode(getLocation());
            }
        }.setDiameter(0.9).setMotion(direction);
    }
    
    private void explode(final Location location) {
        final ParticleBuilder builder = new ParticleBuilder(Particle.END_ROD)
            .count(1)
            .force(true)
            .location(location);
        for (int i = 0; i < 10; i++) {
            builder.spawn();
        }
        location.getWorld().playSound(location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.2F, 1.5F);
    }
}
