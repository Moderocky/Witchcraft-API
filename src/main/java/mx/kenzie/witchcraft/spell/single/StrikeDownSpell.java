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
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Map;

import static net.kyori.adventure.sound.Sound.Source;
import static net.kyori.adventure.sound.Sound.sound;
import static org.bukkit.Sound.ENTITY_BLAZE_SHOOT;
import static org.bukkit.Sound.ENTITY_MINECART_INSIDE_UNDERWATER;

public class StrikeDownSpell extends AbstractTargetedSpell {
    private static final Sound DIE = sound().type(ENTITY_BLAZE_SHOOT).volume(1.4F).pitch(0.5F).source(Source.AMBIENT)
        .build(),
        SHOOT = sound().type(ENTITY_MINECART_INSIDE_UNDERWATER).volume(0.5F).pitch(1.9F).source(Source.AMBIENT).build();
    protected transient ParticleCreator creator = ParticleCreator.of(Particle.WAX_ON.builder().count(0));
    
    public StrikeDownSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Target trace = this.getTarget(caster, range, true);
        if (trace == null) return;
        final Location target = trace.target();
        final Location start = caster.getEyeLocation(), step = start.clone();
        final Vector direction = start.getDirection();
        final Polygon polygon = creator.createPolygon(direction, 0.6, 4).fillInLines(false, 0.16);
        final VectorShape lightning = ParticleCreator.of(Particle.SPELL_WITCH.builder().count(0))
            .createLightning(start, target, 0.2, 0.5);
        step.add(direction);
        for (int i = 0; i < 7; i++) {
            step.add(direction);
            polygon.rotate(direction, 16);
            polygon.draw(step);
            for (Vector vector : polygon) vector.multiply(1.3);
        }
        target.getWorld().playSound(SHOOT, step.getX(), step.getY(), step.getZ());
        target.getWorld().playSound(DIE, target.getX(), target.getY(), target.getZ());
        final Entity found = trace.entity();
        lightning.draw(start, 3);
        if (found instanceof Player)
            WitchcraftAPI.minecraft.damageEntity(found, caster, 6 + amplitude, EntityDamageEvent.DamageCause.MAGIC);
        else if (found != null)
            WitchcraftAPI.minecraft.damageEntity(found, caster, 200, EntityDamageEvent.DamageCause.VOID);
    }
}
