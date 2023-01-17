package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LevitationSpell extends StandardSpell {
    public LevitationSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Color color = new Color(124, 253, 253);
        final Location location = caster.getEyeLocation();
        final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB).offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
            .count(0).force(true);
        final List<LivingEntity> list = new ArrayList<>(location.getNearbyLivingEntities(range, range / 3.0F));
        list.removeIf(living -> !WitchcraftAPI.minecraft.isEnemy(living, caster));
        final PotionEffect effect = new PotionEffect(PotionEffectType.LEVITATION, (int) (20 * (6 + amplitude)), (int) amplitude, true, true, false);
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        for (LivingEntity entity : list) entity.addPotionEffect(effect);
        final Vector axis = new Vector(0, 1, 0);
        WitchcraftAPI.executor.submit(() -> {
            for (int i = 1; i < 10; i++) {
                final VectorShape shape = creator.createCircle(axis, 1 + i, i * 4);
                shape.draw(location);
                WitchcraftAPI.sleep(30);
            }
        });
    }
}
