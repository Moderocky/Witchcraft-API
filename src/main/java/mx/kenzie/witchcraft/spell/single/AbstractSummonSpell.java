package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

abstract class AbstractSummonSpell extends StandardSpell {
    protected transient Block target;
    
    public AbstractSummonSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        if (WitchcraftAPI.minecraft.nearbySummons(caster, null) >= 5) return false;
        Block block = caster.getTargetBlockExact(25, FluidCollisionMode.NEVER);
        if (block == null) return false;
        block = block.getRelative(BlockFace.UP);
        attempt:
        {
            if (AbstractTeleportSpell.isInvalidStand(block)) break attempt;
            if (AbstractTeleportSpell.isInvalidAbove(block)) break attempt;
            if (AbstractTeleportSpell.isInvalidBelow(block)) break attempt;
            this.target = block;
            return true;
        }
        final List<Block> blocks = getValidSpawnSpaces(block.getLocation(), 3);
        if (blocks.isEmpty()) return false;
        if (blocks.size() == 1) this.target = blocks.get(0);
        else this.target = blocks.get(ThreadLocalRandom.current().nextInt(blocks.size()));
        return true;
    }
    
    public static List<Block> getValidSpawnSpaces(Location centre, double radius) {
        final World world = centre.getWorld();
        final List<Block> blocks = new ArrayList<>();
        for (double x = centre.getX() - radius; x <= centre.getX() + radius; x++) {
            for (double y = centre.getY() - radius; y <= centre.getY() + (radius / 2); y++) {
                for (double z = centre.getZ() - radius; z <= centre.getZ() + radius; z++) {
                    final Location sample = new Location(world, x, y, z);
                    if (sample.distanceSquared(centre) > radius * radius) continue;
                    final Block block = sample.getBlock();
                    if (AbstractTeleportSpell.isInvalidStand(block)) continue;
                    if (AbstractTeleportSpell.isInvalidAbove(block)) continue;
                    if (AbstractTeleportSpell.isInvalidBelow(block)) continue;
                    if (!WitchcraftAPI.minecraft.hasLineOfSight(sample, centre)) continue;
                    blocks.add(sample.getBlock());
                }
            }
        }
        blocks.sort(Comparator.comparing(block -> block.getLocation().distanceSquared(centre)));
        return blocks;
    }
    
}
