package mx.kenzie.witchcraft.data.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IngredientList extends ArrayList<Ingredient> {
    
    public boolean matches(ItemStack... stacks) {
        final List<Ingredient> sample = new ArrayList<>(this);
        final List<ItemStack> workingCopy = new ArrayList<>(List.of(stacks));
        workingCopy.removeIf(this::isInvalid);
        if (workingCopy.size() != sample.size()) return false;
        sample.removeIf(ingredient -> {
            final List<ItemStack> matching = new ArrayList<>(workingCopy);
            matching.removeIf(test -> !ingredient.matches(test));
            if (matching.size() < 1) return false;
            if (matching.size() == 1) {
                workingCopy.remove(matching.get(0));
                return true;
            }
            return false;
        });
        final Ingredient[] test = sample.toArray(new Ingredient[0]);
        final ItemStack[] working = workingCopy.toArray(new ItemStack[0]);
        if (test.length != working.length) return false;
        for (int i = 0; i < test.length; i++) {
            boolean boo = true;
            for (int j = 0; j < working.length; j++) if (!test[j].matches(working[j])) boo = false;
            if (boo) return true;
            this.rotateArray(working);
        }
        return false;
    }
    
    private boolean isInvalid(ItemStack stack) {
        return stack == null || stack.getType() == Material.AIR;
    }
    
    private <T> void rotateArray(T[] array) {
        T[] copy = Arrays.copyOf(array, array.length);
        for (int i = 0; i < array.length; i++) {
            int index = (i + 1) == array.length ? 0 : i + 1;
            array[index] = copy[i];
        }
    }
    
    private void sorting(List<Ingredient> ingredients) {
        ingredients.sort((o1, o2) -> {
            if (o1 == null || o1.getType() == null) return 1;
            if (o2 == null || o2.getType() == null) return -1;
            if (o1.getType() == Ingredient.Type.MATERIAL && o2.getType() != Ingredient.Type.MATERIAL)
                return -1;
            if (o1.getType() == Ingredient.Type.MATERIAL) {
                return o1.material.compareTo(o2.material);
            } else if (o1.getType() == Ingredient.Type.ID && o2.getType() != Ingredient.Type.ID)
                return -1;
            if (o1.getType() == Ingredient.Type.ID) {
                return o1.id.compareTo(o2.id);
            } else if (o1.getType() == Ingredient.Type.TAG && o2.getType() != Ingredient.Type.TAG)
                return -1;
            if (o1.getType() == Ingredient.Type.TAG) {
                return o1.tag.compareTo(o2.tag);
            } else return o1.item.compareTo(o2.item);
        });
    }
    
    private void sort(List<ItemStack> stacks) {
    
    }
    
}
