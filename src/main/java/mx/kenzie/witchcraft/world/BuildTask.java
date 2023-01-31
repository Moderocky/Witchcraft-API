package mx.kenzie.witchcraft.world;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import java.util.function.Consumer;

public interface BuildTask extends Cloneable, Task {

    boolean tick(Consumer<Step> consumer);

    Step peek();

    BuildTask clone();

    BuildTask clone(Location location);

    @Override
    String toString();

    record Step(BlockData data, Location location) {
    }

}
