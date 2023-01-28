package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
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

public class TotemicInductionSpell extends StandardSpell {
    public transient final ParticleCreator totem = ParticleCreator.of(new ParticleBuilder(Particle.TOTEM)
        .count(0)
        .force(true));
    protected transient final ParticleCreator creator = ParticleCreator.of(Particle.REDSTONE.builder().count(0))
        .color(new Color(20, 18, 21));

    public TotemicInductionSpell(Map<String, Object> map) {
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
            .draw(location.add(vector), 20);
        totem.drawPoof(location, 0.5, 10);
        ItemArchetype.of("totem_of_undying").giveSafely(((Player) caster));
    }
}
