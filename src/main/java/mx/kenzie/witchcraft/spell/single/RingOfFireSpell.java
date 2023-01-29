package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Flags;
import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RingOfFireSpell extends StandardSpell {
    public static final int TIME_LIFE = 200;

    public RingOfFireSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return !Flags.read(caster).contains(Flags.SPELL_FIRE_RING);
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_BLAZE_BURN, 1.0F, 0.5F);
        Minecraft.getInstance().spawnFireRing(caster, 1 + amplitude);
        Flags.of(caster).add(Flags.SPELL_FIRE_RING);
        WitchcraftAPI.scheduler.schedule(
            () -> Flags.of(caster).remove(Flags.SPELL_FIRE_RING),
            TIME_LIFE * 50,
            TimeUnit.MILLISECONDS);
    }

}
