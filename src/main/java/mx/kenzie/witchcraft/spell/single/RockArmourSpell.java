package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RockArmourSpell extends StandardSpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(Material.IRON_BARS);
    protected transient final VectorShape circle = creator.createCircle(new Vector(0, 1, 0), 1, 28);

    public RockArmourSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = caster.getLocation();
        final PotionEffect effect = new PotionEffect(PotionEffectType.ABSORPTION, 300, (int) (3 + Math.ceil(amplitude)), false, false, false);
        final List<Block> list = new ArrayList<>(this.getBlocks(caster));
        final int bound = list.size();
        final Random random = ThreadLocalRandom.current();
        caster.addPotionEffect(effect);
        if (bound < 1) return;
        final List<Location> blocks = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) blocks.add(list.get(random.nextInt(bound)).getLocation().add(0.5, 0.5, 0.5));
        final World world = caster.getWorld();
        world.playSound(location, Sound.BLOCK_ANVIL_USE, 1.3F, 0.7F);
        WitchcraftAPI.executor.submit(() -> {
            for (Location block : blocks) {
                location.add(0, 0.3, 0);
                this.circle.draw(location);
                final Vibration vibration = new Vibration(new Vibration.Destination.EntityDestination(caster), 20);
                world.spawnParticle(Particle.VIBRATION, block, 1, vibration);
                WitchcraftAPI.sleep(100);
                world.playSound(location, Sound.ITEM_ARMOR_EQUIP_IRON, 0.5F, 1.4F);
            }
        });
    }

    protected Collection<Block> getBlocks(LivingEntity caster) {
        final int radius = 8;
        final Location location = caster.getLocation();
        final List<Block> blocks = new LinkedList<>();
        for (int x = 0; x < radius * 2; x++) {
            for (int y = 0; y < radius * 2; y++) {
                for (int z = 0; z < radius * 2; z++) {
                    final Vector offset = new Vector(x - radius, y - radius, z - radius);
                    final Location point = location.clone().add(offset);
                    final Block block = point.getBlock();
                    if (!block.getType().isOccluding()) continue;
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }
}
