package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Map;

public class BoneCageSpell extends AbstractEntitySpell {
	protected transient final ParticleCreator creator = ParticleCreator.of(new ParticleBuilder(Particle.ASH)
			.count(0));
	public BoneCageSpell(Map<String, Object> map) {
		super(map);
	}

	@Override
	public boolean canCast(LivingEntity caster) {
		return super.canCast(caster);
	}

	@Override
	protected void run(LivingEntity caster, int range, float scale, double amplitude) {
		if (target == null) throw new RuntimeException();
		final Location centre = target.getLocation();
		target.setVelocity(new Vector(0, 0, 0));
		final int duration = (int) (5 + (amplitude * 2));
		target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration, 128));
		target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 128));
		creator.playSpiral(centre, 1.5, 2.3, 12, 2);
	}
}
