package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import mx.kenzie.witchcraft.spell.projectile.MagicProjectile;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TrueStrikeSpell extends AbstractProjectileSpell {
    public TrueStrikeSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public AbstractProjectile createProjectile(LivingEntity caster, float scale, double amplitude) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = 1 + amplitude;
        return new MagicProjectile(caster, location, damage) {
            @Override
            public void onTick() {
                world.spawnParticle(Particle.BLOCK_CRACK, getLocation(), 0, Material.ICE.createBlockData());
            }
            
            @Override
            public void onLaunch() {
                world.playSound(location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.6F, 0.8F);
            }
            
            @Override
            public void explode(Location location) {
                Random random = ThreadLocalRandom.current();
                world.playSound(location, Sound.BLOCK_GLASS_BREAK, 0.8F, 0.8F);
                for (int i = 0; i < 6; i++) {
                    Location loc = location.clone()
                        .add(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
                    location.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 0, Material.ICE.createBlockData());
                }
            }
        }.setDiameter(0.4);
    }
}
