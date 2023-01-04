package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.data.Coven;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class PortalHomeSpell extends AbstractPortalSpell {
    
    public PortalHomeSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0, 0.5, 0);
        location.setDirection(location.toVector().subtract(caster.getLocation().toVector()));
        location.setPitch(0);
        final Coven coven = Coven.getCoven(caster);
        this.doPortal(coven.getHome(), location);
        this.target = null;
    }
    
    
    @Override
    public boolean canCast(LivingEntity caster) {
        final Coven coven = Coven.getCoven(caster);
        if (coven == null) return false;
        if (coven.getHome() == null) return false;
        return super.canCast(caster);
    }
}
