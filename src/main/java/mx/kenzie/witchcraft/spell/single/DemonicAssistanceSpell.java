package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class DemonicAssistanceSpell extends AbstractDemonSpell {
    
    protected transient final ParticleCreator creator = ParticleCreator.of(Particle.COMPOSTER);
    
    public DemonicAssistanceSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return super.canCast(caster);
    }
    
    @Override
    protected DemonMaker getDemon() {
        return Minecraft.getInstance()::spawnSoldierDemon;
    }
    
}
