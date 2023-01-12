package mx.kenzie.witchcraft.data.item;

import com.destroystokyo.paper.Namespaced;
import mx.kenzie.witchcraft.ResourceManager;
import mx.kenzie.witchcraft.WitchcraftAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public record PlaceableMaterial(Material material) implements ItemArchetype {
    
    private static final Set<Namespaced> BLOCK_KEYS = Arrays.stream(Material.values()).filter(Material::isBlock)
        .map(Material::getKey).collect(Collectors.toSet());
    
    public PlaceableMaterial(BukkitMaterial material) {
        this(material.material());
    }
    
    @Override
    public boolean isProtected() {
        return false;
    }
    
    @Override
    public Set<Tag> tags() {
        return new HashSet<>();
    }
    
    @Override
    public Component itemName() {
        return Component.translatable(material.translationKey(), NamedTextColor.DARK_GRAY)
            .decoration(TextDecoration.ITALIC, false);
    }
    
    @Override
    public String name() {
        return ResourceManager.pascalCase(material.name().toLowerCase().replace('_', ' '));
    }
    
    @Override
    public Rarity rarity() {
        return Rarity.COMMON;
    }
    
    @Override
    public String id() {
        return material.name().toLowerCase();
    }
    
    @Override
    public List<Component> itemLore() {
        final List<String> lines = new ArrayList<>(4);
        final Matcher matcher = LINE.matcher(this.description());
        while (matcher.find()) lines.add(matcher.group(1));
        final List<Component> components = new ArrayList<>(lines.size() + 2);
        for (String line : lines)
            components.add(Component.text(line, Style.style()
                .color(TextColor.color(200, 200, 200))
                .decoration(TextDecoration.ITALIC, true).build()));
        components.add(Component.text(""));
        components.add(Component.text("Building Resource", NamedTextColor.DARK_GRAY)
            .decoration(TextDecoration.ITALIC, false));
        return components;
    }
    
    @Override
    public String description() {
        return "Consumes resources when placed.";
    }
    
    @Override
    public ItemStack create() {
        final ItemStack stack = new ItemStack(material);
        stack.editMeta(meta -> {
            meta.displayName(this.itemName());
            meta.lore(this.itemLore());
            meta.setPlaceableKeys(BLOCK_KEYS);
            meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            final PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(WitchcraftAPI.plugin.getKey("ephemeral"), PersistentDataType.BYTE, (byte) 1);
            container.set(WitchcraftAPI.plugin.getKey("building"), PersistentDataType.BYTE, (byte) 1);
        });
        return new ItemStack(material);
    }
}
