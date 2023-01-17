package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
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
import java.util.concurrent.ThreadLocalRandom;

public class PrismaticRaySpell extends AbstractTargetedSpell {
    public static final Color RED = new Color(255, 40, 58),
        ORANGE = new Color(255, 105, 40),
        YELLOW = new Color(255, 219, 40),
        GREEN = new Color(144, 255, 40),
        BLUE = new Color(40, 190, 255),
        INDIGO = new Color(128, 79, 255),
        VIOLET = new Color(183, 40, 255);
    public static final Color[] COLORS = {RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET};
    
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB)
        .count(0)
        .force(true);
    
    public PrismaticRaySpell(Map<String, Object> map) {
        super(map);
    }
    
    public static ParticleBuilder setRandom(ParticleBuilder builder) {
        final Color color = COLORS[ThreadLocalRandom.current().nextInt(COLORS.length)];
        if (builder.particle() == Particle.SPELL_MOB)
            builder.offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0);
        else builder.color(color.getRed(), color.getGreen(), color.getBlue());
        return builder;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Target trace = this.getTarget(caster, range, true);
        final Location target = trace.target();
        final Entity found = trace.entity();
        final Location eye = caster.getEyeLocation(), locked = eye.clone();
        final Vector direction = eye.getDirection();
        locked.setPitch(0);
        final Vector step = locked.getDirection().rotateAroundAxis(new Vector(0, 1, 0), Math.toRadians(-90));
        final double damage = 2.5 + amplitude;
        eye.add(direction);
        caster.getWorld().playSound(eye, Sound.BLOCK_BEACON_AMBIENT, 0.4F, 1.8F);
        WitchcraftAPI.executor.submit(() -> {
            final ParticleCreator creator = ParticleCreator.of(builder);
            for (int i = 0; i < COLORS.length; i++) {
                final Color color = COLORS[i];
                final Vector offset = step.clone().multiply((i - 3.5) / 5.5);
                final Location start = eye.clone().add(offset), end = target.clone().add(offset);
                this.builder.offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0);
                creator.drawLine(start, end, 0.3);
            }
            target.getWorld().playSound(target, Sound.BLOCK_GLASS_BREAK, 0.6F, 0.2F);
        });
        final Set<Entity> set = new HashSet<>(target.getNearbyLivingEntities(2));
        if (found != null) set.add(found);
        for (Entity entity : set)
            WitchcraftAPI.minecraft.damageEntitySafely(entity, caster, damage, EntityDamageEvent.DamageCause.MAGIC);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
}
