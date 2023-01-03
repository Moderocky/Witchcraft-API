package mx.kenzie.witchcraft.data.recipe;

import mx.kenzie.witchcraft.ResourceManager;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.item.BukkitMaterial;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.item.Rarity;
import mx.kenzie.witchcraft.data.item.Tag;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.security.MessageDigest;
import java.util.*;

public class Ingredient implements ItemArchetype {
    
    public static final Ingredient EMPTY = new Ingredient(null, null, 0);
    private final Type type;
    public String id = null;
    public String item = null;
    public String tag = null;
    public Material material = null;
    public boolean isEmpty = false;
    public int count = 1;
    public ItemArchetype inner;
    
    public <T> Ingredient(Type type, T choice, int count) {
        this(type, choice);
        this.count = count;
    }
    
    public <T> Ingredient(Type type, T choice) {
        this.type = type;
        if (choice == null || type == null) {
            isEmpty = true;
            this.inner = BukkitMaterial.AIR;
            return;
        }
        switch (type) {
            case ID -> id = (String) choice;
            case ITEM -> item = (String) choice;
            case TAG -> tag = (String) choice;
            case MATERIAL -> material = (Material) choice;
        }
        this.inner = switch (type) {
            case ID -> WitchcraftAPI.resources.getArchetype(id);
            case ITEM -> WitchcraftAPI.resources.getArchetype(item);
            case TAG -> {
                for (ItemArchetype item : WitchcraftAPI.resources.getMaterials()) if (item.hasTag(tag)) yield item;
                for (ItemArchetype item : WitchcraftAPI.resources.getItems()) if (item.hasTag(tag)) yield item;
                yield new BukkitMaterial(Material.AIR);
            }
            case MATERIAL -> new BukkitMaterial(material);
        };
    }
    
    public static Ingredient fromJson(Object element) {
        if (!(element instanceof Map<?, ?> map) || map.size() < 1) return new Ingredient(null, null, 0);
        final Map<String, Object> object = (Map<String, Object>) element;
        final int count = object.get("count") instanceof Number number ? number.intValue() : 1;
        if (object.get("id") instanceof String string) return new Ingredient(Type.ID, string, count);
        else if (object.get("item") instanceof String string) return new Ingredient(Type.ITEM, string, count);
        else if (object.get("tag") instanceof String string) return new Ingredient(Type.TAG, string, count);
        else if (object.get("material") instanceof String string)
            return new Ingredient(Type.MATERIAL, Material.valueOf(string.toUpperCase()), count);
        else return new Ingredient(null, null, 0);
    }
    
    public int getCount() {
        return count;
    }
    
    public Type getType() {
        return type;
    }
    
    public ItemStack getExample() {
        if (isEmpty) return new ItemStack(Material.AIR);
        final ItemStack stack = inner.create();
        if (stack == null) return new ItemStack(Material.AIR);
        if (stack.getType() != Material.AIR) stack.setAmount(count);
        return stack;
    }
    
    public ItemStack[] getAllPossibilities() {
        if (isEmpty) return new ItemStack[] {new ItemStack(Material.AIR)};
        final List<ItemStack> items = new ArrayList<>();
        ItemStack stack;
        switch (type) {
            case ID -> {
                stack = WitchcraftAPI.resources.getMaterial(id).create();
                stack.setAmount(count);
                items.add(stack.clone());
            }
            case ITEM -> {
                stack = WitchcraftAPI.resources.getItem(id).create();
                stack.setAmount(count);
                items.add(stack.clone());
            }
            case TAG -> {
                for (ItemArchetype item : WitchcraftAPI.resources.getMaterials()) {
                    if (item.hasTag(tag)) {
                        stack = item.create();
                        stack.setAmount(count);
                        items.add(stack.clone());
                    }
                }
            }
            case MATERIAL -> {
                stack = new ItemStack(material);
                stack.setAmount(count);
                items.add(stack.clone());
            }
        }
        return items.toArray(new ItemStack[0]);
    }
    
