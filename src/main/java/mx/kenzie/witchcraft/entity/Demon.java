package mx.kenzie.witchcraft.entity;

import mx.kenzie.witchcraft.data.WarlockDeity;

import java.util.UUID;

public interface Demon extends Human, Owned, Active {

    WarlockDeity getDeity();

    void setDeity(WarlockDeity deity);

    UUID getOwnerID();

    void setOwner(org.bukkit.entity.Entity entity);

}
