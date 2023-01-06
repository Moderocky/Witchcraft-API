package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class ThunderboltSpell extends StandardSpell {
	public ThunderboltSpell(Map<String, Object> map) {
		super(map);
	}

	@Override
	public boolean canCast(LivingEntity caster) {
		return true;
	}

	@Override
	protected void run(LivingEntity caster, int range, float scale, double amplitude) {
		final World world = caster.getWorld();
		final Location location = caster.getEyeLocation();
		world.strikeLightning(location);
	}
}
