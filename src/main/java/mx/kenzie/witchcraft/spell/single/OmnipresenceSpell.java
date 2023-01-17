package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class OmnipresenceSpell extends AbstractTargetedSpell {
    
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.DRAGON_BREATH)
        .extra(0.03)
        .force(true);
    private transient List<Block> blocks;
    
    public OmnipresenceSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Player player = ((Player) caster);
        final int count = Math.min(blocks.size(), 2 + (int) amplitude);
        final Random random = ThreadLocalRandom.current();
        final Location centre = caster.getEyeLocation();
        final Set<LivingEntity> entities = new HashSet<>();
        for (int i = 0; i < count; i++) {
            final Block block = blocks.remove(random.nextInt(blocks.size()));
            final Location location = block.getLocation();
            this.splash(location);
            location.setDirection(centre.toVector().subtract(location.toVector()));
            entities.add(WitchcraftAPI.minecraft.spawnMirrorImage(location, player));
        }
        final AbstractTargetedSpell.Target target = this.getTarget(caster, range, false);
        if (target != null && target.entity() instanceof LivingEntity living)
            for (LivingEntity entity : entities) WitchcraftAPI.minecraft.getAsSummon(entity).setTarget(living);
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, () -> {
            for (LivingEntity entity : entities) {
                if (entity.isDead()) continue;
                this.splash(entity.getLocation());
                entity.remove();
            }
        }, Math.min(Math.max(((long) scale * 8), 8), 20) * 20);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        final Location location = caster.getEyeLocation();
        if (!(caster instanceof Player)) return false;
        this.blocks = AbstractTeleportSpell.getValidTeleportSpaces(location, 5.6);
        this.blocks.removeIf(block -> !WitchcraftAPI.minecraft.hasLineOfSight(block.getLocation(), location));
        return blocks.size() > 6;
    }
    
    private void splash(Location location) {
        ParticleCreator.of(builder).drawPoof(location, 1, 10);
    }
}
