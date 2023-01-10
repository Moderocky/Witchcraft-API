package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.Polygon;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HexSpell extends AbstractTargetedSpell {
    
    protected transient final Color color = new Color(147, 18, 183);
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB)
        .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
        .count(0)
        .force(true);
    protected transient final ParticleBuilder dust = new ParticleBuilder(Particle.DRIPPING_OBSIDIAN_TEAR)
        .count(0).force(true);
    
    public HexSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final AbstractTargetedSpell.Target trace = this.getTarget(caster, range, true);
        final Location target = trace.target();
        final Entity found = trace.entity();
        final Location eye = caster.getEyeLocation();
        final Vector direction = eye.getDirection(), reverse = direction.clone().multiply(-2.5);
        final double damage = 2.5 + amplitude;
        this.dust.offset(reverse.getX(), reverse.getY(), reverse.getZ());
        eye.add(direction.clone().multiply(2));
        final Polygon polygon = ParticleCreator.of(dust).createPolygon(direction, 1.1, 5).fillInLines(true, 0.14);
        polygon.draw(eye);
        caster.getWorld().playSound(eye, Sound.ENTITY_WITHER_SHOOT, 0.6F, 1.4F);
        WitchcraftAPI.executor.submit(() -> {
            final ParticleCreator creator = ParticleCreator.of(builder);
            final VectorShape shape = creator.createLine(eye, target, 0.3);
            for (Vector vector : shape) {
                final Location point = eye.clone().add(vector);
                this.builder.location(point).spawn();
                WitchcraftAPI.sleep(5);
            }
            Bukkit.getScheduler().callSyncMethod(WitchcraftAPI.plugin, () -> {
                final Set<Entity> set = new HashSet<>(target.getNearbyLivingEntities(2));
                if (found != null) set.add(found);
                for (Entity entity : set)
                    WitchcraftAPI.minecraft.damageEntitySafely(entity, caster, damage, EntityDamageEvent.DamageCause.MAGIC);
                return set.size() > 0;
            });
            target.getWorld().playSound(target, Sound.BLOCK_GLASS_BREAK, 0.6F, 0.2F);
            creator.drawPoof(target, 1.1, 16);
        });
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
}
