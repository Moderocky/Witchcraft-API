package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public abstract class AbstractEntitySpell extends StandardSpell {
	public AbstractEntitySpell(Map<String, Object> map) {
		super(map);
	}

	protected transient LivingEntity target;

	@Override
	public boolean canCast(LivingEntity caster) {
		final Entity entity = caster.getTargetEntity(20);
		if (!(entity instanceof LivingEntity living)) return false;
		target = living;
		return true;
	}
}
