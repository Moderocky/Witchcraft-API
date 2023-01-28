package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.Protection;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class InfernoSpell extends AbstractTargetedSpell {

    protected transient final ParticleCreator burst = ParticleCreator.of(Particle.FLAME.builder().count(0));
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
        final double damage = Math.max(3, Math.min(9, 1.5 + amplitude));
        final Inferno inferno = new Inferno(caster, caster.getLocation(), range, damage);
        inferno.task = WitchcraftAPI.scheduler.scheduleAtFixedRate(inferno, 200, 50, TimeUnit.MILLISECONDS);

    }

    protected Vector getEnd(LivingEntity caster, int range) {
        return caster.getEyeLocation().add(caster.getEyeLocation().getDirection().multiply(range)).toVector();
    }

    protected static class Breaker {

        private final Block block;
        private final int health, id;
        private final Player player;
        private final List<Player> viewers;
        private int lifetime;
        private int stage;
        private boolean done;

        protected Breaker(Block block, Player player) {
            this.block = block;
            final float hardness = block.getType().getHardness();
            this.id = ((block.getX() & 0xFFF) << 20) | ((block.getZ() & 0xFFF) << 8) | (block.getY() & 0xFF);
            if (hardness <= 0) health = Integer.MAX_VALUE;
            else health = (int) (hardness * 3);
            this.player = player;
            this.viewers = new ArrayList<>(Bukkit.getOnlinePlayers());
            this.viewers.removeIf(viewer -> viewer.getWorld() != block.getWorld());
            this.viewers.removeIf(viewer -> viewer.getLocation().distanceSquared(block.getLocation()) > 1600);
        }

        public void tick() {
            if (done) return;
            if (health > 1000) return;
            lifetime++;
            final float progress = ((float) lifetime) / health;
            if (progress > 1) {
                this.clear();
                this.destroy();
                return;
            }
            final int stage = (int) Math.floor(progress / 9);
            if (this.stage == stage) return;
            this.stage = stage;
            WitchcraftAPI.client.sendBlockBreak(id, block, stage, viewers.toArray(new Player[0]));
        }

        public void clear() {
            WitchcraftAPI.client.sendBlockBreak(id, block, 10, viewers.toArray(new Player[0]));
        }

        public void destroy() {
            if (!Protection.getInstance().canBreak(player, block.getLocation())) return;
            this.block.breakNaturally(true, true);
            this.done = true;
        }

    }

    protected class Inferno implements Runnable {

        private final LivingEntity caster;
        private final ParticleCreator ring = ParticleCreator.of(Particle.FLAME.builder().count(0).extra(0.5));
        private final Location start;
        private final int range;
        private final double damage;
        private final ParticleCreator flames = ParticleCreator.of(Particle.REDSTONE.builder()
            .color(org.bukkit.Color.fromRGB(245, 50, 3), 1).count(0));
        private final Map<Block, Breaker> blocks = new HashMap<>();
        private ScheduledFuture<?> task;
        private volatile Vector end, direction;
        private volatile boolean cancelled;
        private int lifetime;

        protected Inferno(LivingEntity caster, Location start, int range, double damage) {
            this.caster = caster;
            this.start = start;
            this.range = range;
            this.damage = damage;
            this.end = getEnd(caster, range);
        }

        @Override
        public void run() {
            if (cancelled) return;
            if (lifetime % 10 == 0) {
                start.getWorld().playSound(start, Sound.BLOCK_CAMPFIRE_CRACKLE, 0.5F, 0.3F);
                shape.draw(start);
            }
            final int blob = 40, scale = 78;
            final int green = (int) (scale * Math.sin(lifetime / 6F) + scale + blob);
            this.flames.color(new Color(250, green, 3));
            if (++lifetime > 160) this.cancel();
            else if (caster.isDead()) this.cancel();
            else if (caster.getWorld() != start.getWorld()) this.cancel();
            else if (caster.getLocation().distanceSquared(start) > 2.5) this.cancel();
            else this.tick();
        }

        public void cancel() {
            this.cancelled = true;
            if (task == null) return;
            this.task.cancel(true);
        }

        public void tick() {
            final Vector predict = getEnd(caster, range);
            if (end.distanceSquared(predict) < 4) end = predict;
            else end.add(predict.subtract(end).normalize().multiply(2));
            final Vector direction = this.direction = end.clone().subtract(caster.getEyeLocation().toVector())
                .normalize();
            Minecraft.getInstance().ensureMain(this::calculateTarget);
            if (lifetime % 4 == 0) {
                ring.getBuilder().offset(direction.getX(), direction.getY(), direction.getZ());
                ring.createCircle(direction, 0.2, 12).draw(caster.getEyeLocation().add(0, 0.55, 0));
            }
        }

        private void calculateTarget() {
            final Target target = getTarget(caster, direction, range, true, Mode.NOT_ALLIES);
            final Location end = target.target(), start = caster.getEyeLocation().add(0, 0.55, 0);
            double dither = 0;
            final VectorShape line = flames.createLine(start, end, 0.2);
            for (Vector vector : line) vector.add(ParticleCreator.random().multiply(dither += 0.005));
            line.draw(start);
            if (target.entity() != null) {
                Minecraft.getInstance()
                    .damageEntity(target.entity(), caster, damage, EntityDamageEvent.DamageCause.MAGIC);
                if (end == null) return;
                burst.drawPoof(end, 0.5, 10);
            } else if (target.result() != null && target.result().getHitBlock() != null) {
                final Block block = target.result().getHitBlock();
                if (!block.isSolid() || !block.getType().isBlock()) return;
                Particle.LAVA.builder().location(end).spawn();
                if (!(caster instanceof Player player)) return;
                this.blocks.computeIfAbsent(block, thing -> new Breaker(thing, player));
                final Breaker breaker = blocks.get(block);
                breaker.tick();
            }
        }

    }

}
