package mx.kenzie.witchcraft.spell.single;

import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.spell.StandardSpell;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Map;

public class WonderworkSpell extends StandardSpell {
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
            .setLore().create(), (clicker, event) -> {
            clicker.closeInventory();
            // todo item
        });
        gui.createButton('B', ItemArchetype.of("work_material_plus").mutate()
            .setName(Component.text("Conjure Material")
                .decoration(TextDecoration.ITALIC, false))
            .setLore().create(), (clicker, event) -> {
            clicker.closeInventory();
            // todo material
        });
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
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player;
    }
}
