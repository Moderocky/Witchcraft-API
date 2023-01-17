package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ShillelaghSpell extends StandardSpell {
    public transient final ParticleBuilder builder = new ParticleBuilder(Particle.ENCHANTMENT_TABLE)
        .count(1)
        .force(true);
    
    public ShillelaghSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final EntityEquipment equipment = caster.getEquipment();
        if (equipment == null) return;
        final ItemStack off = equipment.getItemInOffHand(), main = equipment.getItemInMainHand();
        final Random random = ThreadLocalRandom.current();
        for (int i = 0; i < 8; i++) {
            final Location location = caster.getEyeLocation();
            this.builder.location(location)
                .offset(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5)
                .spawn();
        }
        if (this.enchant(off)) return; // we try to enchant the offhand
        this.enchant(main); // if this doesn't work we enchant the player's magic item
    }
    
    protected boolean enchant(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        if (item.containsEnchantment(Enchantment.DAMAGE_ALL)) return false;
        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        return true;
    }
}
