package mx.kenzie.witchcraft.spell.single;

import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.recipe.StorageGUI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class WonderworkSpell extends StandardSpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(Particle.TOTEM);
    
    public WonderworkSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        final PaginatedGUI gui = new PaginatedGUI(WitchcraftAPI.plugin, InventoryType.DISPENSER, "Wonderwork");
        gui.setLayout(new String[] {
            "A#B",
            "###",
            "C#D"
        });
        gui.createButton('A', ItemArchetype.of("work_item_plus").mutate()
            .setName(Component.text("Conjure Item")
                .decoration(TextDecoration.ITALIC, false))
            .setLore().create(), this::spawnItem);
        gui.createButton('B', ItemArchetype.of("work_material_plus").mutate()
            .setName(Component.text("Conjure Material")
                .decoration(TextDecoration.ITALIC, false))
            .setLore().create(), this::spawnMaterial);
        gui.createButton('C', ItemArchetype.of("work_realm_enter").mutate()
            .setName(Component.text("Enter Realm")
                .decoration(TextDecoration.ITALIC, false))
            .setLore().create(), (clicker, event) -> {
            clicker.closeInventory();
            // todo realm
        });
        gui.createButton('D', ItemArchetype.of("work_spell_cast").mutate()
            .setName(Component.text("Cast Spell")
                .decoration(TextDecoration.ITALIC, false))
            .setLore().create(), (clicker, event) -> {
            clicker.closeInventory();
            // todo spell
        });
        gui.finalise();
        gui.open(player);
    }
    
    @SuppressWarnings({"unchecked", "RawUseOfParameterized"})
    protected void spawnItem(Player player, InventoryClickEvent click) {
        this.spawnThing(player, (Set) WitchcraftAPI.resources.getItems());
    }
    
    @SuppressWarnings({"unchecked", "RawUseOfParameterized"})
    protected void spawnMaterial(Player player, InventoryClickEvent click) {
        this.spawnThing(player, (Set) WitchcraftAPI.resources.getMaterials());
    }
    
    private void spawnThing(Player player, Set<ItemArchetype> archetypes) {
        final List<ItemStack> materials = new ArrayList<>(380);
        for (ItemArchetype material : archetypes) {
            if (material.isProtected()) continue;
            if (material.rarity().weight() > 40) continue;
            materials.add(material.create());
        }
        final PaginatedGUI gui = new StorageGUI(WitchcraftAPI.plugin, 54, "Wonderwork") {
            {inventory.setMaxStackSize(127);}
            
            @Override
            public void onInventoryClick(InventoryClickEvent event) {
                event.setCancelled(true);
            }
        };
        final BiConsumer<Player, InventoryClickEvent> consumer = (clicker, event) -> {
            final ItemStack stack = event.getCurrentItem();
            final ItemArchetype archetype = ItemArchetype.of(stack);
            if (archetype.isEmpty()) return;
            clicker.closeInventory();
            this.creator.drawPoof(clicker.getEyeLocation(), 0.5, 10);
            archetype.giveSafely(clicker);
        };
        assembleMenu(player, materials, gui, consumer);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player;
    }
}
