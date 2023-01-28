package mx.kenzie.witchcraft.entity;

import mx.kenzie.witchcraft.entity.handle.Handle;
import org.bukkit.entity.Entity;

public interface CustomEntity extends Entity {

    Handle getHandle();

    default String getTypeId() {
        return this.getKind().key;
    }

    CustomEntityType<?> getKind();

    default boolean shouldSpawnGrave() {
        return !(this instanceof Summon || this instanceof Portal || this instanceof NoSpawnGrave);
    }

}
