package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.Position;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class PortalSpawnSpell extends AbstractPortalSpell {

    public PortalSpawnSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0, 0.5, 0);
        location.setDirection(location.toVector().subtract(caster.getLocation().toVector()));
        location.setPitch(0);
        final Position position = WitchcraftAPI.plugin.getSpawn();
        this.doPortal(position, location);
        this.target = null;
    }
}
