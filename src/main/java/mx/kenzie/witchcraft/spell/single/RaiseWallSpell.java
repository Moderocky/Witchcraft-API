package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.FloatingBlock;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class ShieldSpell extends StandardSpell {

    protected transient final Material material = Material.LIGHT_BLUE_STAINED_GLASS;

    public ShieldSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final int size = (int) (3 + Math.ceil(scale * 2));
        final Location eye = caster.getEyeLocation(), start = eye.clone();
        WitchcraftAPI.executor.submit(() -> {
            final Vector direction = eye.getDirection();
            start.add(direction.clone().multiply(size));
            final float factor = 26.56F, space = factor * (2.0F / size);
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    final Location point = eye.clone();
                    point.setYaw(point.getYaw() - factor + (space * x));
                    point.setPitch(point.getPitch() + (space * y));
                    point.add(point.getDirection().multiply(size));
                    final FloatingBlock block = Minecraft.getInstance()
                        .spawnFloatingBlock(point, caster, FloatingBlock.Type.ROTATING, Material.LIGHT_BLUE_STAINED_GLASS);
                    block.getBuilder().count(0).particle(Particle.WATER_BUBBLE).data(null);
                }
                WitchcraftAPI.sleep(200);
            }
        });
    }

}
