package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

abstract class AbstractWardSpell extends StandardSpell {
    protected transient Block target;
    
    public AbstractWardSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        final List<Block> blocks = AbstractSummonSpell.getValidSpawnSpaces(caster.getLocation(), 3);
        if (blocks.isEmpty()) return false;
        if (blocks.size() == 1) this.target = blocks.get(0);
        else this.target = blocks.get(ThreadLocalRandom.current().nextInt(blocks.size()));
        return true;
    }
    
    protected LivingEntity summonWard(LivingEntity caster) {
        final Location spawn = target.getLocation().add(0.5, 0.1, 0.5);
        return WitchcraftAPI.minecraft.summonWardCube(caster, spawn);
    }
    
}
