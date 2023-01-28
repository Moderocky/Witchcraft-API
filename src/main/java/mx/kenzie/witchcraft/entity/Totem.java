package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.LivingEntity;

import java.util.UUID;
import java.util.function.Consumer;

public interface Totem extends Owned, NoSpawnGrave {

    void setMajorTickConsumer(Consumer<LivingEntity> consumer);

    UUID getOwnerID();

    void setOwner(UUID uuid);

    LivingEntity getOwningEntity();

    void setTarget(LivingEntity target);

}
