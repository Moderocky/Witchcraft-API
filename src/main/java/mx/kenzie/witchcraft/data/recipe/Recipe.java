package mx.kenzie.witchcraft.data.recipe;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class Recipe implements RecipeType {
    
    public final String id;
    public final String group;
    public final Map<String, Object> result;
    public final Ingredient[] ingredients;
    public final boolean locked;
    public transient ItemStack item;
    
    public Recipe(Map<String, Object> map) {
        id = map.get("id") instanceof String string ? string : null;
        result = (Map<String, Object>) map.get("result");
        List<Object> array = (List<Object>) map.get("ingredients");
        ingredients = new Ingredient[array.size()];
        for (int i = 0; i < array.size(); i++) {
            ingredients[i] = Ingredient.fromJson(array.get(i));
        }
        group = map.get("group") instanceof String string ? string : "";
        locked = Boolean.TRUE.equals(map.get("locked"));
        this.getResult(); // generate transient field
    }
    
    @Override
    public String id() {
        return id;
    }
    
    @Override
    public Ingredient[] ingredients() {
        return ingredients;
    }
    
    @Override
    public ItemStack getResult() {
        if (item != null) return item;
        try {
            if (result.get("id") instanceof String string) {
                final ItemArchetype item = WitchcraftAPI.resources.getArchetype(string);
                this.item = item.create();
            } else if (result.get("item") instanceof String string) {
                final ItemArchetype item = WitchcraftAPI.resources.getArchetype(string);
                this.item = item.create();
            } else if (result.get("material") instanceof String string) {
                final Material material = Material.valueOf(string.toUpperCase());
                this.item = new ItemStack(material);
            } else return new ItemStack(Material.AIR);
            this.item.setAmount(result.get("count") instanceof Number number ? number.intValue() : 1);
        } catch (Throwable ex) {
            ex.printStackTrace();
            this.item = new ItemStack(Material.BARRIER, 1);
            this.item.editMeta(meta -> meta.displayName(Component.text("Error")));
        }
        return item;
    }
    
    @Override
    public String getGroup() {
        return group;
    }
    
    @Override
    public boolean isLocked() {
        return locked;
    }
    
    public String lookupItemKey() {
        final String part;
        if (result.get("id") instanceof String string) part = string;
        else if (result.get("item") instanceof String string) part = string;
        else return "";
        final int count = (int) result.getOrDefault("count", 1);
        if (count > 1) return part + " x" + count;
        else return part;
    }
    
    public Ingredient[][] ingredientMatrix() {
        final Ingredient[][] matrix = new Ingredient[3][3];
        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 3; x++) {
                final int index = 3 * y + x;
                if (ingredients.length > index) matrix[y][x] = ingredients[index];
                else matrix[y][x] = Ingredient.EMPTY;
            }
        return matrix;
    }
    
}
