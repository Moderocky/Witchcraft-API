package mx.kenzie.witchcraft.spell;

import mx.kenzie.witchcraft.spell.casting.Angle;
import mx.kenzie.witchcraft.spell.casting.Point;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public record Pattern(List<Point> points) {
    public Pattern {
        points = Collections.unmodifiableList(points);
    }

    public List<Angle> toAngles(final Angle origin) {
        final Pattern scaled = scaledBy(1 / scale());
        final List<Angle> angles = new ArrayList<>();
        final Location dummy = new Location(null, 0, 0, 0);
        for (final Point point : scaled.points) {
            final Vector toPoint = new Vector(-point.x(), -point.y(), 1);
            toPoint.rotateAroundX(Math.toRadians(origin.pitch()));
            toPoint.rotateAroundY(Math.toRadians((-origin.yaw())));
            dummy.setDirection(toPoint);
            angles.add(new Angle(dummy.getYaw(), dummy.getPitch()));
        }
        return angles;
    }

    public Pattern scaledBy(final double factor) {
        final ArrayList<Point> newPoints = new ArrayList<>(points);
        final ListIterator<Point> iter = points.listIterator();
        while (iter.hasNext()) {
            newPoints.set(iter.nextIndex(), iter.next().scaledBy(factor));
        }
        return new Pattern(newPoints);
    }

    public double scale() {
        if (this.points.isEmpty()) return 0;
        if (this.points.size() == 1) return 1;
        return this.points.get(1).distance(this.points.get(0));
    }

    @Override
    public String toString() {
        return "Pattern" + points;
    }

    public double deviation(final Pattern other) {
        if (this.points.size() != other.points.size()) return Double.MAX_VALUE;
        double total = 0;
        for (int i = 0, length = points.size(); i < length; ++i) {
            total += points.get(i).distance(other.points.get(i));
        }
        return total / points.size();
    }

    public Pattern scaledToMatch(final Pattern other) {
        if (points.size() < 2) return this;
        if (other.points.size() < 2) return this;
        return this.scaledBy(this.relativeScale(other));
    }

    public double relativeScale(final Pattern other) {
        if (this.scale() == 0) return 0;
        return other.scale() / this.scale();
    }
}
