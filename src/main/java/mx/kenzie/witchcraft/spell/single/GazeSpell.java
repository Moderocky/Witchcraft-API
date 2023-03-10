package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

import static net.kyori.adventure.sound.Sound.Source;
import static net.kyori.adventure.sound.Sound.sound;
import static org.bukkit.Sound.ENTITY_SKELETON_HORSE_DEATH;

public class GazeSpell extends AbstractTargetedSpell {
    public GazeSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final AbstractTargetedSpell.Target trace = this.getTarget(caster, range, false, Mode.NOT_ALLIES);
        if (trace == null) return;
        final Location target = trace.target();
        final Entity found = trace.entity();
        this.explode(target);
        WitchcraftAPI.minecraft.damageEntitySafely(found, caster, 2.5 + amplitude, EntityDamageEvent.DamageCause.MAGIC);
    }

    private void explode(final Location target) {
        final PotionEffect effect = new PotionEffect(PotionEffectType.CONFUSION, 40, 1, false, false, false);
        for (LivingEntity entity : target.getNearbyLivingEntities(3)) entity.addPotionEffect(effect);
        final Sound sound = sound().type(ENTITY_SKELETON_HORSE_DEATH)
            .volume(1).pitch(1).source(Source.AMBIENT)
            .build();
        target.getWorld().playSound(sound, target.getX(), target.getY(), target.getZ());
        WitchcraftAPI.client.particles(Particle.SPELL_WITCH).createSphere(1.3, 32).draw(target.add(0, 1.6, 0));
    }
}
