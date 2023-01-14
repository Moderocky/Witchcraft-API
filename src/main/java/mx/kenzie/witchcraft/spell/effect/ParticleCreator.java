package mx.kenzie.witchcraft.spell.effect;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public interface ParticleCreator {
    
    static ParticleCreator of(Particle particle) {
        if (WitchcraftAPI.isTest) return null;
        return WitchcraftAPI.client.particles(particle);
    }
    
    static ParticleCreator of(ParticleBuilder builder) {
        if (WitchcraftAPI.isTest) return null;
        return WitchcraftAPI.client.particles(builder);
    }
    
    static Vector random() {
        final Random random = ThreadLocalRandom.current();
        return new Vector((random.nextDouble() - 0.5), (random.nextDouble() - 0.5), (random.nextDouble() - 0.5));
    }
    
    static Vector randomHorizontal() {
        final Random random = ThreadLocalRandom.current();
        return new Vector((random.nextDouble() - 0.5), 0, (random.nextDouble() - 0.5));
    }
    
    ParticleBuilder getBuilder();
    
    double drawLine(Location start, Location end, double spread);
    
    Vector drawCurve(Location start, Location end, Vector curve, double spread);
    
    VectorShape createArc(Vector direction, double roll, double length, double height, int step);
    
    VectorShape createArc(Vector direction, double length, double height, int step);
    
    VectorShape createArc(double length, double height, int step);
    
    void drawPoof(Location start, double radius, int particles);
    
    VectorShape createPoof(double radius, int particles);
    
    VectorShape createPoof(double radiusX, double radiusY, int particles);
    
    void drawLightning(Location start, Location end, double spread);
    
    VectorShape createLightning(Location start, Location end, double spread);
    
    VectorShape createLightning(Location start, Location end, double spread, double zig);
    
    void draw(Location location, VectorShape shape);
    
    Polygon createPolygon(Vector axis, double radius, int vertices);
    
    VectorShape createLightning(Vector direction, double distance, double spread);
    
    VectorShape createLightning(Vector start, Vector direction, double distance, double spread);
    
    VectorShape createLightning(Vector start, Vector direction, double distance, double spread, double zig);
    
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
