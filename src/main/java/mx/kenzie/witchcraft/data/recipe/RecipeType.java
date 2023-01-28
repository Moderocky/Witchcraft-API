package mx.kenzie.witchcraft.data.recipe;

import mx.kenzie.witchcraft.ResourceManager;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.achievement.Achievement;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.item.Rarity;
import mx.kenzie.witchcraft.data.item.Tag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public interface RecipeType {

    TextColor[] COLORS = new TextColor[]{
        TextColor.color(239, 66, 92),
        TextColor.color(243, 130, 57),
        TextColor.color(241, 201, 53),
        TextColor.color(190, 243, 66),
        TextColor.color(116, 241, 133),
        TextColor.color(82, 250, 230),
        TextColor.color(82, 177, 250),
        TextColor.color(112, 91, 229),
        TextColor.color(159, 102, 236)
    };

    default boolean isShaped() {
        return true;
    }

    default boolean isComplex() {
        return false; // i mean this whole fucking thing is complex but...
    }

    default TextColor getColor(Ingredient ingredient) {
        int i = List.of(this.ingredients()).indexOf(ingredient);
        return i >= 0 ? COLORS[i] : ResourceManager.DEFAULT_COLOUR;
    }

    String id();

    default TextColor[] getColorGrid() {
        final Ingredient[] ingredients = this.ingredients();
        final TextColor[] strings = new TextColor[9];
        Arrays.fill(strings, ResourceManager.DEFAULT_COLOUR);
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i] == null || ingredients[i].isEmpty) continue;
            strings[i] = this.getColor(ingredients[i]);
        }
        return strings;
        // ◆
    }

    default ItemStack getIcon(Player player) {
        final ItemStack result = this.getResult().clone();
        final ItemMeta meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.values());
        final PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(WitchcraftAPI.plugin.getKey("recipe_id"), PersistentDataType.STRING, this.id());
        if (this.canComplete(player))
            container.set(WitchcraftAPI.plugin.getKey("can_complete"), PersistentDataType.BYTE, (byte) 1);
        meta.displayName(Component.textOfChildren(
            meta.displayName() != null ? Objects.requireNonNull(meta.displayName()) : Component.translatable(result),
            Component.text(" ").color(NamedTextColor.WHITE),
            Component.text("×" + result.getAmount()).color(NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false)
        ));
        final TextColor[] colors = this.getColorGrid();
        final List<Component> lore = new ArrayList<>();
        final List<Ingredient> ingredients = Arrays.asList(this.ingredients());
        if (this.isShaped()) {
            lore.add(Component.text(""));
            lore.add(Component.text("Arrangement").color(ResourceManager.DEFAULT_COLOUR)
                .decoration(TextDecoration.ITALIC, false));
            lore.add(Component.textOfChildren(Component.text("⬛").color(colors[0]), Component.text("⬛")
                .color(colors[1]), Component.text("⬛").color(colors[2])).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.textOfChildren(Component.text("⬛").color(colors[3]), Component.text("⬛")
                .color(colors[4]), Component.text("⬛").color(colors[5])).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.textOfChildren(Component.text("⬛").color(colors[6]), Component.text("⬛")
                .color(colors[7]), Component.text("⬛").color(colors[8])).decoration(TextDecoration.ITALIC, false));
        }
        lore.add(Component.text(""));
        lore.add(Component.text("Ingredients").color(ResourceManager.DEFAULT_COLOUR)
            .decoration(TextDecoration.ITALIC, false));
        for (Ingredient ingredient : this.isShaped() ? new HashSet<>(ingredients) : ingredients) {
            if (ingredient.equals(Ingredient.EMPTY)) continue;
            if (ingredient.getType() == null) continue;
            final Component component = Component.textOfChildren(
                Component.text(this.isShaped() ? "◆ " : " ")
                    .color(this.getColor(ingredient)).decoration(TextDecoration.ITALIC, false),
                switch (ingredient.getType()) {
                    case ID, ITEM -> WitchcraftAPI.resources.getArchetype(ingredient.id).itemName()
                        .color(TextColor.color(104, 130, 161)).decoration(TextDecoration.ITALIC, false);
                    case TAG -> Component.text(ResourceManager.pascalCase(ingredient.tag.replace("_", " ")))
                        .color(TextColor.color(104, 130, 161)).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text(" (Any)").color(NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false));
                    case MATERIAL -> Component.translatable(ingredient.material)
                        .color(TextColor.color(104, 130, 161)).decoration(TextDecoration.ITALIC, false);
                },
                Component.text(" ×" + ingredient.count)
                    .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                ingredient.canComplete(player)
                    ? Component.text(" ✔").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)
                    : Component.text(" ❌").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
            );
            lore.add(component);
        }
        meta.lore(lore);
        result.setItemMeta(meta);
        return result;
    }

    default ItemStack complete(ItemStack[] matrix) {
        final ItemStack stack = this.getResult(matrix).clone();
        final Ingredient[] ingredients = this.ingredients();
        if (ingredients.length < matrix.length) {
            for (int i = 0; i < (matrix.length - ingredients.length) + 1; i++) {
                final Ingredient[] sample = new Ingredient[matrix.length];
                Arrays.fill(sample, Ingredient.EMPTY);
                int j = i;
                for (Ingredient ingredient : ingredients) {
                    sample[j] = ingredient;
                    j++;
                }
                if (this.matches(sample, matrix)) return this.complete(sample, matrix);
            }
        } else {
            return this.complete(ingredients, matrix);
        }
        return stack;
    }

    default ItemStack getResult(ItemStack[] contextMatrix) {
        return this.getResult();
    }

    Ingredient[] ingredients();

    default boolean matches(Ingredient[] ingredients, ItemStack[] contextMatrix) {
        for (int i = 0; i < ingredients.length; i++) if (ingredients[i] == null) ingredients[i] = Ingredient.EMPTY;
        if (contextMatrix.length < ingredients.length) return false;
        for (int i = 0; i < ingredients.length; i++) if (!ingredients[i].matches(contextMatrix[i])) return false;
        return true;
    }

    default ItemStack complete(Ingredient[] ingredients, ItemStack[] matrix) {
        final ItemStack stack = this.getResult(matrix).clone();
        if (matrix.length < ingredients.length) return stack;
        for (int i = 0; i < ingredients.length; i++) {
            if (matrix[i] == null || matrix[i].getType() == Material.AIR) continue;
            matrix[i].setAmount(Math.max(0, matrix[i].getAmount() - ingredients[i].count));
        }
        return stack;
    }

    ItemStack getResult();

    default boolean canComplete(Player player) {
        if (player == null) return false;
        final Ingredient[] ingredients = this.ingredients();
        if (ingredients == null || ingredients.length < 1) return true;
        final List<ItemStack> list = new ArrayList<>(player.getInventory().getSize());
        for (ItemStack stack : player.getInventory()) if (stack != null) list.add(stack.clone());
        for (Ingredient ingredient : ingredients) {
            if (ingredient == null || ingredient.isEmpty) continue;
            final int count = ingredient.remove(list);
            if (count > 0) return false;
        }
        return true;
    }

    default void complete(Player player) {
        if (player == null) return;
        final Ingredient[] ingredients = this.ingredients();
        final PlayerInventory inventory = player.getInventory();
        if (ingredients != null) for (Ingredient ingredient : ingredients) {
            if (ingredient == null || ingredient.isEmpty) continue;
            ingredient.remove(inventory);
        }
        inventory.addItem(this.getResult());
        Achievement.CRAFT_ITEM.give(player);
        if (ingredients != null) for (Ingredient ingredient : ingredients) {
            if (ingredient.rarity() == Rarity.FINITE) Achievement.CRAFT_FINITE_ITEM.give(player);
        }
        final ItemArchetype archetype = ItemArchetype.of(this.getResult());
        if (archetype.hasTag(Tag.parse("arcane"))) Achievement.CRAFT_ARCANE_ITEM.give(player);
    }

    default boolean matches(ItemStack[] contextMatrix) {
        final Ingredient[] ingredients = this.ingredients();
        if (ingredients.length < contextMatrix.length) {
            for (int i = 0; i < (contextMatrix.length - ingredients.length) + 1; i++) {
                final Ingredient[] sample = new Ingredient[contextMatrix.length];
                Arrays.fill(sample, Ingredient.EMPTY);
                int j = i;
                for (Ingredient ingredient : ingredients) {
                    sample[j] = ingredient;
                    j++;
                }
                if (this.matches(sample, contextMatrix)) return true;
            }
            return false;
        } else {
            final Ingredient[] sample = Arrays.copyOf(this.ingredients(), contextMatrix.length);
            for (int i = 0; i < sample.length; i++) if (sample[i] == null) sample[i] = Ingredient.EMPTY;
            return this.matches(sample, contextMatrix);
        }
    }

    default boolean couldMatch(ItemStack[] contextMatrix) {
        final Ingredient[] ingredients = this.ingredients();
        for (int i = 0; i < contextMatrix.length; i++) {
            if (contextMatrix[i] == null || contextMatrix[i].getType() == Material.AIR) continue;
            if (!ingredients[i].matchesBarAmount(contextMatrix[i])) return false;
        }
        return true;
    }

    default ItemStack[] exampleIngredients() {
        final Ingredient[] ingredients = this.ingredients();
        final ItemStack[] stacks = new ItemStack[9];
        int i;
        for (i = 0; i < ingredients.length; i++) stacks[i] = ingredients[i].getExample();
        if (i < 9) for (int j = 0; j < 9 - i; j++) stacks[j] = Ingredient.EMPTY.getExample();
        return stacks;
    }

    default String getGroup() {
        return "";
    }

    default boolean isLocked() {
        return false;
    }

}
