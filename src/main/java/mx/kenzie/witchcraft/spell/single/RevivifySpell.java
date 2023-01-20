package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.LocatedShape;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Map;

public class RevivifySpell extends AbstractTargetedSpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(new ParticleBuilder(Particle.REDSTONE)
        .data(new Particle.DustOptions(Color.fromRGB(55, 250, 94), 2))
        .count(0).force(true));
    
    public RevivifySpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Target target = this.getTarget(caster, range);
        final LivingEntity entity;
        if (target == null || !(target.entity() instanceof LivingEntity)) entity = caster;
        else entity = (LivingEntity) target.entity();
        for (PotionEffectType value : PotionEffectType.values()) {
            if (value.isInstant()) continue;
            if (value.getEffectCategory() != PotionEffectType.Category.HARMFUL) continue;
            entity.removePotionEffect(value);
        }
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, (int) amplitude, false, false, false));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 60, (int) amplitude, false, false, false));
        WitchcraftAPI.executor.submit(() -> {
            final Location location = entity.getLocation();
            final LocatedShape shape = creator.createSpiral(location, new Vector(0, 1, 0), 1.2, 2.5, 170);
            final ParticleBuilder builder = creator.getBuilder();
            double y = 0;
            for (Vector vector : shape) {
                y += 0.01;
                final Location point = location.clone().add(vector).add(0, y, 0);
                builder.location(point).spawn();
                WitchcraftAPI.sleep(3);
            }
        });
    }
}
