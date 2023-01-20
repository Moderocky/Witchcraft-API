package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.entity.Grave;
import mx.kenzie.witchcraft.entity.Totem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ShallowGravesSpell extends AbstractWardSpell {
    protected transient final Color color = new Color(20, 18, 21);
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB)
        .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
        .count(0)
        .force(true);
    
    public ShallowGravesSpell(Map<String, Object> map) {
        super(map);
    }
    
    public static Location getRandom(Location centre) {
        final Random random = ThreadLocalRandom.current();
        return centre.add((random.nextDouble() - 0.5) * 20, (random.nextDouble() - 0.2) * 8, (random.nextDouble() - 0.5) * 20);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final int lifetime = 20 * 30;
        final LivingEntity entity = this.summonWard(caster, lifetime);
        final Totem cube = WitchcraftAPI.minecraft.getHandle(entity);
        cube.setMajorTickConsumer(thing -> {
            final List<Grave> graves = AbstractGraveSpell.getGraves(thing, range);
            if (graves.isEmpty()) return;
            final int summons = AbstractGraveSpell.summonCount(caster);
            if (summons >= AbstractGraveSpell.maxSummonCount(caster)) return;
            for (Grave grave : graves) {
                if (!grave.canGrow()) continue;
                final Location location = grave.getStart();
                final Entity skeleton = CustomEntityType.SKELETON_SUMMON.summon(caster, location);
                grave.attemptGrow(skeleton);
                break;
            }
        });
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, entity::remove, lifetime);
    }
    
}
