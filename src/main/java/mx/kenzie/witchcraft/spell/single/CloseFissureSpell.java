package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Particle;

import java.util.Map;

public class CloseFissureSpell extends UnravelSpell {
    
    public CloseFissureSpell(Map<String, Object> map) {
        super(map);
        this.creator = ParticleCreator.of(Particle.FIREWORKS_SPARK.builder().count(0));
    }
    
}
