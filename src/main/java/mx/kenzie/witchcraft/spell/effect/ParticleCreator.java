package mx.kenzie.witchcraft.spell.effect;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public interface ParticleCreator {
    
    static Vector random() {
        final Random random = ThreadLocalRandom.current();
        return new Vector((random.nextDouble() - 0.5), (random.nextDouble() - 0.5), (random.nextDouble() - 0.5));
    }
    
    ParticleBuilder getBuilder();
    
    double drawLine(Location start, Location end, double spread);
    
    void drawPoof(Location start, double radius, int particles);
    
    VectorShape createPoof(double radius, int particles);
    
    VectorShape createPoof(double radiusX, double radiusY, int particles);
    
    void drawLightning(Location start, Location end, double spread);
    
    VectorShape createLightning(Location start, Location end, double spread);
    
    void draw(Location location, VectorShape shape);
    
    Polygon createPolygon(Vector axis, double radius, int vertices);
    
    VectorShape createLightning(Vector direction, double distance, double spread);
    
    VectorShape createLightning(Vector start, Vector direction, double distance, double spread);
    
    void drawPlate(Location start, double radius, int particles);
    
    VectorShape createPlate(Vector axis, double radius, int particles);
    
    LocatedShape createSpiral(Location location, double startRadius, double endRadius, double height, int horizontalPrecision, int verticalPrecision);
    
    LocatedShape createSpiral(Location location, double radius, double height, int horizontalPrecision, int verticalPrecision);
    
    void playSpiral(Location location, double radius, double height, int horizontalPrecision, int verticalPrecision);
    
    VectorShape createCircle(Vector axis, double radius, int particles);
    
    Vector getNormal(Vector axis);
    
    VectorShape createSpiral(Vector axis, double length, double radius, int rings, int density);
    
    LocatedShape createSpiral(Location location, Vector axis, double startRadius, double endRadius, int particles);
    
    VectorShape createLine(Location start, Location end, double spread);
    
    VectorShape createLine(Vector direction, double distance, double spread);
    
    VectorShape createLine(Vector start, Vector direction, double distance, double spread);
    
    VectorShape createLine(Vector start, Vector end, double spread);
    
    VectorShape createSphere(double radius, int density);
    
}
