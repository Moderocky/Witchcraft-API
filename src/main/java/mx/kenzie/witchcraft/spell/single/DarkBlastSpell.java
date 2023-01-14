package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import mx.kenzie.witchcraft.spell.projectile.MagicProjectile;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.awt.*;
import java.util.Map;

public class DarkBlastSpell extends AbstractProjectileSpell {
    
    protected transient final Color color = new Color(33, 5, 44);
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.REDSTONE)
        .color(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()))
        .count(0)
        .force(true);
    
    public DarkBlastSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public AbstractProjectile createProjectile(LivingEntity caster, float scale, double amplitude) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        final double damage = 3 + amplitude;
        return new MagicProjectile(caster, location, damage) {
            @Override
            public void onTick() {
                final Location point = location.clone();
                point.setPitch(point.getPitch() + 90);
                creator.drawPlate(point, 0.5, 3);
            }
            
            @Override
            public void onLaunch() {
                world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 0.6F, 1.4F);
            }
            
            @Override
            public void explode(Location location) {
                world.playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 0.4F, 0.4F);
                creator.drawPoof(location, 1, 12);
            }
        }.setDiameter(1.1);
    }
}
