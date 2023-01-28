package mx.kenzie.witchcraft.data.item;

import mx.kenzie.witchcraft.ResourceManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public record BukkitMaterial(Material material) implements ItemArchetype {
    public static final BukkitMaterial AIR = new BukkitMaterial(Material.AIR);

    @Override
    public boolean isProtected() {
        return true;
    }

    @Override
    public Set<Tag> tags() {
        return new HashSet<>();
    }

    @Override
    public Component itemName() {
        return Component.translatable(material.translationKey());
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
    public String description() {
        return "";
    }

    @Override
    public ItemStack create() {
        return new ItemStack(material);
    }
}
