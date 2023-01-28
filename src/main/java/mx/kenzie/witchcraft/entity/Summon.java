package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.UUID;

public interface Summon extends CustomEntity, LivingEntity, Mob, Owned, NoSpawnGrave {

    UUID getOwnerID();

    void setOwner(UUID uuid);

    LivingEntity getOwningEntity();

    void setTarget(LivingEntity target);

}
