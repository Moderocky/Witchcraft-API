package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.Projectile;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class SummonDarkfireSpell extends BaneSpell {
    public SummonDarkfireSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        WitchcraftAPI.executor.submit(() -> {
            for (int i = 0; i < Math.max(4, Math.min(12, 9 * scale)); i++) {
                final Vector direction = caster.getLocation().getDirection().add(ParticleCreator.random().multiply(0.2))
                    .normalize().multiply(1.2);
                final Projectile projectile = this.createProjectile(caster, direction, scale, amplitude, range);
                projectile.launch();
                WitchcraftAPI.sleep(300);
            }
        });
    }

}
