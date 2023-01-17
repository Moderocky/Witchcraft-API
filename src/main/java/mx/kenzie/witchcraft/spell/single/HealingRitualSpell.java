package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HealingRitualSpell extends StandardSpell {
	protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.VILLAGER_HAPPY)
			.count(0)
			.force(true);

	public HealingRitualSpell(Map<String, Object> map) {
		super(map);
	}

	@Override
	public boolean canCast(LivingEntity caster) {
		return true;
	}

	@Override
	protected void run(LivingEntity caster, int range, float scale, double amplitude) {
		final Location centre = caster.getEyeLocation();
		final World world = caster.getWorld();
		final List<LivingEntity> entities = new ArrayList<>(centre.getNearbyLivingEntities(10, 5));
		entities.removeIf(target -> !WitchcraftAPI.minecraft.isAlly(caster, target));
		for (LivingEntity entity : entities) {
			if (entity == caster)
				continue;
			Vibration vibration = new Vibration(new Vibration.Destination.EntityDestination(entity), 40);
			for (int i = 0; i < 250; i += 50) {
				WitchcraftAPI.scheduler.schedule(() ->
								world.spawnParticle(Particle.VIBRATION, centre, 1, vibration),
						i, TimeUnit.MILLISECONDS);
			}
		}
		WitchcraftAPI.scheduler.schedule(() -> {
			for (LivingEntity entity : entities)
				entity.setHealth(Math.min(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), entity.getHealth() + 5));
		}, 2, TimeUnit.SECONDS);
	}
}
