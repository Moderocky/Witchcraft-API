package mx.kenzie.witchcraft.ward;

import mx.kenzie.fern.Fern;
import org.bukkit.Location;

import java.io.OutputStream;
import java.util.UUID;

public class AreaWardInstance extends WardInstance {
    protected UUID world;
    protected int x1, y1, z1, x2, y2, z2;

    protected AreaWardInstance(long expiry) {
        super(expiry);
    }

    protected AreaWardInstance() {
        super(0);
    }

    @Override
    public UUID getWorld() {
        return world;
    }

    @Override
    public boolean includes(Location location) {
        if (location == null) return false;
        if (location.getWorld() != null && location.getWorld().getUID() != world) return false;
        final double x = location.getX(), y = location.getY(), z = location.getZ();
        if (x < x1 || x > x2) return false;
        if (y < y1 || y > y2) return false;
        return !(z < z1) && !(z > z2);
    }

    public void save(OutputStream stream) {
        final Fern fern = new Fern(null, stream);
        fern.write(this);
    }
}
