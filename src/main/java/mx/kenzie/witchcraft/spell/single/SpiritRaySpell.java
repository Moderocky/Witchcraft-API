package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
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

public class SpiritRaySpell extends AbstractProjectileSpell {
    private transient final ParticleBuilder builder = new ParticleBuilder(Particle.DRIPPING_OBSIDIAN_TEAR)
        .force(true)
        .count(0);
    
    public SpiritRaySpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public AbstractProjectile createProjectile(LivingEntity caster, float scale, double amplitude) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = 4 + amplitude;
        final Vector direction = location.getDirection().multiply(2.0);
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        return new AbstractProjectile(caster, location) {
            @Override
            public void onTick() {
                world.playSound(getLocation(), Sound.BLOCK_BEACON_AMBIENT, 0.5F, 1.7F);
                double distance = getPrevious().distance(getLocation());
                creator.playSpiral(getPrevious(), 0.2, distance, 12, 1);
            }
            
            @Override
            public void onCollide(Entity entity) {
                if (!(entity instanceof final LivingEntity living)) return;
                if (hasCollided()) return;
                sync(() -> {
                    WitchcraftAPI.minecraft.damageEntity(living, getSource(), damage, EntityDamageEvent.DamageCause.MAGIC);
                    return true;
                });
            }
            
            @Override
            public void onCollide(Block block) {
            }
            
            @Override
            public void onLaunch() {
                world.playSound(getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.8F, 1.2F);
            }
        }.setMotion(direction).setDiameter(0.8);
    }
}
