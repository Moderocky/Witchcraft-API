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

public class InvisibilitySpell extends AbstractPotionSpell {
    public InvisibilitySpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = caster.getEyeLocation();
        final List<LivingEntity> list = new ArrayList<>(location.getNearbyLivingEntities(3));
        list.removeIf(living -> !WitchcraftAPI.minecraft.isAlly(living, caster));
        final PotionEffect effect = new PotionEffect(this.getPotion(), (int) (20 * (18 + (amplitude * 4))), 0, false, false, true);
        final ParticleCreator creator = this.getCreator();
        for (LivingEntity entity : list) entity.addPotionEffect(effect);
        final Vector axis = new Vector(0, 1, 0);
        WitchcraftAPI.executor.submit(() -> {
            for (int i = 1; i < 3; i++) {
                final VectorShape shape = creator.createCircle(axis, 1 + i, i * 7);
                shape.draw(location);
                WitchcraftAPI.sleep(30);
            }
        });
    }

    @Override
    public PotionEffectType getPotion() {
        return PotionEffectType.INVISIBILITY;
    }

    @Override
    public Color getColor() {
        return new Color(124, 253, 253);
    }
}
