package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LandslideSpell extends BulwarkSpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(Material.MUD);
    protected transient final VectorShape circle = creator.createCircle(new Vector(0, 1, 0), 2.2, 60);
    protected transient Collection<Block> blocks;
    
    public LandslideSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return super.canCast(caster) && this.getBlocks(caster).size() > 1;
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = caster.getLocation();
        final List<Block> list = new ArrayList<>(blocks);
        final double damage = 3 + amplitude;
        final int bound = list.size();
        final Random random = ThreadLocalRandom.current();
        if (bound < 1) return;
        final World world = caster.getWorld();
        final Minecraft minecraft = Minecraft.getInstance();
        final int count = (int) Math.min(bound, Math.min(5, Math.max(3, 3 + amplitude)));
        world.playSound(location, Sound.ENTITY_WARDEN_DIG, 1.3F, 0.8F);
        this.circle.draw(location, 30);
        for (int i = 0; i < count; i++) {
            final Block block = list.get(random.nextInt(bound));
            minecraft.launchBlock(block, caster, i * 5, damage);
        }
    }
    
    protected Collection<Block> getBlocks(LivingEntity caster) {
        final int radius = 8;
        final Location location = caster.getLocation();
        final List<Block> blocks = new LinkedList<>();
        for (int x = 0; x < radius * 2; x++) {
            for (int y = 0; y < radius * 1.5; y++) {
                for (int z = 0; z < radius * 2; z++) {
                    final Vector offset = new Vector(x - radius, y - radius, z - radius);
                    final Location point = location.clone().add(offset);
                    final Block block = point.getBlock();
                    if (!isBlockOkay(block)) continue;
                    blocks.add(block);
                }
            }
        }
        return this.blocks = blocks;
    }
    
}
