package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ScarlotsWeb extends AbstractSummonSpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(Particle.ELECTRIC_SPARK.builder().count(0));

    public ScarlotsWeb(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Minecraft minecraft = Minecraft.getInstance();
        final List<LivingEntity> list = new ArrayList<>(caster.getLocation().getNearbyLivingEntities(range, range / 2F));
        list.removeIf(entity -> minecraft.isAlly(entity, caster));
        final Location start = caster.getEyeLocation().add(0, -0.25, 0);
        final World world = caster.getWorld();
        world.playSound(start, Sound.BLOCK_CHAIN_PLACE, 1.0F, 1.0F);
        for (LivingEntity entity : list) {
            if (!(entity instanceof Mob)) continue;
            this.creator.createLine(start, entity.getEyeLocation(), 0.2).draw(start, 30);
            Location centre = entity.getLocation().toCenterLocation().add(0, -0.5, 0);
            world.playSound(centre, Sound.BLOCK_CHAIN_PLACE, 1.0F, 1.0F);
            int attempts = 0;
            while (centre.getBlock().getRelative(BlockFace.DOWN).isEmpty() && ++attempts < 3)
                centre = centre.add(0, -1, 0);
            final List<Block> blocks = getValidSpawnSpaces(entity.getLocation(), 3.5);
            minecraft.createChains(entity, centre, true);
            for (int i = 0; i < Math.min(blocks.size(), 4); i++) {
                final Location point = blocks.remove(ThreadLocalRandom.current().nextInt(blocks.size())).getLocation().toCenterLocation();
                point.add(0, -0.5, 0);
                minecraft.createChains(entity, centre, false);
            }
        }
    }

}
