package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import net.kyori.adventure.sound.Sound;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

import static net.kyori.adventure.sound.Sound.Source;
import static net.kyori.adventure.sound.Sound.sound;
import static org.bukkit.Particle.FLAME;
import static org.bukkit.Sound.ENTITY_GENERIC_EXPLODE;

public class IncinerateSpell extends StandardSpell {
    public IncinerateSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final World world = caster.getWorld();
        final Location targetLocation;
        final Entity targetEntity;
        computeTarget:
        {
            final Location start = caster.getEyeLocation();
            final Vector direction = caster.getLocation().getDirection();
            final RayTraceResult result = world.rayTrace(start, direction, range,
                FluidCollisionMode.NEVER, true, 0.5, entity -> !entity.equals(caster));
            if (result != null) {
                targetEntity = result.getHitEntity();
                targetLocation = result.getHitPosition().toLocation(world);
                break computeTarget;
            }
            return;
        }
        this.explode(caster, targetLocation, scale);
        if (targetEntity != null) {
            targetEntity.setFireTicks((int) (targetEntity.getFireTicks() + 60 * scale));
        }
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    private void explode(Entity caster, final Location target, float scale) {
        target.getWorld().createExplosion(target, 3 * scale, true, false, caster);
        final Sound sound = sound().type(ENTITY_GENERIC_EXPLODE)
            .volume(4 * scale).pitch(0.5F / scale).source(Source.AMBIENT)
            .build();
        final AtomicInteger count = new AtomicInteger();
        final ParticleBuilder builder = FLAME.builder().count(0);
        CompletableFuture.runAsync(() -> {
            while (count.getAndIncrement() < 8) {
                final var sphere = WitchcraftAPI.client.particles(Particle.FIREWORKS_SPARK)
                    .createSphere(count.get() * scale,
                        (int) (count.get() * count.get() * count.get() * scale)
                    );
                
                for (final Vector vector : sphere.getVectors()) {
                    final Vector velocity = vector.clone().normalize().multiply(Math.random() / 2.0 + 0.5);
                    final double x = velocity.getX(), y = velocity.getY(), z = velocity.getZ();
                    
                    builder.location(target.clone().add(vector)).extra(4 / (double) count.get()).offset(x, y, z)
                        .spawn();
                }
                
                target.getWorld().playSound(sound, target.getX(), target.getY(), target.getZ());
                LockSupport.parkNanos(Duration.ofMillis(30).toNanos());
            }
        });
    }
}
