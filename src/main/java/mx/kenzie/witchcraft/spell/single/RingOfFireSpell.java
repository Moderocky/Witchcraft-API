package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class RingOfFireSpell extends StandardSpell {
    public RingOfFireSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_BLAZE_BURN, 1.0F, 0.5F);
        Minecraft.getInstance().spawnFireRing(caster, 1 + amplitude);
    }
    
}
