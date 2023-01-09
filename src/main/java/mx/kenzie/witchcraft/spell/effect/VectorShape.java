package mx.kenzie.witchcraft.spell.effect;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public interface VectorShape extends List<Vector> {
    VectorShape rotate(Vector axis, double degrees);
    
    @Override
    int size();
    
    List<Vector> getVectors();
    
    void draw(Location location);
    
    default void draw(Location location, long delay) {
        WitchcraftAPI.executor.submit(() -> {
            for (Vector vector : this.getVectors()) {
                final Location point = location.clone().add(vector);
                this.builder().location(point).spawn();
                WitchcraftAPI.sleep(delay);
            }
        });
    }
    
    ParticleBuilder builder();
}
