package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
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
    
    protected void drawCircle(ParticleCreator creator, Location centre) {
        final int particles = 80;
        final VectorShape circle = creator.createCircle(new Vector(0, -1, 0), 10, particles);
        for (Vector vector : circle) {
            final Location point = centre.clone().add(vector);
            creator.getBuilder().location(point).spawn();
            try {
                Thread.sleep(4000 / particles); // 4 rings drawing at a time
            } catch (InterruptedException ignored) {}
        }
    }
    
    protected List<LivingEntity> getAffected(LivingEntity caster, LivingEntity totem, boolean includeAllies) {
        final Location centre = totem.getLocation();
        final List<LivingEntity> list = new ArrayList<>(centre.getNearbyLivingEntities(10, 5));
        list.removeIf(found -> {
            if (totem == found) return true;
            return !includeAllies && WitchcraftAPI.minecraft.isAlly(found, caster);
        });
        return list;
    }
}
