package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RaiseWallSpell extends StandardSpell {

    public RaiseWallSpell(Map<String, Object> map) {
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
                    this.drawBlock(point, this.getMaterial());
                }
                WitchcraftAPI.sleep(200);
            }
        });
    }

    protected Material getMaterial() {
        final Material[] materials = new Material[]{Material.MUD, Material.DIRT, Material.ROOTED_DIRT, Material.COARSE_DIRT};
        return materials[ThreadLocalRandom.current().nextInt(materials.length)];
    }

    protected void drawBlock(Location location, Material material) {
        final Location start = location.add(0, -4, 0);
        final ParticleCreator creator = ParticleCreator.of(material);
        WitchcraftAPI.executor.submit(() -> {
            final ParticleBuilder builder = creator.getBuilder();
            for (Vector vector : creator.createLine(start, location, 0.2)) {
                final Location point = start.clone().add(vector);
                builder.location(point).spawn();
                WitchcraftAPI.sleep(50);
            }
            Minecraft.getInstance().ensureMain(() -> {
                final Block block = location.getBlock();
                if (block.isEmpty()) block.setType(material, false);
            });
        });
    }

}
