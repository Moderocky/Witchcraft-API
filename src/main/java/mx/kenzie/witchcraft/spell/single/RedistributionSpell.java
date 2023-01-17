package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedistributionSpell extends StandardSpell {
    public ParticleBuilder builder = new ParticleBuilder(Particle.DAMAGE_INDICATOR).count(0).force(true);
    
    public RedistributionSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location centre = caster.getEyeLocation();
        final List<LivingEntity> list = new ArrayList<>(centre.getNearbyLivingEntities(range / 2.0, range / 2.0));
        list.removeIf(Entity::isInvulnerable);
        double total = 0;
        for (LivingEntity entity : list) total += entity.getHealth();
        final double each = total / list.size();
        for (LivingEntity entity : list) {
            final AttributeInstance instance = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (instance != null) entity.setHealth(Math.min(instance.getValue(), Math.max(1, each)));
            if (entity == caster) continue;
            this.playLine(caster, entity);
        }
    }
    
    protected void playLine(LivingEntity caster, LivingEntity target) {
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        WitchcraftAPI.executor.submit(() -> {
            final Location start = caster.getEyeLocation();
            final Location end = target.getEyeLocation();
            final VectorShape line = creator.createLine(start, end, 0.4);
            for (Vector vector : line) {
                final Location point = start.clone().add(vector);
                this.builder.location(point).spawn();
                WitchcraftAPI.sleep(20);
            }
        });
    }
}
