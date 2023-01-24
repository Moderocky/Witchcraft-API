package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
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
import java.util.function.BiFunction;
import java.util.function.Predicate;

public abstract class AbstractTargetedSpell extends StandardSpell {
    
    public AbstractTargetedSpell(Map<String, Object> map) {
        super(map);
    }
    
    protected Target getTarget(LivingEntity caster, int range) {
        return this.getTarget(caster, range, false);
    }
    
    protected Target getTarget(LivingEntity caster, int range, boolean strikeAir) {
        return this.getTarget(caster, range, strikeAir, Mode.SAFE_TO_TARGET);
    }
    
    public static Target findTarget(LivingEntity caster, Vector direction, int range, boolean strikeAir, Mode mode) {
        final World world = caster.getWorld();
        final Location start = caster.getEyeLocation();
        final Predicate<Entity> predicate = entity -> mode.filter.apply(entity, caster);
        final RayTraceResult result = world.rayTrace(start, direction, range, FluidCollisionMode.NEVER, true, 0.5, predicate);
        if (strikeAir && result == null) return new Target(start.clone().add(direction.clone().multiply(range)), null);
        else if (result == null) return null;
        final Entity found = result.getHitEntity();
        final Location target = result.getHitPosition().toLocation(world);
        return new Target(target, found, result);
    }
    
    protected Target getTarget(LivingEntity caster, int range, boolean strikeAir, Mode mode) {
        return findTarget(caster, caster.getEyeLocation().getDirection(), range, strikeAir, mode);
    }
    
    protected Target getTarget(LivingEntity caster, Vector direction, int range, boolean strikeAir, Mode mode) {
        return findTarget(caster, direction, range, strikeAir, mode);
    }
    
    public enum Mode {
        ALL((entity, caster) -> !entity.equals(caster)),
        ALLIES_ONLY((entity, caster) -> !entity.equals(caster)
            && !Minecraft.getInstance().isSameVehicle(entity, caster)
            && Minecraft.getInstance().isAlly(entity, caster)),
        ENEMIES_ONLY((entity, caster) -> !entity.equals(caster)
            && !Minecraft.getInstance().isSameVehicle(entity, caster)
            && Minecraft.getInstance().isEnemy(entity, caster)),
        NOT_ALLIES((entity, caster) -> !entity.equals(caster)
            && !Minecraft.getInstance().isSameVehicle(entity, caster)
            && !Minecraft.getInstance().isAlly(entity, caster)),
        NOT_ENEMIES((entity, caster) -> !entity.equals(caster)
            && !Minecraft.getInstance().isSameVehicle(entity, caster)
            && !Minecraft.getInstance().isEnemy(entity, caster)),
        SAFE_TO_TARGET((entity, caster) -> !entity.equals(caster)
            && !Minecraft.getInstance().isSameVehicle(entity, caster)
            && Minecraft.getInstance().isValidToDamage(entity));
        
        public final BiFunction<Entity, LivingEntity, Boolean> filter;
        
        Mode(BiFunction<Entity, LivingEntity, Boolean> filter) {
            this.filter = filter;
        }
    }
    
    public record Target(Location target, @Nullable Entity entity, RayTraceResult result) {
        private Target(Location target, @Nullable Entity entity) {
            this(target, entity, null);
        }
    }
    
}
