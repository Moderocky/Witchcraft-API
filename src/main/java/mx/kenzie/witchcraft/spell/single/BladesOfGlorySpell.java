package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BladesOfGlorySpell extends AbstractSummonSpell {
    
    protected transient final ParticleCreator creator = ParticleCreator.of(new ParticleBuilder(Particle.REDSTONE)
        .data(new Particle.DustOptions(Color.fromRGB(253, 58, 9), 3))
        .count(0)
        .extra(0));
    
    public BladesOfGlorySpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Random random = ThreadLocalRandom.current();
        for (int i = 0; i < 3; i++) {
            final Block block = blocks.get(random.nextInt(blocks.size()));
            final Location location = block.getLocation().add(0.5, 0.1, 0.5);
            this.getEntityType().summon(caster, location);
            this.creator.playSpiral(
                location.setDirection(new Vector(0, 1, 0)),
                0.7, 1.6, 12, 2
            );
        }
    }
    
    protected transient List<Block> blocks;
    
    protected CustomEntityType getEntityType() {
        return CustomEntityType.BLADE_OF_GLORY;
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        if (WitchcraftAPI.minecraft.nearbySummons(caster, this.getEntityType()) > 0) return false;
        Block block = caster.getTargetBlockExact(15, FluidCollisionMode.NEVER);
        if (block == null) return false;
        block = block.getRelative(BlockFace.UP);
        this.blocks = getValidSpawnSpaces(block.getLocation(), 4);
        return !blocks.isEmpty();
    }
}
