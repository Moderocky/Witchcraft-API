package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.entity.Projectile;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.Polygon;
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
    static final ParticleCreator PURPLE = ParticleCreator.of(new ParticleBuilder(Particle.SPELL_WITCH).count(0)
        .force(true));
    
    public UnholyBlastSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public Projectile createProjectile(LivingEntity caster, float scale, double amplitude, int range) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final double damage = 3 + amplitude;
        final Vector axis = location.getDirection();
        final Polygon polygon = PURPLE.createPolygon(axis, 0.4, 5);
        polygon.fillInLines(false, 0.2);
        final Vector direction = location.getDirection().multiply(0.5);
        final Projectile projectile = this.spawnProjectile(caster, direction, 1.2F, range);
        world.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 0.7F, 0.7F);
        projectile.setDamage(damage);
        projectile.onTick(() -> {
            this.PURPLE.getBuilder().location(projectile.getLocation()).spawn();
            this.builder.draw(projectile.getLocation(), polygon);
        });
        projectile.onCollide(() -> {
            world.playSound(location, Sound.ENTITY_SILVERFISH_HURT, 0.8F, 0.4F);
            this.PURPLE.drawPoof(location, 0.8, 10);
        });
        return projectile;
    }
}
