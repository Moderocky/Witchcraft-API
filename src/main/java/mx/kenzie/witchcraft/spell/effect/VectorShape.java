package mx.kenzie.witchcraft.spell.effect;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public interface VectorShape extends List<Vector> {
    @Override
    int size();
    
    List<Vector> getVectors();
    
    void draw(Location location);
    
    ParticleBuilder builder();
}
