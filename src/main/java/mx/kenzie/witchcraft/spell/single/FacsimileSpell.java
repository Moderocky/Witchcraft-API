package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class FacsimileSpell extends StandardSpell {
	protected transient static final Map<Player, LivingEntity> facsimiles = new HashMap<>(); // Maybe a better way to implement this, potentially entity persistent data containers?
	protected transient static final ParticleCreator creator = ParticleCreator.of(Material.CRYING_OBSIDIAN);
	protected transient static final VectorShape circle = creator.createCircle(new Vector(0, 1, 0), 0.8, 26);

	public static boolean hasFacsimile(Player player) {
		return facsimiles.containsKey(player);
	}

	public static void handleRespawn(Player player) {
		if (!facsimiles.containsKey(player)) return;
		final LivingEntity facsimile = facsimiles.get(player);
		facsimiles.remove(player);
		final Location start = facsimile.getLocation();
		facsimile.remove();
		player.setHealth(WitchcraftAPI.minecraft.getMaxHealth(player));
		player.teleport(start);
		start.getWorld().playSound(start, Sound.ENTITY_CAT_HISS, 1.2F, 1.0F);
		WitchcraftAPI.executor.submit(() -> {
			for (int i = 1; i < 8; i++) {
				start.add(0, 0.2, 0);
				creator.draw(start, circle);
				WitchcraftAPI.sleep(100);
			}
		});
		player.sendMessage(Component.text("You have been returned to your facsimile...", WitchcraftAPI.colors()
						.lowlight()));
	}

	public FacsimileSpell(Map<String, Object> map) {
		super(map);
	}

	@Override
	public boolean canCast(LivingEntity caster) {
		return caster instanceof Player;
	}

	@Override
	protected void run(LivingEntity caster, int range, float scale, double amplitude) {
		if (!(caster instanceof Player player)) return;
		Component component = Component.text("You have spawned your facsimile.", WitchcraftAPI.colors()
				.lowlight());
		if (FacsimileSpell.hasFacsimile(player)) {
			final LivingEntity facsimile = facsimiles.get(player);
			facsimile.remove();
			component = Component.text("You have spawned your facsimile, destroying your old facsimile.", WitchcraftAPI.colors()
					.lowlight());
		}
		// TODO make spawning the facsimile look nicer.
		final Location start = player.getLocation();
		final LivingEntity facsimile = WitchcraftAPI.minecraft.spawnMirrorImageNoAI(start, player);
		facsimile.setInvulnerable(true); // Up for debate.
		facsimiles.put(player, facsimile);
		player.sendMessage(component);
	}
}
