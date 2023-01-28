package mx.kenzie.witchcraft.spell.effect;

import org.bukkit.util.Vector;

public interface Polygon extends VectorShape {
    boolean isSimple();

    Polygon fillInLines(boolean all, double density);

    @Override
    Polygon rotate(Vector axis, double degrees);
}
