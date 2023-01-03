package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.sloth.Cache;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.WeakHashMap;

abstract class AbstractWarhammerSpell extends StandardSpell {
    protected static final Cache<LivingEntity, LivingEntity> HAMMERS = Cache.weak(WeakHashMap::new);
    protected transient Block target;
    
    public AbstractWarhammerSpell(Map<String, Object> map) {
        super(map);
    }
    
    protected LivingEntity summonHammer(LivingEntity caster, int range) {
        final LivingEntity past = this.getCurrentHammer(caster);
        if (past != null) past.remove();
        final int result = Math.max(5, Math.min(20, range));
        final Block found = caster.getTargetBlockExact(Math.max(5, Math.min(20, range)));
        final Block target;
        final Location eye = caster.getEyeLocation();
        if (found == null) target = eye.add(eye.getDirection().multiply(result)).getBlock();
        else target = found;
        final Location spawn = target.getRelative(0, 5, 0).getLocation().add(0.5, 0, 0.5);
        final LivingEntity hammer = WitchcraftAPI.minecraft.summonWarhammerTotem(caster, spawn);
        HAMMERS.put(caster, hammer);
        return hammer;
    }
    
    public LivingEntity getCurrentHammer(LivingEntity caster) {
        return HAMMERS.get(caster);
    }
    
}
