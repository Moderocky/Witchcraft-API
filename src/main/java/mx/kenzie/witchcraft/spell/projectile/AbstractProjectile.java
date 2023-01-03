package mx.kenzie.witchcraft.spell.projectile;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CollisionTraceResult;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractProjectile implements Synchroniser, IProjectile {
    
    protected final Location start;
    protected final Location location;
    protected final Entity source;
    protected CollisionTraceResult result = CollisionTraceResult.NONE;
    protected volatile boolean dead = false;
    protected Location previous;
    protected Vector motion;
    protected double maxRange = 60;
    protected int lifetime;
    protected int life = 0;
    protected volatile boolean collided = false;
    protected double diameter = 0.5;
    protected float gravity = 0.0F;
    
    public AbstractProjectile(@Nullable Entity source, Location location) {
        this.source = source;
        this.start = location.clone();
        this.previous = location.clone();
        this.location = location.clone();
        this.motion = new Vector();
        this.lifetime = -1;
    }
    
    public AbstractProjectile setGravity(float gravity) {
        this.gravity = gravity;
        return this;
    }
    
    public AbstractProjectile setLifetime(int ticks) {
        this.lifetime = ticks;
        return this;
    }
    
    public AbstractProjectile setMaxRange(double distance) {
        this.maxRange = distance;
        return this;
    }
    
    public AbstractProjectile setDiameter(double diameter) {
        this.diameter = diameter;
        return this;
    }
    
    protected CollisionTraceResult getResult() {
        return result;
    }
    
    public void tick() {
        try {
            if (lifetime > 0) {
                life++;
                if (life > lifetime) remove();
            }
            if (maxRange > 0) {
                if (start.distanceSquared(location) >= maxRange * maxRange) remove();
            }
            if (hasCollided()) remove();
            if (isDead()) return;
            result = WitchcraftAPI.minecraft.collisionCheck(this);
            if (result != CollisionTraceResult.NONE) {
                collided = true;
                remove();
            } else {
                List<Entity> entities = collideEntities();
                entities.removeIf(entity -> entity == source);
                entities.removeIf(entity -> !WitchcraftAPI.minecraft.isInteractible(entity));
                entities.removeIf(entity -> entity instanceof Player && ((Player) entity).getGameMode() == GameMode.SPECTATOR);
                if (source != null) {
                    entities.removeIf(entity -> WitchcraftAPI.minecraft.isSameVehicle(entity, source));
                }
                if (entities.size() > 0) {
                    dead = true;
                    if (!hasCollided()) {
                        onCollide(entities.get(0));
                        collided = true;
                    }
                    remove();
                }
            }
            previous = location.clone();
            location.add(motion);
            motion.add(new Vector(0, gravity * -1, 0));
            if (isDead()) return;
            onTick();
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }
    }
    
    public void remove() {
        dead = true;
        onRemove();
    }
    
    public boolean isDead() {
        return dead;
    }
    
    protected List<Entity> collideEntities() {
        List<Entity> entities = new ArrayList<>();
        final BoundingBox box = getBoundingBox();
        final BoundingBox prev = getBoundingBox(getPrevious());
        if (!box.overlaps(prev)) {
            try {
                entities = new ArrayList<>(sync(() -> getLocation().getNearbyEntities(8, 8, 8)).get());
            } catch (Throwable ignore) {}
            BoundingBox[] boxes = getBoxesBetween(getPrevious(), getLocation());
            entities.removeIf(entity -> {
                for (BoundingBox boundingBox : boxes) {
                    if (entity.getBoundingBox().overlaps(boundingBox)) return false;
                }
                return true;
            });
            return entities;
        } else {
            try {
                entities.addAll(new ArrayList<>(sync(() -> getLocation().getNearbyEntities(8, 8, 8)).get()));
                entities.removeIf(((Predicate<Entity>) entity -> entity.getBoundingBox().overlaps(box)).negate());
                return entities;
            } catch (Throwable ignore) {}
        }
        return entities;
    }
    
    protected BoundingBox getBoundingBox(Location location) {
        return BoundingBox.of(location, diameter, diameter, diameter);
    }
    
    protected BoundingBox[] getBoxesBetween(Location start, Location end) {
        final Vector direction = end.toVector()
            .subtract(start.toVector())
            .normalize()
            .multiply(diameter);
        final double distance = start.distance(end);
        final int split = (int) (distance / diameter);
        List<Location> locations = new ArrayList<>(split);
        for (int i = 0; i < split; i++) {
            locations.add(start.clone().add(direction.clone().multiply(i)));
        }
        final List<BoundingBox> boxes = new ArrayList<>(split);
        for (Location location : locations) {
            boxes.add(BoundingBox.of(location, diameter, diameter, diameter));
        }
        return boxes.toArray(new BoundingBox[0]);
    }
    
    protected double drawLine(ParticleBuilder builder, Location start, Location end, double spread) {
        final double distance = start.distance(end);
        final Vector direction = end.toVector().subtract(start.toVector());
        for (double d = 0; d <= distance; d += spread) {
            Location loc = start.clone().add(direction.clone().normalize().multiply(d));
            builder
                .location(loc)
                .spawn();
        }
        return distance;
    }
    
    @Override
    public void onLaunch() {
    
    }
    
    @Override
    public void onRemove() {
    
    }
    
    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(getLocation());
    }
    
    public Location getLocation() {
        return location.clone();
    }
    
    public Location getPrevious() {
        return previous.clone();
    }
    
    public Location getPotentialNext() {
        return getLocation().add(getMotion());
    }
    
    public Vector getMotion() {
        return motion.clone();
    }
    
    public AbstractProjectile setMotion(@NotNull Vector motion) {
        this.motion = motion;
        return this;
    }
    
    public @Nullable Entity getSource() {
        return source;
    }
    
    public boolean hasCollided() {
        return collided;
    }
    
}
