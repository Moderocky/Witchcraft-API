package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Map;

public class RakeSpell extends AbstractTargetedSpell {
    protected transient final java.awt.Color color = new java.awt.Color(20, 18, 21);
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.REDSTONE)
        .color(color.getRed(), color.getGreen(), color.getBlue())
        .count(0).force(true);
    
    public RakeSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Target trace = this.getTarget(caster, range);
        if (trace == null) return;
        final Location target = trace.target(), effect;
        final Entity found = trace.entity();
        effect = found instanceof LivingEntity living ? living.getEyeLocation() : target.clone().add(0, 1.3, 0);
        if (found != null)
            WitchcraftAPI.minecraft.damageEntitySafely(found, caster, 3 + amplitude, EntityDamageEvent.DamageCause.MAGIC);
        final ParticleCreator creator = ParticleCreator.of(builder);
        for (int i = 0; i < 5; i++) {
            final Vector first = ParticleCreator.random().multiply(1.8);
            final Vector second = ParticleCreator.random().multiply(1.8);
            creator.createLine(first, second, 0.2).draw(effect.clone().add(first), 30);
        }
    }
    
}
