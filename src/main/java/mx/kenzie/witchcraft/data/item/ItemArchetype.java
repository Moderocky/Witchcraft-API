package mx.kenzie.witchcraft.data.item;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.LearnedSpell;
import mx.kenzie.witchcraft.data.outfit.OutfitData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
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
        final PersistentDataContainer container = stack.getItemMeta().getPersistentDataContainer();
        if (container.has(WitchcraftAPI.plugin.getKey("building"))) return new PlaceableMaterial(stack.getType());
        return new BukkitMaterial(stack.getType());
    }
    
    static @NotNull ItemArchetype of(String id) {
        if (id == null || id.isBlank()) return BukkitMaterial.AIR;
        final ItemArchetype sample = WitchcraftAPI.resources.getArchetype(id);
        if (sample != null) return sample;
        return BukkitMaterial.AIR;
    }
    
    default boolean isOutfit() {
        return false;
    }
    
    default OutfitData asOutfit() {
        return null;
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
        components.add(this.typeDescriptor().decoration(TextDecoration.ITALIC, false));
        return components;
    }
    
    default Component typeDescriptor() {
        return (this instanceof ItemMaterial
            ? Component.text(this.rarity().qualifiedName() + " Material")
            : this.hasTag("staff")
            ? Component.text(this.rarity().qualifiedName() + " Staff")
            : this.hasTag("wand")
            ? Component.text(this.rarity().qualifiedName() + " Wand")
            : this.hasTag("sword")
            ? Component.text(this.rarity().qualifiedName() + " Sword")
            : this.hasTag("arcane")
            ? Component.text(this.rarity().qualifiedName() + " Artefact")
            : this.hasTag("helmet")
            ? Component.text(this.rarity().qualifiedName() + " Helmet")
            : this instanceof Item
            ? Component.text(this.rarity().qualifiedName() + " Item")
            : Component.text(this.rarity().qualifiedName())
        ).color(this.rarity().color());
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
    
    default void giveSafely(Player player) {
        final HashMap<Integer, ItemStack> rest = player.getInventory().addItem(this.create());
        if (rest.size() > 0) {
            final Location location = player.getLocation();
            for (ItemStack value : rest.values()) {
                location.getWorld().spawn(location, org.bukkit.entity.Item.class, item -> {
                    item.setItemStack(value);
                    item.setOwner(player.getUniqueId());
                    item.setInvulnerable(true);
                    item.setCanMobPickup(false);
                });
            }
        }
    }
    
    default MutableArchetype mutate() {
        return new MutableArchetype(this);
    }
    
}
