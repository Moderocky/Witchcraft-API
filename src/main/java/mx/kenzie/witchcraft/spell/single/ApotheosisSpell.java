package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class ApotheosisSpell extends StandardSpell {
    public ApotheosisSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        // todo spell functionality
    }
}
