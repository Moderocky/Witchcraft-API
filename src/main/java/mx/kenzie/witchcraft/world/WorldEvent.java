package mx.kenzie.witchcraft.world;

import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class WorldEvent implements Keyed {
    
    protected final Location location;
    protected final Type type;
    protected final long start;
    protected @NotNull Status status;
    
    protected WorldEvent(Type type, Location location) {
        this.location = location;
        this.type = type;
        this.start = System.currentTimeMillis();
        this.status = Status.STARTING;
    }
    
    public @NotNull Status getStatus() {
        return status;
    }
    
    public abstract @NotNull NamespacedKey getKey();
    
    public boolean isFinished() {
        return switch (status) {
            case ONGOING, STARTING -> false;
            default -> true;
        };
    }
    
    public long getStart() {
        return start;
    }
    
    public abstract BossBar getBossBar();
    
    public abstract void tick();
    
    public abstract void updateInvolvement();
    
    public boolean isInvolved(Player player) {
        return this.getInvolvedPlayers().contains(player);
    }
    
    public abstract Collection<Player> getInvolvedPlayers();
    
    public boolean isInvolved(Entity entity) {
        return this.getInvolvedEntities().contains(entity);
    }
    
    public abstract Collection<Entity> getInvolvedEntities();
    
    public Location getLocation() {
        return location;
    }
    
    public Type getType() {
        return type;
    }
    
    public enum Status {
        ONGOING,
        VICTORY,
        LOSS,
        STOPPED,
        STARTING
    }
    
    public enum Type {
        DEMON_INVASION
    }
}
