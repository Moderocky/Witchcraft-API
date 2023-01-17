package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class RainbowRoadSpell extends StandardSpell {
    
    public RainbowRoadSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location point = caster.getLocation().add(0, -1, 0);
        final Vector direction = point.getDirection().multiply(2);
        WitchcraftAPI.executor.submit(() -> {
            for (int i = 0; i < 6; i++) {
                point.add(direction);
                if (point.getBlock().getType() != Material.AIR) continue;
                CustomEntityType.RAINBOW_BRIDGE.spawn(point);
                caster.getWorld().playSound(point, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1F, 1F);
            }
        });
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
}
