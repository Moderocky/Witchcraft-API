package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.data.modifier.Modifier;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CooperativeMagicSpell extends HealingRitualSpell {

    public CooperativeMagicSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final int duration = 120 * 20;
        final Minecraft minecraft = Minecraft.getInstance();
        final Location centre = caster.getEyeLocation();
        final World world = caster.getWorld();
        final List<LivingEntity> entities = new ArrayList<>(centre.getNearbyLivingEntities(10, 5));
        entities.removeIf(target -> !minecraft.isAlly(caster, target));
        for (LivingEntity entity : entities) {
            Modifier.get(entity).put("cooperation", Modifier.of(Modifier.Type.BONUS_ENERGY, 2, duration));
        }
        this.doVibration(centre, world, entities);
    }

}
