package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Particle;

import java.util.Map;

public class MirrorImageSpell extends TemporalDuplicateSpell {
    
    transient final ParticleCreator creator = ParticleCreator.of(Particle.SPELL_WITCH.builder().count(0));
    
    public MirrorImageSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    protected int maxLifeTime() {
        return 36;
    }
    
    @Override
    protected int minLifeTime() {
        return 10;
    }
    
    @Override
    public ParticleCreator getCreator() {
        return creator;
    }
}
