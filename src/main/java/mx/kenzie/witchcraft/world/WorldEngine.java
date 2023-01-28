package mx.kenzie.witchcraft.world;

import org.bukkit.Keyed;
import org.bukkit.World;

public interface WorldEngine extends Keyed {
    World.Environment environment();

    String name();
}
