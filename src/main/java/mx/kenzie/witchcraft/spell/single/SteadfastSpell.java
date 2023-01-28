package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.Polygon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Map;

public class SteadfastSpell extends StandardSpell {
    public transient final ParticleCreator creator = ParticleCreator.of(Particle.CRIT.builder().count(0));

    public SteadfastSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final EntityEquipment equipment = caster.getEquipment();
        final Location location = caster.getEyeLocation();
        if (equipment == null) return;
        for (ItemStack stack : equipment.getArmorContents()) this.enchant(stack);
        caster.getWorld().playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.7F, 0.8F);
        WitchcraftAPI.executor.submit(() -> {
            final Polygon polygon = creator.createPolygon(new Vector(0, 1, 0), 1, 6);
            polygon.fillInLines(false, 0.2);
            for (int i = 0; i < 5; i++) {
                location.add(0, -0.2, 0);
                this.creator.draw(location, polygon);
            }
        });
    }

    protected void enchant(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return;
        if (item.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) return;
        if (!Enchantment.PROTECTION_ENVIRONMENTAL.canEnchantItem(item)) return;
        item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
    }
}
