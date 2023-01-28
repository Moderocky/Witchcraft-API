package mx.kenzie.witchcraft.spell.casting;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public record Point(double x, double y) {
    public Point scaledBy(final double factor) {
        return new Point(this.x * factor, this.y * factor);
    }

    public double distance(final Point point) {
        return sqrt(pow(this.x - point.x, 2) + pow(this.y - point.y, 2));
    }

    public double separation(final Point point) {
        return Math.max(Math.abs(x - point.x), Math.abs(y - point.y));
    }
}
