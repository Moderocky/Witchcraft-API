package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Flags;
import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DemonBallsSpell extends StandardSpell {
    public static final int TIME_LIFE = 200;

    public DemonBallsSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return !Flags.read(caster).contains(Flags.SPELL_DEMON_BALLS);
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        Minecraft.getInstance().spawnDemonBalls(caster, 3 + amplitude);
        Flags.of(caster).add(Flags.SPELL_DEMON_BALLS);
        WitchcraftAPI.scheduler.schedule(
            () -> Flags.of(caster).remove(Flags.SPELL_DEMON_BALLS),
            TIME_LIFE * 50,
            TimeUnit.MILLISECONDS);
    }

}
