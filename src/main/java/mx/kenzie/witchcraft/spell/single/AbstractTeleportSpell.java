package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class AbstractTeleportSpell extends StandardSpell {
    public AbstractTeleportSpell(Map<String, Object> map) {
        super(map);
    }
    
    public static List<Block> getValidTeleportSpaces(Location centre, double radius) {
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
    
    public static boolean isInvalidStand(Block block) {
        return block.getType().isCollidable();
    }
    
    public static boolean isInvalidAbove(Block block) {
        return block.getRelative(BlockFace.UP).getType().isCollidable();
    }
    
    public static boolean isInvalidBelow(Block block) {
        return !block.getRelative(BlockFace.DOWN).getType().isSolid();
    }
    
    public static boolean isValidResult(Block block, boolean stand) {
        if (AbstractTeleportSpell.isInvalidStand(block)) return false;
        if (AbstractTeleportSpell.isInvalidAbove(block)) return false;
        return !stand || !AbstractTeleportSpell.isInvalidBelow(block);
    }
    
    public static List<Block> getValidTeleportSpacesNoSight(Location centre, double radius) {
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
                    blocks.add(sample.getBlock());
                }
            }
        }
        blocks.sort(Comparator.comparing(block -> block.getLocation().distanceSquared(centre)));
        return blocks;
    }
    
    public static List<Block> getUnoccupiedTeleportSpaces(Location centre, double radius) {
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
                    blocks.add(sample.getBlock());
                }
            }
        }
        blocks.sort(Comparator.comparing(block -> block.getLocation().distanceSquared(centre)));
        return blocks;
    }
}
