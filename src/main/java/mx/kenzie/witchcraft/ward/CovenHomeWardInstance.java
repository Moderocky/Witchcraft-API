package mx.kenzie.witchcraft.ward;

import mx.kenzie.witchcraft.data.Coven;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CovenHomeWardInstance extends AreaWardInstance {

    protected @NotNull UUID coven_id;
    protected @NotNull WardType type;
    protected transient Coven coven;

    protected CovenHomeWardInstance(@NotNull WardType type, Coven coven, long expiry) {
        super(expiry);
        this.type = type;
        this.coven = coven;
        this.coven_id = coven.getId();
    }

    protected CovenHomeWardInstance() {
        super();
        type = WardType.NOTIFY;
        coven_id = new UUID(0, 0);
    }

    public @NotNull WardType getType() {
        return type;
    }

    @Override
    public boolean isOwner(LivingEntity entity) {
        return this.getCoven().isOwner(entity);
    }

    public Coven getCoven() {
        if (coven == null) coven = Coven.getCoven(coven_id);
        return coven;
    }

    @Override
    public boolean permits(LivingEntity entity) {
        return this.getCoven().isMember(entity);
    }

    @Override
    public boolean canBreak(LivingEntity entity) {
        if (type != WardType.PROTECT_TERRAIN) return true;
        return this.permits(entity);
    }

    @Override
    public boolean canHurt(LivingEntity entity, Entity target) {
        if (type != WardType.PROTECT_MEMBERS) return true;
        if (this.permits(entity)) return true;
        if (!(target instanceof LivingEntity living)) return true;
        return (!this.permits(living));
    }

    @Override
    public boolean canTeleport(LivingEntity entity) {
        if (type != WardType.BLOCK_TELEPORT) return true;
        return this.permits(entity);
    }
}
