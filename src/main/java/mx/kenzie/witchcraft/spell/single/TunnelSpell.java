package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.Protection;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class TunnelSpell extends StandardSpell {

    private transient final ParticleCreator creator = ParticleCreator.of(Particle.ASH),
        repel = ParticleCreator.of(Particle.WAX_OFF);
    private transient Block start;

    public TunnelSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        this.start = caster.getTargetBlockExact(5);
        if (start == null) return false;
        return this.isAllowed(start);
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location point = start.getLocation();
        final Vector direction = caster.getEyeLocation().getDirection();
        final double length = 4 + (amplitude * 1.5);
        WitchcraftAPI.executor.submit(() -> {
            for (int i = 0; i < length; i++) {
                this.creator.drawPoof(point, 1, 10);
                if (!this.bore(point, caster)) break;
                point.add(direction);
                WitchcraftAPI.sleep(350);
            }
        });
    }

    public boolean bore(Location location, LivingEntity caster) {
        final Block centre = location.getBlock();
        if (!this.isAllowed(centre)) return false;
        final Protection protection = Protection.getInstance();
        if (!protection.canBreak(caster, location)) return false;
        final Minecraft minecraft = Minecraft.getInstance();
        for (int x = -1; x < 2; x++)
            for (int y = -1; y < 2; y++)
                for (int z = -1; z < 2; z++) {
                    final Location point = location.clone().add(x, y, z);
                    final Block block = point.getBlock();
                    if (!this.isAllowed(block)) repel.drawPoof(point, 0.8, 10);
                    else if (!protection.canBreak(caster, point)) repel.drawPoof(point, 0.8, 10);
                    else minecraft.breakBlock(block);
                }
        return true;
    }

    public boolean isAllowed(Block block) {
        if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR) return true;
        return this.isSoft(block);
    }

    public boolean isSoft(Block block) {
        return switch (block.getType()) {
            case MUD, DIRT, ROOTED_DIRT, PODZOL, GRASS_BLOCK,
                MOSS_BLOCK, MOSS_CARPET,
                SAND, GRAVEL, CLAY,
                SNOW, SNOW_BLOCK, POWDER_SNOW -> true;
            default -> false;
        };
    }

}
