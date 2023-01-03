package mx.kenzie.witchcraft.data.item;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.LearnedSpell;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ItemArchetype {
    
    Set<LearnedSpell> EMPTY_SET = new HashSet<>();
    
    Pattern LINE = Pattern.compile("\\G\\s*(.{1," + 32 + "})(?=\\s|$)", Pattern.DOTALL);
    
    static @NotNull ItemArchetype of(ItemStack stack) {
        if (stack == null) return BukkitMaterial.AIR;
        if (stack.getType() == Material.AIR) return BukkitMaterial.AIR;
        final ItemArchetype sample = WitchcraftAPI.resources.getArchetype(stack);
        if (sample != null) return sample;
        return new BukkitMaterial(stack.getType());
    }
    
    static @NotNull ItemArchetype of(String id) {
        if (id == null) return BukkitMaterial.AIR;
        final ItemArchetype sample = WitchcraftAPI.resources.getArchetype(id);
        if (sample != null) return sample;
        return BukkitMaterial.AIR;
    }
    
    boolean isProtected();
    
    default boolean hasTag(String tag) {
        return this.tags().contains(Tag.parse(tag));
    }
    
    Set<Tag> tags();
    
    default boolean hasTag(Tag tag) {
        return this.tags().contains(tag);
    }
    
    default Component itemName() {
        return Component.text(this.name()).color(this.rarity().color()).decoration(TextDecoration.ITALIC, false);
    }
    
    String name();
    
    Rarity rarity();
    
    String id();
    
    default List<Component> itemLore() {
        final List<String> lines = new ArrayList<>(4);
        final Matcher matcher = LINE.matcher(this.description());
        while (matcher.find()) lines.add(matcher.group(1));
        final List<Component> components = new ArrayList<>(lines.size() + 2);
        for (String line : lines)
            components.add(Component.text(line, Style.style()
                .color(TextColor.color(200, 200, 200))
                .decoration(TextDecoration.ITALIC, true).build()));
        components.add(Component.text(""));
        components.add(Component.text(this.rarity().qualifiedName()).color(this.rarity().color())
            .decoration(TextDecoration.ITALIC, false));
        return components;
    }
    
    String description();
    
    default void update(ItemStack item) {
        if (item == null) return;
        final ItemStack template = this.create();
        if (template == null) return;
        if (item.getType() != template.getType()) item.setType(template.getType());
        item.setItemMeta(template.getItemMeta());
    }
    
    ItemStack create();
    
    default boolean isHelmet() {
        return false;
    }
    
    default boolean isMagic() {
        return false;
    }
    
    default Set<LearnedSpell> getSpells() {
        return EMPTY_SET;
    }
    
    default double bonusAmplitude() {
        return 0.0;
    }
    
    default int castRange() {
        return 10;
    }
    
    default int bonusEnergy() {
        return 0;
    }
    
    default boolean isEmpty() {
        return true;
    }
    
}
