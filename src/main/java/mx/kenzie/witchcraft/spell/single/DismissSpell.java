package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class DismissSpell extends AbstractTargetedSpell {
    protected final ParticleCreator creator = ParticleCreator.of(Particle.FIREWORKS_SPARK.builder().count(0));
    
    public DismissSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        for (Entity summon : Minecraft.getInstance().getSummons(caster)) {
            this.creator.drawPoof(summon.getLocation().add(0, 1, 0), 0.5, 10);
            summon.remove();
        }
    }
}
