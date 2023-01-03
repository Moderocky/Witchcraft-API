package mx.kenzie.witchcraft.spell.effect;

import org.bukkit.Location;

public interface VisualEffect {
    
    void play(Location location);
    
    void draw(Location start, Location end);
    
}