    public boolean matchesBarAmount(ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR) return isEmpty;
        ItemArchetype item;
        if (WitchcraftAPI.resources.isCustom(stack)) {
            item = WitchcraftAPI.resources.getMaterial(WitchcraftAPI.resources.getCustomId(stack));
        } else item = null;
        return this.checkType(stack, item);
    }
    
    private boolean checkType(ItemStack stack, ItemArchetype item) {
        if (type == null) return (stack == null || stack.getType() == Material.AIR);
        switch (type) {
            case ID, ITEM -> {
                if (item == null) return false;
                return item.id().equalsIgnoreCase(this.id);
            }
            case TAG -> {
                if (item == null) return false;
                return item.hasTag(this.tag);
            }
            case MATERIAL -> {
                return stack.getType() == material;
            }
        }
        return false;
    }
    
    public boolean matches(ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR) return isEmpty;
        if (stack.getAmount() < count) return false;
        final ItemArchetype item = WitchcraftAPI.resources.getArchetype(stack);
        return this.checkType(stack, item);
    }
    
    public int remove(Iterable<ItemStack> inventory) {
        if (this.isEmpty()) return 0;
        if (inventory == null) return 0;
        if (this.type == null) return 0;
        int count = this.count;
        for (ItemStack stack : inventory) {
            final boolean matches = switch (type) {
                case MATERIAL -> (stack.getType() == material);
                case ID -> this.id.equals(ResourceManager.getId(stack));
                case ITEM -> this.item.equals(ResourceManager.getId(stack));
                case TAG -> ResourceManager.hasTag(stack, this.tag);
            };
            if (!matches) continue;
            if (stack.getAmount() > count) {
                stack.setAmount(stack.getAmount() - count);
                count = 0;
            } else {
                count -= stack.getAmount();
                stack.setAmount(0);
            }
            if (count < 1) break;
        }
        return count;
    }
    
    public boolean canComplete(Player player) {
        if (this.isEmpty()) return true;
        if (player == null) return false;
        if (this.type == null) return true;
        int count = this.count;
        for (ItemStack stack : player.getInventory()) {
            final boolean matches = switch (type) {
                case MATERIAL -> (stack.getType() == material);
                case ID -> this.id.equals(ResourceManager.getId(stack));
                case ITEM -> this.item.equals(ResourceManager.getId(stack));
                case TAG -> ResourceManager.hasTag(stack, this.tag);
            };
            if (!matches) continue;
            if (stack.getAmount() > count) {
                count = 0;
                break;
            } else {
                count -= stack.getAmount();
            }
            if (count < 1) break;
        }
        return count < 1;
    }
    
    public TextColor getColor() {
        final long hash = Math.abs(this.hash(this.toString()));
        final String sample = (hash + "111111111").substring(0, 12);
        final int r = Math.min(240, Math.max(60, (Integer.parseInt(sample.substring(0, 3)) / 3) - 1));
        final int g = Math.min(240, Math.max(60, (Integer.parseInt(sample.substring(4, 7)) / 3) + 5));
        final int b = Math.min(250, Math.max(65, (Integer.parseInt(sample.substring(8, 11)) / 3) + 15));
        return TextColor.color(r, g, b);
    }
    
    private long hash(final String str) {
        return this.hash(str.getBytes());
    }
    
    public long hash(final byte[] buf) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            final byte[] digest = md.digest(buf);
            return (this.getLong(digest, 0) ^ this.getLong(digest, 8));
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
    
    //
    private long getLong(final byte[] array, final int offset) {
        long value = 0;
        for (int i = 0; i < 8; i++) value = ((value << 8) | (array[offset + i] & 0xFF));
        return value;
    }
    
    public String lookupItemKey() {
        if (type == null) return "";
        final String part;
        if (id != null) part = id;
        else if (item != null) part = item;
        else if (tag != null) part = "tag:" + tag;
        else if (material != null) part = "builtin:" + material;
        else return "";
        if (this.count > 1) return part + " x" + count;
        else return part;
    }
    
    public TextColor getColor(RecipeType recipe) {
        return recipe.getColor(this);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(count, id, item, tag, material, type, isEmpty);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient that)) return false;
        return isEmpty == that.isEmpty &&
            count == that.count &&
            Objects.equals(id, that.id) &&
            Objects.equals(item, that.item) &&
            Objects.equals(tag, that.tag) &&
            material == that.material &&
            type == that.type;
    }
    
    @Override
    public String toString() {
        return "Ingredient{" +
            "count=" + count +
            ", type=" + type +
            ", id='" + id + '\'' +
            ", item='" + item + '\'' +
            ", tag='" + tag + '\'' +
            ", material=" + material +
            ", isEmpty=" + isEmpty +
            '}';
    }
    
    @Override
    public boolean isProtected() {
        return inner.isProtected();
    }
    
    @Override
    public Set<Tag> tags() {
        return inner.tags();
    }
    
    @Override
    public String name() {
        return inner.name();
    }
    
    @Override
    public Rarity rarity() {
        return inner.rarity();
    }
    
    @Override
    public String id() {
        return inner.id();
    }
    
    @Override
    public String description() {
        return inner.description();
    }
    
    @Override
    public ItemStack create() {
        return inner.create();
    }
    
    public boolean isEmpty() {
        return isEmpty;
    }
    
    public enum Type {
        ID,
        ITEM,
        TAG,
        MATERIAL
    }
}
