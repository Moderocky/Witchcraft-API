package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.entity.FloatingBlock;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class BurningSpheresSpell extends StandardSpell {

    public BurningSpheresSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final int amount = (int) Math.max(1, Math.min(5, 4 * scale));
        final double damage = 3 + amplitude;
        final Minecraft minecraft = Minecraft.getInstance();
        final Location start = caster.getLocation().add(0, 0.7, 0);
        final float angle = (float) (Math.toRadians(start.getYaw()) - (Math.PI / amount)); // it all went wrong from like here onwards tbh
        final double tau = 2 * Math.PI;
        for (int i = 0; i < amount + 1; ++i) {
            final double radius = 2.5;
            final double theta = angle + i / ((double) 12) * tau + tau / 5;
            final Location point = new Location(start.getWorld(),
                start.getX() + Math.cos(theta) * radius,
                start.getY(),
                start.getZ() + Math.sin(theta) * radius, (float) (theta * 180.0 / Math.PI), 0);
            final FloatingBlock block = minecraft.spawnFloatingBlock(point, caster, FloatingBlock.Type.BURNING_SPHERE, Material.MAGMA_BLOCK);
            block.getEquipment().setHelmet(ItemArchetype.of("magma_ore").create());
            block.setDamage(damage);
            block.setVelocity(start.getDirection());
        }
        caster.getWorld().playSound(start, Sound.BLOCK_FIRE_AMBIENT, 2.5F, 0.8F);
    }

}
