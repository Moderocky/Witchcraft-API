package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.spell.effect.Polygon;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class FangsSpell extends AbstractTargetedSpell {

    protected final Polygon shape = UnholyBlastSpell.PURPLE.createPolygon(new Vector(0, 1, 0), 1, 5);

    public FangsSpell(Map<String, Object> map) {
        super(map);
        if (shape != null) shape.fillInLines(true, 0.2);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Target target = this.getTarget(caster, 15, true, Mode.NOT_ALLIES);
        final Location end;
        if (target.entity() != null) end = target.entity().getLocation();
        else end = target.target();
        final double damage = 4 + amplitude;
        Minecraft.getInstance().spawnFangs(caster, end, range, scale, damage);
        this.shape.draw(caster.getLocation().add(0, 0.2, 0));
    }

}
