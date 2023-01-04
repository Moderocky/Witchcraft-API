package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ShadeShiftSpell extends AbstractTeleportSpell {
	protected transient final java.awt.Color color = new java.awt.Color(20, 18, 21);
	protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB)
		.offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
		.count(0).force(true);
	
	public ShadeShiftSpell(Map<String, Object> map) {
		super(map);
	}
	
	private transient List<Block> blocks;
	
	@Override
	public void run(LivingEntity caster, int range, float scale, double amplitude) {
		final Location centre = caster.getLocation();
		WitchcraftAPI.executor.submit(() -> this.playCircles(centre.clone()));
		final List<Block> found = blocks;
		final Location end = found.get(found.size() - 1).getLocation();
		end.setDirection(centre.getDirection());
		caster.teleportAsync(end);
		WitchcraftAPI.executor.submit(() -> this.playCircles(end.clone()));
	}
	
	private void playCircles(Location location) {
		final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
		final VectorShape shape = creator.createCircle(new Vector(0, 1, 0), 0.9, 20);
		for (int i = 0; i < 7; i++) {
			shape.draw(location);
			location.add(0, 0.3, 0);
			WitchcraftAPI.sleep(50);
		}
	}
	
	@Override
	public boolean canCast(LivingEntity caster) {
		this.blocks = getValidShadedLocations(caster.getLocation(), 15);
		return blocks.size() > 0; // this is easier to attempt-cast since we can be doing the blocks at the same time :)
	}
	
	public static List<Block> getValidShadedLocations(Location centre, int radius) {
		final int startX = centre.getBlockX(), startY = centre.getBlockY(), startZ = centre.getBlockZ();
		final List<Block> blocks = new ArrayList<>(30);
		final World world = centre.getWorld();
		final boolean day = world.isDayTime();
		check:
		for (int x = -radius; x < radius; x++)
			for (int z = -radius; z < radius; z++)
				for (int y = -(radius / 2); y < (radius / 2); y++) {
					if (blocks.size() > 29) break check;
					final int newX = startX + x, newY = startY + y, newZ = startZ + z;
					final Location point = new Location(world, newX, newY, newZ);
					final Block block = point.getBlock();
					if (block.getLightFromBlocks() > 7) continue;
					if (day && block.getLightFromSky() > 8) continue;
					if (isValidResult(point.getBlock(), true)) blocks.add(point.getBlock());
				}
		blocks.sort(Comparator.comparing(block -> block.getLocation().distanceSquared(centre)));
		return blocks;
	}
	
	// Potentially could be a static/default method in ParticleCreator?
	private void drawShape(VectorShape shape, Location location, long delay) {
		final ParticleBuilder builder = shape.builder();
		WitchcraftAPI.executor.submit(() -> {
			for (Vector vector : shape) {
				final Location point = location.clone().add(vector);
				builder.location(point).spawn();
				WitchcraftAPI.sleep(delay);
			}
		});
	}
}
