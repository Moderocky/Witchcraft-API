package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.util.Map;

public class EnrageSpell extends StandardSpell {
    protected final ParticleCreator creator = ParticleCreator.of(Particle.REDSTONE.builder().count(0))
        .color(new Color(176, 22, 22));
    protected final ParticleCreator darkness = ParticleCreator.of(Particle.SPELL_MOB.builder().count(0))
        .color(new Color(20, 18, 21));
    
    public EnrageSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 1, true, true, false);
        final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 200, 1, true, true, false);
        for (Entity summon : Minecraft.getInstance().getSummons(caster)) {
            if (!(summon instanceof LivingEntity living)) continue;
            living.addPotionEffect(strength);
            living.addPotionEffect(speed);
            this.darkness.createLine(caster.getLocation(), living.getEyeLocation(), 0.3).draw(caster.getLocation(), 30);
            this.creator.drawPoof(living.getEyeLocation(), 0.6, 12);
        }
    }
}
