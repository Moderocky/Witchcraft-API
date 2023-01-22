package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.entity.Projectile;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

abstract class AbstractProjectileSpell extends StandardSpell {
    public AbstractProjectileSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Projectile projectile = this.createProjectile(caster, scale, amplitude, range);
        projectile.launch();
    }
    
    public abstract Projectile createProjectile(LivingEntity caster, float scale, double amplitude, int range);
    
    protected Projectile spawnProjectile(LivingEntity caster, Vector velocity, float diameter, double range) {
        return Minecraft.getInstance()
            .spawnProjectile(caster, caster.getEyeLocation().add(0, -0.2, 0), velocity, diameter, range);
    }
    
}
