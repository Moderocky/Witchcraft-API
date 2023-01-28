package mx.kenzie.witchcraft.spell.single;

import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.item.ItemMaterial;
import mx.kenzie.witchcraft.data.item.Rarity;
import mx.kenzie.witchcraft.data.recipe.StorageGUI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class FormShapeSpell extends StandardSpell {
    public FormShapeSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player;
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player thing)) return;
        final List<ItemStack> materials = new ArrayList<>(380);
        for (ItemMaterial material : WitchcraftAPI.resources.getMaterials()) {
            if (material.restricted) continue;
            if (material.rarity != Rarity.COMMON && material.rarity != Rarity.UNCOMMON) continue;
            materials.add(material.create());
        }
        final PaginatedGUI gui = new StorageGUI(WitchcraftAPI.plugin, 54, "Form Shape");
        final BiConsumer<Player, InventoryClickEvent> consumer = (clicker, event) -> {
            final ItemStack stack = event.getCurrentItem();
            final ItemArchetype archetype = ItemArchetype.of(stack);
            if (archetype.isEmpty()) return;
            clicker.closeInventory();
            final ItemStack created = archetype.create();
            final Item item = clicker.getWorld().dropItem(clicker.getEyeLocation(), created);
            item.setCanMobPickup(false);
            item.setOwner(clicker.getUniqueId());
            item.setPickupDelay(20);
            item.setThrower(clicker.getUniqueId());
            item.setInvulnerable(true);
        };
        assembleMenu(thing, materials, gui, consumer);
    }
}
