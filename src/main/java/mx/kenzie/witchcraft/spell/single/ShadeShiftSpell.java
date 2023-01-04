package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ShadeShiftSpell extends AbstractTeleportSpell {
	public ShadeShiftSpell(Map<String, Object> map) {
		super(map);
	}

	private transient List<Block> blocks;

	@Override
	public boolean canCast(LivingEntity caster) {
		this.blocks = getValidShadedLocations(caster.getLocation(), 10); // Radius TBD
		return blocks.size() >= 1;
	}

	public static List<Block> getValidShadedLocations(Location centre, double radius) {
		List<Block> blocks = AbstractTeleportSpell.getValidTeleportSpaces(centre, radius);
		List<Block> valid = new ArrayList<>();
		for (Block block : blocks) {
			if (block.getLightLevel() < 5) // Valid Light Level TBD
				valid.add(block);
		}
		return valid;
	}

	@Override
	public void run(LivingEntity caster, int range, float scale, double amplitude) {
		final Location location = caster.getLocation();
		final Location end = blocks.get(ThreadLocalRandom.current().nextInt(blocks.size())).getLocation();
		end.setDirection(location.getDirection());
		caster.teleport(end);
		ParticleCreator creator = WitchcraftAPI.client.particles(new ParticleBuilder(Particle.REDSTONE)
				.color(Color.GRAY, 0.75F));
		VectorShape shape = creator.createCircle(new Vector(0, 0, 0), 1.75, 100);
		for (Vector v = new Vector(0, 0, 0); v.getY() > 2; v.add(new Vector(0, 0.1, 0)))
			this.drawShape(shape, end.clone().add(v), 20);
	}

	// Potentially could be a static/default method in ParticleCreator?
	private void drawShape(VectorShape shape, Location location, long delay) {
		WitchcraftAPI.executor.submit(() -> {
			final ParticleBuilder builder = shape.builder();
			for (Vector vector : shape) {
				final Location point = location.clone().add(vector);
				builder.location(point).spawn();
				WitchcraftAPI.sleep(delay);
			}
		});
	}
}
