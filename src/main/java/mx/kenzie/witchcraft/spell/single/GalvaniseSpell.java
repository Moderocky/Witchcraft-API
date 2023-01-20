package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.data.item.Item;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Map;

public class GalvaniseSpell extends StandardSpell {
    public transient final ParticleCreator creator = ParticleCreator.of(new ParticleBuilder(Particle.TOTEM)
        .count(0)
        .force(true));
    
    public GalvaniseSpell(Map<String, Object> map) {
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
        this.creator.drawPoof(caster.getEyeLocation(), 1, 12);
        caster.getWorld().playSound(caster.getEyeLocation(), Sound.BLOCK_ANVIL_USE, 0.7F, 0.7F);
        if (this.enchant(off)) return; // we try to enchant the offhand
        this.enchant(main); // if this doesn't work we enchant the player's magic item
    }
    
    protected boolean enchant(ItemStack item) {
        final ItemArchetype archetype = ItemArchetype.of(item);
        if (archetype.isEmpty()) return false;
        if (archetype instanceof Item thing && thing.fragile) return false;
        if (!(item.getItemMeta() instanceof Damageable damageable)) return false;
        if (damageable.isUnbreakable()) return false;
        damageable.setUnbreakable(true);
        damageable.setDamage(0);
        item.setItemMeta(damageable);
        return true;
    }
}
