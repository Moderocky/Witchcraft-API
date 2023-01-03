package com.moderocky.mask.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * An item-creating factory to allow for easy itemstack creation/manipulation within a single
 * object.
 * This makes use of chain methods as well as consumers to simplify the process.
 */
public class ItemFactory {
    
    private final @NotNull List<Consumer<ItemMeta>> consumers = new ArrayList<>();
    private @NotNull Material material;
    private @NotNull ItemMeta meta;
    private int amount;
    
    /**
     * Creates a default factory for an Air item.
     */
    public ItemFactory() {
        material = Material.AIR;
        amount = 1;
        meta = Bukkit.getItemFactory().getItemMeta(material);
    }
    
    /**
     * Creates an item factory for the given material.
     *
     * @param material Material
     */
    public ItemFactory(@NotNull Material material) {
        this.material = material;
        this.amount = 1;
        this.meta = Bukkit.getItemFactory().getItemMeta(material);
    }
    
    /**
     * Creates an item factory for the given material.
     *
     * @param material Material
     * @param amount   Amount
     */
    public ItemFactory(@NotNull Material material, int amount) {
        this.material = material;
        this.amount = amount;
        this.meta = Bukkit.getItemFactory().getItemMeta(material);
    }
    
    /**
     * Creates an item factory for the given material.
     *
     * @param material Material
     * @param consumer A consumer to be performed on the item when {@link #create()} is used.
     */
    public ItemFactory(@NotNull Material material, @NotNull Consumer<ItemMeta> consumer) {
        this.material = material;
        this.amount = 1;
        this.meta = Bukkit.getItemFactory().getItemMeta(material);
        this.consumers.add(consumer);
    }
    
    /**
     * Creates an item factory for the given material.
     *
     * @param material Material
     * @param amount   Amount
     * @param consumer A consumer to be performed on the item's meta when {@link #create()} is used.
     */
    public ItemFactory(@NotNull Material material, int amount, @NotNull Consumer<ItemMeta> consumer) {
        this.material = material;
        this.amount = amount;
        this.meta = Bukkit.getItemFactory().getItemMeta(material);
        this.consumers.add(consumer);
    }
    
    /**
     * Creates an item factory based on the given item.
     * Remember: any edits will not be applied to this item.
     * <p>
     * You can use {@link #apply(ItemStack)} to apply the edits to an item.
     *
     * @param itemStack The template item stack to use.
     */
    public ItemFactory(@NotNull ItemStack itemStack) {
        this.material = itemStack.getType();
        this.meta = itemStack.getItemMeta();
        this.amount = itemStack.getAmount();
    }
    
    /**
     * Set the final amount. This will be bounded between 0 and 127 inclusively.
     *
     * @param i Amount
     * @return Chain
     */
    public ItemFactory setAmount(int i) {
        amount = i;
        return this;
    }
    
    /**
     * Adds a consumer that can be run on the meta upon creation or application.
     * An unlimited number of consumers can be added here.
     *
     * @param consumer A consumer to be run on the item meta
     * @return Chain
     */
    public ItemFactory addConsumer(Consumer<ItemMeta> consumer) {
        consumers.add(consumer);
        return this;
    }
    
    /**
     * Applies this factory to an existing item.
     * If the material differs, the meta will be converted to match this new item.
     * <p>
     * The material of an existing item cannot be changed.
     *
     * @param itemStack An item
     * @return Chain
     */
    public ItemFactory apply(ItemStack itemStack) {
        ItemMeta meta = this.meta.clone();
        try {
            for (Consumer<ItemMeta> consumer : consumers) {
                consumer.accept(meta);
            }
        } catch (Throwable ignore) {
        }
        itemStack.setAmount(Math.max(0, Math.min(127, amount)));
        itemStack.setItemMeta(meta);
        return this;
    }
    
    /**
     * @return The given material
     */
    public @NotNull Material getMaterial() {
        return material;
    }
    
    /**
     * Re-sets the material.
     * Will attempt to convert the existing item meta to match the new material.
     * If the meta cannot be converted, it will be ignored.
     *
     * @param material A material
     * @return Chain
     */
    public ItemFactory setMaterial(@NotNull Material material) {
        ItemMeta meta = Bukkit.getItemFactory().asMetaFor(this.meta, material);
        if (meta == null) meta = Bukkit.getItemFactory().getItemMeta(material);
        this.material = material;
        this.meta = meta;
        return this;
    }
    
    /**
     * @return The meta, with any given consumers applied
     */
    public @NotNull ItemMeta getItemMeta() {
        ItemMeta meta = this.meta.clone();
        try {
            for (Consumer<ItemMeta> consumer : consumers) {
                consumer.accept(meta);
            }
        } catch (Throwable ignore) {
        }
        return meta;
    }
    
    /**
     * This creates a new item given the provided material and amounts.
     * Any consumers will then be applied to its meta, and the meta will be
     * applied to the item.
     * <p>
     * Where possible, any invalid meta edits will be skipped to preserve
     * the process.
     *
     * @return The newly-created item
     */
    public @NotNull ItemStack create() {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(Math.max(0, Math.min(127, amount)));
        ItemMeta meta = this.meta.clone();
        for (Consumer<ItemMeta> consumer : consumers) {
            try {
                consumer.accept(meta);
            } catch (Throwable ignore) {
            }
        }
        try {
            itemStack.setItemMeta(meta);
        } catch (Throwable ignore) {
        }
        return itemStack;
    }
    
}
