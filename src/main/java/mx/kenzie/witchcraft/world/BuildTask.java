package mx.kenzie.witchcraft.world;

import mx.kenzie.witchcraft.Minecraft;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.structure.Structure;

import java.util.function.Consumer;

public interface BuildTask extends Cloneable, Task {

    static BuildTask of(Structure structure, Location location) {
        return Minecraft.getInstance().createBuildTask(structure, location);
    }

    boolean tick(Consumer<Step> consumer);

    Step peek();

    BuildTask clone();

    BuildTask clone(Location location);

    @Override
    String toString();

    record Step(BlockData data, Location location) {
    }

}
