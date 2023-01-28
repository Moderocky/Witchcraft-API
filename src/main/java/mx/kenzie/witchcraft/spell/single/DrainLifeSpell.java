package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Map;

public class DrainLifeSpell extends AbstractEntitySpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(Material.REDSTONE_BLOCK);
    protected transient final VectorShape circle = creator.createCircle(new Vector(0, 1, 0), 0.8, 26);

    public DrainLifeSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        assert target != null;
        final Minecraft minecraft = Minecraft.getInstance();
        final Location centre = target.getLocation().add(0, 0.2, 0), start = caster.getLocation().add(0, 2, 0);
        this.target.setVelocity(new Vector(0, 0, 0));
        caster.setVelocity(new Vector(0, 0, 0));
        final double missing = minecraft.getMaxHealth(caster) - caster.getHealth();
        minecraft.heal(caster, Math.min(missing, target.getHealth()));
        minecraft.damageEntity(target, caster, missing, EntityDamageEvent.DamageCause.MAGIC);
        final World world = caster.getWorld();
        world.playSound(centre, Sound.BLOCK_LAVA_AMBIENT, 1.2F, 1.0F);
        WitchcraftAPI.executor.submit(() -> {
            for (int i = 1; i < 8; i++) {
                start.subtract(0, 0.2, 0);
                centre.add(0, 0.2, 0);
                this.creator.draw(centre, circle);
                this.creator.draw(start, circle);
                final Vibration vibration = new Vibration(new Vibration.Destination.EntityDestination(caster), 20);
                world.spawnParticle(Particle.VIBRATION, centre, 1, vibration);
                WitchcraftAPI.sleep(100);
            }
        });
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        final Minecraft minecraft = Minecraft.getInstance();
        return super.canCast(caster) && minecraft.isValidToDamage(target) && !minecraft.isAlly(caster, target);
    }
}
