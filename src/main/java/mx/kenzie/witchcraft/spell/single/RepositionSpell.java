package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RepositionSpell extends WarpSpell {
    protected transient final ParticleCreator creator;
    protected transient Location end;
    
    public RepositionSpell(Map<String, Object> map) {
        super(map);
        if (!WitchcraftAPI.isTest)
            creator = WitchcraftAPI.client.particles(new ParticleBuilder(Particle.DRIPPING_OBSIDIAN_TEAR).force(true)
                .count(1));
        else creator = null;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        this.end.setDirection(caster.getLocation().add(0, 0.5, 0).toVector().subtract(end.toVector()));
        this.creator.getBuilder().force(true).count(1);
        final World world = caster.getWorld();
        Location s1 = caster.getLocation();
        s1.setPitch(-90);
        s1.setYaw(0);
        this.creator.playSpiral(s1.add(0, -0.7, 0), 0.8, 2, 25, 3);
        Location s2 = end.clone();
        s2.setPitch(-90);
        s2.setYaw(0);
        this.creator.playSpiral(s2.add(0, -0.7, 0), 0.8, 2, 25, 3);
        caster.teleport(end);
        world.playSound(s1, Sound.BLOCK_BEACON_ACTIVATE, 1.8F, 1.1F);
        world.playSound(s2, Sound.BLOCK_BEACON_DEACTIVATE, 1.8F, 1.1F);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        final Location location = caster.getLocation();
        final Location eye = caster.getEyeLocation();
        final World world = location.getWorld();
        final Entity target = WitchcraftAPI.minecraft.getTargetEntity(eye, 18, 0.96);
        if (target == null) return false;
        final Location centre = target instanceof LivingEntity ? ((LivingEntity) target).getEyeLocation() : target.getLocation();
        double radius = Math.min(Math.max(location.distanceSquared(target.getLocation()) / 4, 10), 20);
        final List<Block> blocks = new ArrayList<>();
        for (double x = centre.getX() - radius; x <= centre.getX() + radius; x++) {
            for (double y = centre.getY() - radius; y <= centre.getY() + (radius / 2); y++) {
                for (double z = centre.getZ() - radius; z <= centre.getZ() + radius; z++) {
                    final Location sample = new Location(world, x, y, z);
                    if (sample.distanceSquared(centre) < ((radius * 0.6) * (radius * 0.6))) continue;
                    if (sample.distanceSquared(centre) > sample.distanceSquared(location)) continue;
                    final Block block = sample.getBlock();
                    if (isInvalidStand(block)) continue;
                    if (isInvalidAbove(block)) continue;
                    if (isInvalidBelow(block)) continue;
                    if (!WitchcraftAPI.minecraft.hasLineOfSight(sample, centre, caster)) continue;
                    blocks.add(sample.getBlock());
                }
            }
        }
        if (blocks.size() < 1) return false;
        this.end = blocks.get(ThreadLocalRandom.current().nextInt(blocks.size())).getLocation();
        return true;
    }
}
