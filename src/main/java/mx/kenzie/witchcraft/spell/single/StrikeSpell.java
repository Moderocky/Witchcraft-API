package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import mx.kenzie.witchcraft.spell.projectile.MagicProjectile;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class StrikeSpell extends AbstractProjectileSpell {
    private transient final ParticleBuilder builder = new ParticleBuilder(Particle.WHITE_ASH)
        .count(3).offset(0.1, 0.1, 0.1).force(true);
    
    public StrikeSpell(Map<String, Object> map) {
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
                builder.location(this.getLocation()).spawn();
            }
            
            @Override
            public void onLaunch() {
                world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 0.6F, 1.2F);
            }
            
            @Override
            public void explode(Location location) {
                Random random = ThreadLocalRandom.current();
                world.playSound(location, Sound.BLOCK_GLASS_BREAK, 0.8F, 0.4F);
                for (int i = 0; i < 6; i++) {
                    Location loc = location.clone()
                        .add(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
                    builder.location(loc).spawn();
                }
            }
        }.setDiameter(0.8);
    }
}
