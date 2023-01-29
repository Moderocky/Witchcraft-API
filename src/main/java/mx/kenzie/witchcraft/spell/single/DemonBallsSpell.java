package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class DemonBallsSpell extends StandardSpell {
    public DemonBallsSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        Minecraft.getInstance().spawnDemonBalls(caster, 3 + amplitude);
    }

}
