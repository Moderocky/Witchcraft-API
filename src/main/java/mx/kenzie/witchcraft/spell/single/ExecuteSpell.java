package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.Polygon;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Map;

import static net.kyori.adventure.sound.Sound.Source;
import static net.kyori.adventure.sound.Sound.sound;
import static org.bukkit.Sound.BLOCK_BEACON_ACTIVATE;
import static org.bukkit.Sound.ENTITY_SKELETON_DEATH;

public class ExecuteSpell extends AbstractTargetedSpell {
    public ExecuteSpell(Map<String, Object> map) {
        super(map);
    }
    
    private static final Sound DIE = sound().type(ENTITY_SKELETON_DEATH).volume(1.6F).pitch(0.5F).source(Source.AMBIENT)
        .build(),
        SHOOT = sound().type(BLOCK_BEACON_ACTIVATE).volume(0.5F).pitch(1.9F).source(Source.AMBIENT).build();
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Target trace = this.getTarget(caster, range, true);
        if (trace == null) return;
        final Location target = trace.target();
        final Location start = caster.getEyeLocation(), step = start.clone();
        final Vector direction = start.getDirection();
        final Polygon polygon = ParticleCreator.of(Particle.WAX_ON.builder().count(0))
            .createPolygon(direction, 0.45, 3).fillInLines(false, 0.16);
        final VectorShape lightning = ParticleCreator.of(Particle.GLOW.builder().count(0))
            .createLightning(start, target, 0.2, 0.35);
        for (int i = 2; i < 5; i++) {
            step.add(direction.clone().multiply(i));
            polygon.rotate(direction, 16);
            polygon.draw(step);
        }
        target.getWorld().playSound(SHOOT, step.getX(), step.getY(), step.getZ());
        target.getWorld().playSound(DIE, target.getX(), target.getY(), target.getZ());
        final Entity found = trace.entity();
        lightning.draw(start, 3);
        if (found != null)
            WitchcraftAPI.minecraft.damageEntity(found, caster, 200, EntityDamageEvent.DamageCause.VOID);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
}
