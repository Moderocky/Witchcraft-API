package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.data.achievement.Achievement;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class ApotheosisSpell extends StandardSpell {
    public ApotheosisSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (caster instanceof Player player) {
            Achievement.CAST_APOTHEOSIS.give(player);
            
        }
        // todo spell functionality
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
}
