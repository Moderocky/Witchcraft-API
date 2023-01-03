package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

abstract class AbstractProjectileSpell extends StandardSpell {
    public AbstractProjectileSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final AbstractProjectile projectile = this.createProjectile(caster, scale, amplitude);
        if (projectile.getMotion().lengthSquared() == 0) {
            final Vector direction = caster.getLocation().getDirection().normalize().multiply(0.8);
            projectile.setMotion(direction);
        }
        projectile.setMaxRange(range);
        WitchcraftAPI.plugin.projectiles().add(projectile);
        projectile.onLaunch();
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    public abstract AbstractProjectile createProjectile(LivingEntity caster, float scale, double amplitude);
    
}
