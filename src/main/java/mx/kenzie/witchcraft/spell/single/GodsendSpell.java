package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class GodsendSpell extends StandardSpell {
    public GodsendSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        // todo spell functionality
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
}
