package mx.kenzie.witchcraft.data;

import mx.kenzie.witchcraft.ResourceManager;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.item.Rarity;
import mx.kenzie.witchcraft.data.item.Tag;
import mx.kenzie.witchcraft.data.recipe.Ingredient;
import mx.kenzie.witchcraft.spell.Spell;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class LearnedSpell implements ItemArchetype {
    public int level = 1;
    public String id;
    private transient Spell spell;

    public LearnedSpell() {
    }

    public LearnedSpell(Spell spell) {
        this(spell, 1);
    }

    public LearnedSpell(Spell spell, int level) {
        this.id = spell.getId();
        this.spell = spell;
        this.level = level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LearnedSpell spell)) return false;
        return Objects.equals(id, spell.id);
    }

    @Override
    public boolean isProtected() {
        return true;
    }

    @Override
    public Set<Tag> tags() {
        return new HashSet<>();
    }

    @Override
    public String name() {
        return spell.getName();
    }

    @Override
    public Rarity rarity() {
        return Rarity.values()[Math.min(Math.max(0, spell.getPoints() - 4), Rarity.values().length)];
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String description() {
        return spell.getDescription();
    }

    @Override
    public Component typeDescriptor() {
        return Component.text("Level " + spell.getPoints() + " " + spell.getType().qualifiedName())
            .color(this.rarity().color());
    }

    @Override
    public ItemStack create() {
        final ItemStack item = (switch (this.getStyle()) {
            case HEDGE_WITCH -> ItemArchetype.of("wild_spell_book");
            case NECROMANCER -> ItemArchetype.of("necromancy_spell_book");
            case GLADIOMAGUS -> ItemArchetype.of("battle_spell_book");
            case THAUMATURGE -> ItemArchetype.of("thaumaturgy_spell_book");
            case WARLOCK -> ItemArchetype.of("warlock_spell_book");
            case DIVINE -> ItemArchetype.of("divine_spell_book");
            default -> ItemArchetype.of("pure_spell_book");
        }).create();
        item.addItemFlags(ItemFlag.values());
        item.setAmount(Math.max(1, Math.min(127, level)));
        final ItemMeta meta = item.getItemMeta();
        final TextColor color = spell.getType().color;
        if (color != null && meta instanceof PotionMeta potion) potion.setColor(Color.fromRGB(color.value()));
        meta.displayName(this.itemName());
        final List<Component> lore = new ArrayList<>();
        lore.add(Component.text(" ⚡ " + spell.getEnergy() + " Energy")
            .color(TextColor.color(255, 225, 28)).decoration(TextDecoration.ITALIC, false));
        for (Ingredient ingredient : this.spell.getMaterials()) {
            if (ingredient.equals(Ingredient.EMPTY)) continue;
            if (ingredient.getType() == null) continue;
            final Component component = Component.textOfChildren(
                Component.text(" "),
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
                    .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
            );
            lore.add(component);
        }
        lore.add(Component.empty());
        lore.addAll(this.itemLore());
        lore.add(Component.empty());
        lore.add(Component.text("  " + spell.getPatternPicture()).color(NamedTextColor.GOLD)
            .decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        lore.add(Component.empty());
        lore.add(Component.empty());
        meta.lore(lore);
        meta.getPersistentDataContainer().set(WitchcraftAPI.plugin.getKey("spell_id"), PersistentDataType.STRING, id);
        item.setItemMeta(meta);
        return item;
    }

    public MagicClass getStyle() {
        return this.getSpell().getStyle();
    }

    public Spell getSpell() {
        if (spell != null) return spell;
        return spell = WitchcraftAPI.spells.getSpell(id);
    }

    @Override
    public Set<LearnedSpell> getSpells() {
        return new HashSet<>(List.of(this));
    }
}
