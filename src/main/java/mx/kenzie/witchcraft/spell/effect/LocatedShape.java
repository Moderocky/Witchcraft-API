package mx.kenzie.witchcraft.spell.effect;

import mx.kenzie.witchcraft.WitchcraftAPI;
import org.bukkit.Location;

import java.util.List;

public interface LocatedShape extends VectorShape {

    default void draw(long delay) {
        WitchcraftAPI.executor.submit(() -> {
            for (Location point : this.getLocations()) {
                this.builder().location(point).spawn();
                WitchcraftAPI.sleep(delay);
            }
        });
    }

    List<Location> getLocations();
}
