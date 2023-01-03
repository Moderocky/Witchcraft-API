package mx.kenzie.witchcraft.spell.effect;

import org.bukkit.Location;

import java.util.List;

public interface LocatedShape extends VectorShape {
    
    List<Location> getLocations();
    
}
