package mx.kenzie.witchcraft.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public interface DimensionDoor extends Portal {

    @Nullable Location getOutcome();

    void setOutcome(Location location);

}
