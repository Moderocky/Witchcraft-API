package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class InfernoSpell extends AbstractTargetedSpell {
    
    protected transient final ParticleCreator flames = ParticleCreator.of(Particle.FLAME.builder().count(0)),
        burst = ParticleCreator.of(Particle.FLAME);
    protected transient final VectorShape shape = UnholyBlastSpell.PURPLE.createCircle(new Vector(0, 1, 0), 1.5, 50);
    
    public InfernoSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final double damage = Math.max(2, Math.min(8, amplitude));
        final Inferno inferno = new Inferno(caster, caster.getLocation(), range, damage);
        inferno.task = WitchcraftAPI.scheduler.scheduleAtFixedRate(inferno, 200, 200, TimeUnit.MILLISECONDS);
        
    }
    
    protected class Inferno implements Runnable {
        
        private final LivingEntity caster;
        private final Location start;
        private final int range;
        private final double damage;
        private ScheduledFuture<?> task;
        private volatile Vector end, direction;
        private volatile boolean cancelled;
        private int lifetime;
        
        protected Inferno(LivingEntity caster, Location start, int range, double damage) {
            this.caster = caster;
            this.start = start;
            this.range = range;
            this.damage = damage / 4;
            this.end = getEnd(caster, range);
        }
        
        @Override
        public void run() {
            if (cancelled) return;
            if (++lifetime > 160) this.cancel();
            else if (caster.isDead()) this.cancel();
            else if (caster.getWorld() != start.getWorld()) this.cancel();
            else if (caster.getLocation().distanceSquared(start) > (1.5 * 1.5)) this.cancel();
            else this.tick();
        }
        
        public void tick() {
            final Vector predict = getEnd(caster, range);
            if (end.distanceSquared(predict) < 4) end = predict;
            else end.add(predict.subtract(end).normalize().multiply(4));
            shape.draw(start);
            this.direction = end.clone().subtract(caster.getEyeLocation().toVector()).normalize();
            Minecraft.getInstance().ensureMain(this::calculateTarget);
        }
        
        private void calculateTarget() {
            final Target target = getTarget(caster, direction, range, true, Mode.NOT_ALLIES);
            final Location end = target.target(), start = caster.getEyeLocation().add(0, 0.45, 0);
            // todo hello `end` is always pointing straight left, but direction/this.end are right
            final VectorShape shape = flames.createCircle(direction, 0.15, 6);
            for (Vector vector : flames.createLine(start, end, 0.2)) {
                final Location point = start.clone().add(vector);
                shape.draw(point);
            }
            if (target.entity() != null) {
                Minecraft.getInstance()
                    .damageEntity(target.entity(), caster, damage, EntityDamageEvent.DamageCause.MAGIC);
            } else {
                final Block block = end.getBlock();
                // todo block destroy
            }
            if (end == null) return;
            burst.drawPoof(end, 0.7, 10);
        }
        
        public void cancel() {
            this.cancelled = true;
            if (task == null) return;
            this.task.cancel(true);
        }
        
    }
    
    protected Vector getEnd(LivingEntity caster, int range) {
        return caster.getEyeLocation().getDirection().multiply(range);
    }
    
}
