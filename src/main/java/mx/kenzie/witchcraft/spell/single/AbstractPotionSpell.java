package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Particle;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.util.Map;

public abstract class AbstractPotionSpell extends StandardSpell {
    public AbstractPotionSpell(Map<String, Object> map) {
        super(map);
    }
    
    public abstract PotionEffectType getPotion();
    
    public ParticleCreator getCreator() {
        final Color color = this.getColor();
        final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB).offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
            .count(0).force(true);
        return ParticleCreator.of(builder);
    }
    
    public abstract Color getColor();
    
}
