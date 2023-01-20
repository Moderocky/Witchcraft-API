package mx.kenzie.witchcraft.ward;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.Coven;
import mx.kenzie.witchcraft.entity.Summon;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SimpleWardInstance extends WardInstance {
    
    protected transient final Entity owner, ward;
    
    public SimpleWardInstance(Entity owner, @NotNull Entity ward, int lifetime) {
        super(lifetime > 0 ? System.currentTimeMillis() + (lifetime * 50L) : lifetime);
        this.owner = owner;
        this.ward = ward;
    }
    
    @Override
    public int hashCode() {
        return ward.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof SimpleWardInstance instance && instance.ward == this.ward);
    }
    
    @Override
    public boolean isOwner(LivingEntity entity) {
        if (owner == null) return false;
        return (owner.equals(entity));
    }
    
    
    @Override
    public boolean permits(LivingEntity entity) {
        if (this.isOwner(entity)) return true;
        final Summon summon = WitchcraftAPI.minecraft.getAsSummon(entity);
        if (summon != null && summon.getBukkitOwner() == owner) return true;
        final Coven coven = Coven.getCoven(owner);
        if (coven == null) return false;
        if (coven.isMember(entity)) return true;
        if (summon == null) return false;
        return coven.isMember(summon.getOwnerID());
    }
    
    @Override
    public boolean includes(Location location) {
        if (location.getWorld() != ward.getWorld()) return false;
        return !(location.distanceSquared(ward.getLocation()) > 20 * 20);
    }
    
    @Override
    public void discard() {
        super.discard();
        if (ward == null) return;
        this.ward.remove();
    }
    
    @Override
    public UUID getWorld() {
        return ward.getWorld().getUID();
    }
    
    
}
