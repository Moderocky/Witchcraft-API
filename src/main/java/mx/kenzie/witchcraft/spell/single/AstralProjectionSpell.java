package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AstralProjectionSpell extends StandardSpell {
	public AstralProjectionSpell(Map<String, Object> map) {
		super(map);
	}

	@Override
	public boolean canCast(LivingEntity caster) {
		return caster instanceof Player;
	}

	@Override
	protected void run(LivingEntity caster, int range, float scale, double amplitude) {
		if (caster instanceof Player player) {
			final Location start = player.getLocation();
			LivingEntity mirror = WitchcraftAPI.minecraft.spawnMirrorImage(start, player);
			player.setGameMode(GameMode.SPECTATOR);
			WitchcraftAPI.scheduler.schedule(() -> {
				mirror.remove();
				player.teleport(start);
				player.setGameMode(GameMode.SURVIVAL);
			}, (long) Math.floor(3 + amplitude), TimeUnit.SECONDS);
		}
	}
}
