package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import net.kyori.adventure.sound.Sound;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Map;

import static net.kyori.adventure.sound.Sound.Source;
import static net.kyori.adventure.sound.Sound.sound;
import static org.bukkit.Sound.ENTITY_SKELETON_HORSE_DEATH;

public class GazeSpell extends StandardSpell {
    public GazeSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final World world = caster.getWorld();
        final Location targetLocation;
        final Entity targetEntity;
        compute_target:
        {
            final Location start = caster.getEyeLocation();
            final Vector direction = caster.getLocation().getDirection();
            final RayTraceResult result = world.rayTrace(start, direction, range,
                FluidCollisionMode.NEVER, true, 0.5, entity -> !entity.equals(caster));
            if (result != null) {
                targetEntity = result.getHitEntity();
                targetLocation = result.getHitPosition().toLocation(world);
                break compute_target;
            }
            return;
        }
        this.explode(caster, targetLocation, scale);
        WitchcraftAPI.minecraft.damageEntitySafely(targetEntity, caster, 2 + amplitude, EntityDamageEvent.DamageCause.MAGIC);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    private void explode(Entity caster, final Location target, float scale) {
        target.getWorld().createExplosion(target, 2 * scale, false, false, caster);
        final Sound sound = sound().type(ENTITY_SKELETON_HORSE_DEATH)
            .volume(1).pitch(1).source(Source.AMBIENT)
            .build();
        target.getWorld().playSound(sound, target.getX(), target.getY(), target.getZ());
        WitchcraftAPI.client.particles(Particle.SPELL_WITCH).createSphere(1.3, 32).draw(target.add(0, 1.6, 0));
    }
}
