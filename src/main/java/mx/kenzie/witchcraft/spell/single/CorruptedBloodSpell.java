package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class CorruptedBloodSpell extends AbstractTargetedSpell {
    protected transient final Color color = new Color(194, 21, 44);
    protected transient final ParticleCreator creator = ParticleCreator.of(new ParticleBuilder(Particle.REDSTONE)
        .color(color.getRed(), color.getGreen(), color.getBlue())
        .count(0)
        .force(true));
    
    public CorruptedBloodSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Target target = this.getTarget(caster, range, true);
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = amplitude + 4;
        world.playSound(location, Sound.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, 0.8F, 0.6F);
        final Location end = target.target();
        final double distance = location.distance(end);
        this.creator.createArc(location.getDirection(), ThreadLocalRandom.current()
            .nextInt(360), distance, 1.6, (int) distance * 8).draw(caster.getEyeLocation());
        final Set<Entity> list = new HashSet<>(target.target().getNearbyLivingEntities(2));
        if (target.entity() != null) list.add(target.entity());
        this.creator.drawPoof(end, 0.8, 20);
        location.getWorld().playSound(end, Sound.BLOCK_LAVA_POP, 0.9F, 0.8F);
        for (Entity entity : list)
            WitchcraftAPI.minecraft.damageEntitySafely(entity, caster, damage, EntityDamageEvent.DamageCause.MAGIC);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
}
