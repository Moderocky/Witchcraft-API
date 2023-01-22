package mx.kenzie.witchcraft.spell.effect;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public interface ParticleCreator {
    
    static @NotNull ParticleCreator of(Particle particle) {
        if (WitchcraftAPI.isTest) return new TestParticleCreator();
        return WitchcraftAPI.client.particles(particle);
    }
    
    static @NotNull ParticleCreator of(Material material) {
        if (WitchcraftAPI.isTest) return new TestParticleCreator();
        return ParticleCreator.of(Particle.BLOCK_CRACK.builder().data(material.createBlockData()));
    }
    
    static @NotNull ParticleCreator of(ParticleBuilder builder) {
        if (WitchcraftAPI.isTest) return new TestParticleCreator();
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
    
    double drawLine(Location start, Location end, double spread);
    
    Vector drawCurve(Location start, Location end, Vector curve, double spread);
    
    VectorShape createArc(Vector direction, double roll, double length, double height, int step);
    
    VectorShape createArc(Vector direction, double length, double height, int step);
    
    void drawCuboid(Location start, Location end, double spread);
    
    VectorShape createCuboid(Vector start, Vector end, double spread);
    
    VectorShape createCuboid(BoundingBox box, double spread);
    
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
    
    ParticleCreator clone();
    
    default ParticleCreator color(Color color) {
        final ParticleBuilder builder = this.getBuilder();
        if (builder.particle() == Particle.REDSTONE) builder.color(color.getRed(), color.getGreen(), color.getBlue());
        else if (builder.particle() == Particle.SPELL_MOB)
            builder.offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0);
        return this;
    }
    
    ParticleBuilder getBuilder();
}

class TestParticleCreator implements ParticleCreator {
    
    @Override
    public double drawLine(Location start, Location end, double spread) {
        return 0;
    }
    
    @Override
    public Vector drawCurve(Location start, Location end, Vector curve, double spread) {
        return null;
    }
    
    @Override
    public VectorShape createArc(Vector direction, double roll, double length, double height, int step) {
        return null;
    }
    
    @Override
    public VectorShape createArc(Vector direction, double length, double height, int step) {
        return null;
    }
    
    @Override
    public void drawCuboid(Location start, Location end, double spread) {
    
    }
    
    @Override
    public VectorShape createCuboid(Vector start, Vector end, double spread) {
        return null;
    }
    
    @Override
    public VectorShape createCuboid(BoundingBox box, double spread) {
        return null;
    }
    
    @Override
    public VectorShape createArc(double length, double height, int step) {
        return null;
    }
    
    @Override
    public void drawPoof(Location start, double radius, int particles) {
    
    }
    
    @Override
    public VectorShape createPoof(double radius, int particles) {
        return null;
    }
    
    @Override
    public VectorShape createPoof(double radiusX, double radiusY, int particles) {
        return null;
    }
    
    @Override
    public void drawLightning(Location start, Location end, double spread) {
    
    }
    
    @Override
    public VectorShape createLightning(Location start, Location end, double spread) {
        return null;
    }
    
    @Override
    public VectorShape createLightning(Location start, Location end, double spread, double zig) {
        return null;
    }
    
    @Override
    public void draw(Location location, VectorShape shape) {
    
    }
    
    @Override
    public Polygon createPolygon(Vector axis, double radius, int vertices) {
        return null;
    }
    
    @Override
    public VectorShape createLightning(Vector direction, double distance, double spread) {
        return null;
    }
    
    @Override
    public VectorShape createLightning(Vector start, Vector direction, double distance, double spread) {
        return null;
    }
    
    @Override
    public VectorShape createLightning(Vector start, Vector direction, double distance, double spread, double zig) {
        return null;
    }
    
    @Override
    public void drawPlate(Location start, double radius, int particles) {
    
    }
    
    @Override
    public VectorShape createPlate(Vector axis, double radius, int particles) {
        return null;
    }
    
    @Override
    public LocatedShape createSpiral(Location location, double startRadius, double endRadius, double height, int horizontalPrecision, int verticalPrecision) {
        return null;
    }
    
    @Override
    public LocatedShape createSpiral(Location location, double radius, double height, int horizontalPrecision, int verticalPrecision) {
        return null;
    }
    
    @Override
    public void playSpiral(Location location, double radius, double height, int horizontalPrecision, int verticalPrecision) {
    
    }
    
    @Override
    public VectorShape createCircle(Vector axis, double radius, int particles) {
        return null;
    }
    
    @Override
    public Vector getNormal(Vector axis) {
        return null;
    }
    
    @Override
    public VectorShape createSpiral(Vector axis, double length, double radius, int rings, int density) {
        return null;
    }
    
    @Override
    public LocatedShape createSpiral(Location location, Vector axis, double startRadius, double endRadius, int particles) {
        return null;
    }
    
    @Override
    public VectorShape createLine(Location start, Location end, double spread) {
        return null;
    }
    
    @Override
    public VectorShape createLine(Vector direction, double distance, double spread) {
        return null;
    }
    
    @Override
    public VectorShape createLine(Vector start, Vector direction, double distance, double spread) {
        return null;
    }
    
    @Override
    public VectorShape createLine(Vector start, Vector end, double spread) {
        return null;
    }
    
    @Override
    public VectorShape createSphere(double radius, int density) {
        return null;
    }
    
    @Override
    public ParticleCreator color(Color color) {
        return this;
    }
    
    @Override
    public ParticleBuilder getBuilder() {
        return new ParticleBuilder(Particle.FIREWORKS_SPARK);
    }
    
    @Override
    public ParticleCreator clone() {
        return new TestParticleCreator();
    }
}
