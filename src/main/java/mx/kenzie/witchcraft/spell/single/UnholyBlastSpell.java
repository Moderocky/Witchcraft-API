package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import mx.kenzie.witchcraft.spell.projectile.MagicProjectile;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.Map;

public class UnholyBlastSpell extends AbstractProjectileSpell {
    
    protected transient final Color color = new Color(231, 32, 57);
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB)
        .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
        .count(0)
        .force(true);
    
    public UnholyBlastSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public AbstractProjectile createProjectile(LivingEntity caster, float scale, double amplitude) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = 3 + amplitude;
        final Vector axis = location.getDirection();
        final ParticleCreator purple = WitchcraftAPI.client.particles(new ParticleBuilder(Particle.SPELL_WITCH).count(0)
            .force(true));
        final VectorShape circle = purple.createCircle(axis, 0.4, 10);
        return new MagicProjectile(caster, location, damage) {
            @Override
            public void onTick() {
                builder.location(this.getLocation()).spawn();
                purple.draw(this.getLocation(), circle);
            }
            
            @Override
            public void onLaunch() {
                world.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 0.7F, 0.7F);
            }
            
            @Override
            public void explode(Location location) {
                world.playSound(location, Sound.ENTITY_SILVERFISH_HURT, 0.8F, 0.4F);
                for (int i = 0; i < 6; i++) {
                    final Location loc = location.clone()
                        .add(ParticleCreator.random());
                    builder.location(loc).spawn();
                }
            }
        }.setDiameter(1.2);
    }
}
