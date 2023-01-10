package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class OmnipresenceSpell extends StandardSpell {
    
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.DRAGON_BREATH)
        .extra(0.03)
        .force(true);
    private transient Entity target;
    private transient List<Block> blocks;
    
    public OmnipresenceSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Player player = ((Player) caster);
        final Location centre = target.getLocation();
        final int count = Math.min(blocks.size(), 3 + (int) amplitude);
        final Random random = ThreadLocalRandom.current();
        final Set<LivingEntity> entities = new HashSet<>();
        for (int i = 0; i < count; i++) {
            final Block block = blocks.remove(random.nextInt(blocks.size()));
            final Location location = block.getLocation();
            this.splash(location);
            location.setDirection(centre.toVector().subtract(location.toVector()));
            if (i == 0) player.teleport(location);
            else entities.add(WitchcraftAPI.minecraft.spawnMirrorImage(location, player));
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, () -> {
            for (LivingEntity entity : entities) {
                if (entity.isDead()) continue;
                this.splash(entity.getLocation());
                entity.remove();
            }
        }, 9 * 20);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        final Location location = caster.getEyeLocation();
        this.target = WitchcraftAPI.minecraft.getTargetEntity(location, 16, 0.96);
        if (!(target instanceof LivingEntity)) return false;
        if (!(caster instanceof HumanEntity)) return false;
        final int count = 8;
        this.blocks = AbstractTeleportSpell.getValidTeleportSpaces(target.getLocation(), 5.6);
        this.blocks.removeIf(block -> !WitchcraftAPI.minecraft.hasLineOfSight(block.getLocation(), target.getLocation()));
        return blocks.size() > count;
    }
    
    private void splash(Location location) {
        ParticleCreator.of(builder).drawPoof(location, 1, 10);
    }
}
