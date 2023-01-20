package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DarknessSpell extends AbstractPotionSpell {
    public DarknessSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = caster.getEyeLocation();
        final List<LivingEntity> list = new ArrayList<>(location.getNearbyLivingEntities(10, 4));
        list.removeIf(living -> !WitchcraftAPI.minecraft.isEnemy(living, caster));
        final PotionEffect effect = new PotionEffect(this.getPotion(), (int) (20 * (4 + amplitude)), (int) amplitude, true, true, false);
        final ParticleCreator creator = this.getCreator();
        for (LivingEntity entity : list) entity.addPotionEffect(effect);
        final Vector axis = new Vector(0, 1, 0);
        WitchcraftAPI.executor.submit(() -> {
            for (int i = 1; i < 7; i++) {
                final VectorShape shape = creator.createCircle(axis, 1 + i, i * 4);
                shape.draw(location);
                WitchcraftAPI.sleep(30);
            }
        });
    }
    
    @Override
    public PotionEffectType getPotion() {
        return PotionEffectType.DARKNESS;
    }
    
    @Override
    public Color getColor() {
        return new Color(20, 18, 21);
    }
}
