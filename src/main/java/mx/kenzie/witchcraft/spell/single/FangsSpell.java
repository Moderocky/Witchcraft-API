package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.spell.effect.Polygon;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class FangsSpell extends AbstractEntitySpell {
    
    protected final Polygon shape = UnholyBlastSpell.PURPLE.createPolygon(new Vector(0, 1, 0), 1, 5);
    
    public FangsSpell(Map<String, Object> map) {
        super(map);
        if (shape != null) shape.fillInLines(true, 0.2);
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final double damage = 3 + amplitude;
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 0.6F, 0.6F);
        Minecraft.getInstance().spawnFangs(caster, target, range, scale, damage);
        this.shape.draw(caster.getLocation().add(0, 0.2, 0));
    }
    
}
