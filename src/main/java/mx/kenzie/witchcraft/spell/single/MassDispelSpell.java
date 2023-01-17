package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.Protection;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.LocatedShape;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.ward.WardInstance;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MassDispelSpell extends StandardSpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(new ParticleBuilder(Particle.REDSTONE)
        .color(Color.fromRGB(35, 173, 252))
        .count(0).force(true));
    protected final ParticleBuilder soul = new ParticleBuilder(Particle.SOUL_FIRE_FLAME).count(0).extra(0);
    
    public MassDispelSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        for (PotionEffectType value : PotionEffectType.values()) {
            if (value.isInstant()) continue;
            if (value.getEffectCategory() != PotionEffectType.Category.HARMFUL) continue;
            caster.removePotionEffect(value);
        }
        final Protection protection = Protection.getInstance();
        for (WardInstance instance : protection.getWardsAt(caster.getLocation())) instance.discard();
        WitchcraftAPI.executor.submit(() -> {
            final Location location = caster.getLocation();
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
        WitchcraftAPI.executor.submit(() -> {
            final Location location = caster.getLocation();
            final Random random = ThreadLocalRandom.current();
            final ParticleCreator creator = ParticleCreator.of(soul);
            for (int i = 0; i < 4; i++) {
                WitchcraftAPI.sleep(600);
                final Location start = location.clone();
                start.add((random.nextDouble() - 0.5) * 14, -1, (random.nextDouble() - 0.5) * 14);
                creator.drawLightning(start, start.clone().add(0, 7, 0), 0.2);
                start.getWorld().playSound(start, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5F, 2.0F);
            }
        });
    }
}
