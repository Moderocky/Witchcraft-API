package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.Polygon;
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
    
    protected transient final Color color = new Color(194, 21, 44);
    protected transient final ParticleCreator builder = ParticleCreator.of(new ParticleBuilder(Particle.REDSTONE)
        .color(color.getRed(), color.getGreen(), color.getBlue())
        .count(0)
        .force(true));
    final ParticleCreator purple = ParticleCreator.of(new ParticleBuilder(Particle.SPELL_WITCH).count(0)
        .force(true));
    
    public UnholyBlastSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public AbstractProjectile createProjectile(LivingEntity caster, float scale, double amplitude) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = 3 + amplitude;
        final Vector axis = location.getDirection();
        final Polygon polygon = purple.createPolygon(axis, 0.4, 5);
        polygon.fillInLines(false, 0.2);
        return new MagicProjectile(caster, location, damage) {
            @Override
            public void onTick() {
                purple.getBuilder().location(this.getLocation()).spawn();
                builder.draw(this.getLocation(), polygon);
            }
            
            @Override
            public void onLaunch() {
                world.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 0.7F, 0.7F);
            }
            
            @Override
            public void explode(Location location) {
                world.playSound(location, Sound.ENTITY_SILVERFISH_HURT, 0.8F, 0.4F);
                purple.drawPoof(location, 0.8, 10);
            }
        }.setDiameter(1.2);
    }
}
