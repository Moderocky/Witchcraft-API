package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.Color;
import java.util.Map;

public class AfflictionSpell extends AbstractProjectileSpell {
    
    protected transient final Color color = new Color(43, 243, 21);
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB)
        .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
        .count(0)
        .force(true);
    
    public AfflictionSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public AbstractProjectile createProjectile(LivingEntity caster, float scale, double amplitude) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final ParticleCreator creator = WitchcraftAPI.client.particles(builder);
        final PotionEffect effect = new PotionEffect(PotionEffectType.POISON, 20 * 6, (int) Math.ceil(amplitude), false, true, true);
        return new AbstractProjectile(caster, location) {
            @Override
            public void onTick() {
                creator.drawPoof(location, 0.5, 3);
            }
            
            @Override
            public void onCollide(Entity entity) {
                world.playSound(location, Sound.ENTITY_SILVERFISH_HURT, 0.8F, 0.4F);
                if (entity instanceof LivingEntity living)
                    Bukkit.getScheduler().callSyncMethod(WitchcraftAPI.plugin, () -> living.addPotionEffect(effect));
                creator.drawPoof(entity.getLocation(), 1, 12);
            }
            
            @Override
            public void onCollide(Block block) {
                creator.drawPoof(block.getLocation(), 1, 12);
            }
            
            @Override
            public void onLaunch() {
                world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 0.6F, 1.4F);
            }
        }.setDiameter(1.2);
    }
}
