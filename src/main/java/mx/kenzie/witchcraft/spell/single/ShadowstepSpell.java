package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.Polygon;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class ShadowstepSpell extends AbstractTeleportSpell {
    private transient final ParticleCreator creator = ParticleCreator.of(Particle.REDSTONE.builder().count(0))
        .color(new Color(20, 18, 21));

    private transient List<Block> blocks;

    public ShadowstepSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location start, location = start = caster.getEyeLocation();
        final World world = location.getWorld();
        final Location end = blocks.get(0).getLocation()
            .add(0.5, 0.1, 0.5);
        final Vector direction = location.getDirection();
        end.setDirection(location.getDirection());
        final Polygon shape = creator.createPolygon(location.getDirection(), 0.7, 4);
        shape.fillInLines(false, 0.2);
        double distance = location.distanceSquared(end);
        int i = 1;
        do {
            location.add(direction);
            i++;
            shape.draw(location);
            shape.rotate(direction, 10);
        } while (i * i < distance);
        caster.teleport(end);
        world.playSound(start, Sound.BLOCK_END_PORTAL_SPAWN, 0.2F, 1.9F);
        world.playSound(end, Sound.BLOCK_END_PORTAL_SPAWN, 0.2F, 1.9F);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        final Block test = caster.getTargetBlockExact(25);
        if (test == null) return false;
        final Location centre = test.getLocation();
        double radius = 4.0D;
        this.blocks = getValidTeleportSpacesNoSight(centre, radius);
        return !blocks.isEmpty();
    }
}
