package mx.kenzie.witchcraft.world;

import org.bukkit.World;

public interface SpawnTask {

    int tick(World world, boolean monsters, boolean animals);

}
