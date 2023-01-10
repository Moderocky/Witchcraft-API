package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Map;

public class TemporalDuplicateSpell extends AbstractSummonSpell {
    
    public TemporalDuplicateSpell(Map<String, Object> map) {
        super(map);
    }
    
    transient final ParticleCreator creator = WitchcraftAPI.client.particles(Particle.SOUL.builder().count(0));
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        final Location location = target.getLocation().add(0.5, 0.2, 0.5);
        creator.playSpiral(
            location.setDirection(new Vector(0, 1, 0)),
            0.8, 2, 10, 2
        );
        final LivingEntity image = WitchcraftAPI.minecraft.spawnMirrorImage(location, player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, () -> {
            if (image.isDead()) return;
            creator.playSpiral(
                image.getLocation().setDirection(new Vector(0, 1, 0)),
                0.8, 2, 10, 2
            );
            image.remove();
        }, Math.min(Math.max((((long) amplitude + 1) * 10), 16), 90) * 20);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return super.canCast(caster) && caster instanceof Player;
    }
}