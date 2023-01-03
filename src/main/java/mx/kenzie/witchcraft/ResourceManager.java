package mx.kenzie.witchcraft;

import com.moderocky.mask.gui.GUI;
import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.data.item.Item;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.item.ItemMaterial;
import mx.kenzie.witchcraft.data.item.Tag;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ResourceManager {
    TextColor DEFAULT_COLOUR = TextColor.color(123, 130, 135);
    
    static ItemArchetype convert(ItemStack stack) {
        return (WitchcraftAPI.resources.getArchetype(stack));
    }
    
    ItemArchetype getArchetype(ItemStack stack);
    
    static String getId(ItemStack stack) {
        return (WitchcraftAPI.resources.getCustomId(stack));
    }
    
    String getCustomId(ItemStack stack);
    
    static boolean hasTag(ItemStack stack, String tag) {
        if (!WitchcraftAPI.resources.isCustom(stack)) return false;
        final Tag found = Tag.parse(tag);
        if (found == null) return false;
        return WitchcraftAPI.resources.getArchetype(stack).hasTag(tag);
    }
    
    boolean isCustom(ItemStack stack);
    
    static String pascalCase(String string) {
        String[] words = string.split("\\s");
        StringBuilder capitaliseWord = new StringBuilder();
        for (String word : words) {
            String first = word.substring(0, 1);
            String substring = word.substring(1);
            capitaliseWord.append(first.toUpperCase()).append(substring.toLowerCase()).append(" ");
        }
        return capitaliseWord.toString().trim();
    }
    
    ItemStack back();
    
    ItemStack next();
    
    ItemStack search();
    
    ItemArchetype getArchetype(String id);
    
    void loadButtons();
    
    Item getItem(String id);
    
    PaginatedGUI showRecipeList(Player player);
    
    PaginatedGUI showAllEntries();
    
    PaginatedGUI showAllEntries(Tag filter, GUI back, boolean all);
    
    ItemMaterial getMaterial(String id);
    
    boolean isMaterial(ItemStack stack);
    
    PaginatedGUI showAllItems(Tag filter);
    
    boolean isCustomItem(ItemStack stack);
    
    PaginatedGUI searchAllByTag();
    
    void populateItems(Collection<Item> array);
    
    void populateMaterials(List<?> array);
    
    int add(Inventory inventory, ItemStack itemStack);
    
    int sum(int... ints);
    
    boolean isCustom(ItemStack stack, String id);
    
    Set<Item> getItems();
    
    ItemArchetype getMaterial(ItemStack stack);
    
    ItemArchetype getItem(ItemStack stack);
    
    boolean hasMaterial(String id);
    
    Set<ItemMaterial> getMaterials();
    
    void update(ItemStack stack);
    
    void update(ItemStack stack, ItemArchetype archetype);
}
