package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class AbstractTargetedSpell extends StandardSpell {
    
    public AbstractTargetedSpell(Map<String, Object> map) {
        super(map);
    }
    
    protected Target getTarget(LivingEntity caster, int range) {
        final World world = caster.getWorld();
        final Location start = caster.getEyeLocation();
        final Vector direction = caster.getLocation().getDirection();
        final RayTraceResult result = world.rayTrace(start, direction, range, FluidCollisionMode.NEVER, true, 0.5, entity -> !entity.equals(caster));
        if (result == null) return null;
        final Entity found = result.getHitEntity();
        final Location target = result.getHitPosition().toLocation(world);
        return new Target(target, found);
    }
    
    protected record Target(Location target, @Nullable Entity entity) {}
    
}
