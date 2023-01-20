package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.Map;

public class ShadowBladeSpell extends StandardSpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(Particle.REDSTONE.builder().count(0))
        .color(new Color(20, 18, 21));
    
    public ShadowBladeSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = caster.getEyeLocation();
        final Vector vector = location.getDirection();
        this.creator.createSpiral(location, vector, 0.2, 1.1, 60)
            .draw(location.add(vector));
        ItemArchetype.of("shadowblade").giveSafely(((Player) caster));
    }
}
