package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.Projectile;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.LockSupport;

public class LightRaySpell extends AbstractProjectileSpell {
    private transient final ParticleBuilder builder = new ParticleBuilder(Particle.END_ROD)
        .force(true).count(0);

    public LightRaySpell(Map<String, Object> map) {
        super(map);
    }

    static void drawSpiralPart(World world, ParticleCreator creator, Projectile projectile) {
        projectile.onTick(() -> {
            world.playSound(projectile.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 0.5F, 1.7F);
            double distance = projectile.getPrevious().distance(projectile.getLocation());
            if (distance > 5) return;
            creator.playSpiral(projectile.getPrevious(), 0.2, distance, 12, 1);
        });
    }

    @Override
    public Projectile createProjectile(LivingEntity caster, float scale, double amplitude, int range) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = 2 + amplitude;
        final Vector direction = location.getDirection().multiply(1.4);
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        final int radius = 8, radiusSq = radius * radius;
        final Projectile projectile = this.spawnProjectile(caster, direction, 0.8F, range);
        projectile.setDamage(damage);
        final CompletableFuture<Map<Location, BlockData>> updateFuture;
        final RayTraceResult result = world.rayTrace(location, direction, 60, FluidCollisionMode.NEVER, true, 0.1, projectile::shouldCollide);
        if (result != null) {
            final Block block = result.getHitPosition().toLocation(world).getBlock();
            updateFuture = CompletableFuture.supplyAsync(() -> {
                final int x = block.getX(), y = block.getY(), z = block.getZ();
                final int minX = x - radius, minY = y - radius, minZ = z - radius;
                final int maxX = x + radius, maxY = y + radius, maxZ = z + radius;
                final Map<Location, BlockData> updates = new HashMap<>();
                for (int i = minX; i <= maxX; i += 4) {
                    for (int j = minY; j <= maxY; j += 4) {
                        for (int k = minZ; k <= maxZ; k += 4) {
                            final int distanceSq = (i - x) * (i - x) + (j - y) * (j - y) + (k - z) * (k - z);
                            if (distanceSq > radiusSq) continue;
                            final Block current = world.getBlockAt(i, j, k);
                            if (!current.getType().isAir()) continue;
                            final double distance = Math.sqrt(distanceSq);
                            final int level = (int) (Math.pow(1 - distance / (double) radius, 0.33) * 15);
                            updates.put(current.getLocation(), Bukkit.createBlockData(Material.LIGHT, "[level=" + level + "]"));
                        }
                    }
                }
                return updates;
            }).exceptionally(t -> {
                t.printStackTrace();
                return Map.of();
            });
        } else updateFuture = CompletableFuture.completedFuture(Map.of());
        drawSpiralPart(world, creator, projectile);
        projectile.onCollide(() -> updateFuture.thenAcceptAsync(fake -> {
            final List<Player> players = world.getPlayers();
            for (final Player player : players) player.sendMultiBlockChange(fake);
            final ParticleBuilder ballBuilder = Particle.END_ROD.builder().count(radius / 3).force(true)
                .allPlayers()
                .location(projectile.getLocation()).offset(radius / 2.0, radius / 2.0, radius / 2.0).extra(0.01);
            final Instant end = Instant.now().plus(Duration.ofSeconds(15));
            Instant lastSound = Instant.EPOCH;
            while (Instant.now().isBefore(end)) {
                ballBuilder.spawn();
                if (!Duration.between(lastSound, Instant.now()).minus(Duration.ofSeconds(3)).isNegative()) {
                    world.playSound(projectile.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.AMBIENT, radius, (float) Math.random());
                    lastSound = Instant.now();
                }
                LockSupport.parkNanos(Duration.ofMillis(50).toNanos());
            }
            final Map<Location, BlockData> real = new HashMap<>(fake.size());
            for (final Location key : fake.keySet()) real.put(key, world.getBlockData(key));
            for (final Player player : players) player.sendMultiBlockChange(real);
        }));
        world.playSound(location, Sound.BLOCK_BEACON_ACTIVATE, 1.1F, 1.3F);
        return projectile;
    }
}
