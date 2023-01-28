package mx.kenzie.witchcraft;

import mx.kenzie.witchcraft.data.recipe.RecipeType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface RecipeManager {
    static RecipeManager getInstance() {
        return WitchcraftAPI.recipes;
    }

    boolean hasMatch(ItemStack[] matrix);

    RecipeType getRecipe(ItemStack[] matrix);

    boolean hasPossibleMatches(ItemStack[] matrix);

    List<RecipeType> getPartialMatches(ItemStack[] matrix);

    List<RecipeType> getRecipes();
}
