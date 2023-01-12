package mx.kenzie.witchcraft.data.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import mx.kenzie.fern.Fern;
import mx.kenzie.fern.meta.Name;
import mx.kenzie.witchcraft.WitchcraftAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

public class ItemMaterial implements ItemArchetype {
    
    public String id;
    public String name;
    public String description;
    public Material material = Material.PLAYER_HEAD;
    public String skinValue;
    public Color color;
    public Rarity rarity;
    public boolean customColor;
    public Set<Tag> tags = new HashSet<>();
    public @Name("protected")
    @mx.kenzie.argo.meta.Name("protected") boolean restricted = true;
    public boolean finite = false;
    public boolean unbreakable = false;
    public int customModelData = 0;
    public short damage = 0;
    public float foodValue = 0.0F;
    public float eatingPitch = 1.3F;
    public String modelPath;
    private PlayerProfile profile = null;
    private ItemStack stack = null;
    private PlaceableMaterial[] building = new PlaceableMaterial[0];
    
    public ItemMaterial() {
    }
    
    public static ItemMaterial fromJson(Map<String, Object> element) {
        return apply(new ItemMaterial(), element);
    }
    
    public static ItemMaterial apply(ItemMaterial item, Map<String, Object> map) {
        item.id = map.get("id").toString();
        item.name = map.get("name").toString();
        item.description = map.get("description").toString();
        if (map.get("rarity") instanceof String string) item.rarity = Rarity.valueOf(string);
        if (map.get("material") instanceof String string) item.material = Material.valueOf(string);
        item.unbreakable = map.get("unbreakable") == Boolean.TRUE;
        String value = map.get("skin_value") instanceof String string ? string : null;
        if (value != null && !value.isEmpty()) {
            if (value.matches("https?://.+"))
                item.skinValue = item.baseConvert(value);
            else if (value.length() < 70)
                item.skinValue = item.baseConvert("http://textures.minecraft.net/texture/" + value);
            else
                item.skinValue = value;
        }
        if (map.get("tags") instanceof List<?> list && list.size() > 0)
            for (Object object : list) item.tags.add(Tag.register(object.toString()));
        if (map.get("model_path") instanceof String string) item.modelPath = string;
        if (map.get("food_value") instanceof Number number) item.foodValue = number.floatValue();
        if (map.get("eating_pitch") instanceof Number number) item.eatingPitch = number.floatValue();
        if (map.get("protected") instanceof Boolean boo) item.restricted = boo;
        if (map.get("finite") instanceof Boolean boo) item.finite = boo;
        if (map.get("model") instanceof Number number) item.customModelData = number.intValue();
        if (map.get("damage") instanceof Number number) item.damage = number.shortValue();
        item.color = new Color(item.rarity.color().value());
        if (map.get("color") instanceof List<?> list) {
            try {
                item.color = new Color((int) list.get(0), (int) list.get(1), (int) list.get(2));
                item.customColor = true;
            } catch (Throwable ignore) {
            }
        }
        if (map.get("building") instanceof List<?> list && list.size() > 0) {
            final List<PlaceableMaterial> materials = new ArrayList<>(list.size());
            for (Object o : list) materials.add(new PlaceableMaterial(Material.valueOf(o.toString())));
            item.building = materials.toArray(new PlaceableMaterial[0]);
        }
        return item;
    }
    
    public String baseConvert(String url) {
        URI actualUrl;
        try {
            actualUrl = new URI(url);
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
        JsonObject urlObj = new JsonObject();
        urlObj.addProperty("url", actualUrl.toString());
        JsonObject skin = new JsonObject();
        skin.add("SKIN", urlObj);
        JsonObject object = new JsonObject();
        object.add("textures", skin);
        return Base64.getEncoder().encodeToString(object.toString().getBytes(StandardCharsets.UTF_8));
    }
    
    public static PlayerProfile createProfile(String texture) {
        texture = baseConvert0("http://textures.minecraft.net/texture/" + texture);
        UUID uuid;
        try {
            uuid = new UUID(
                texture.substring(texture.length() - 20).hashCode(),
                texture.substring(texture.length() - 10).hashCode()
            );
        } catch (Throwable throwable) {
            uuid = UUID.randomUUID();
        }
        final PlayerProfile profile = Bukkit.createProfile(uuid, texture.hashCode() + "");
        profile.completeFromCache();
        profile.setProperty(new ProfileProperty("textures", texture));
//        profile.setProperty(new ProfileProperty("textures", skinValue, skinSignature));
        return profile;
    }
    
    static String baseConvert0(String url) {
        URI actualUrl;
        try {
            actualUrl = new URI(url);
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
        JsonObject urlObj = new JsonObject();
        urlObj.addProperty("url", actualUrl.toString());
        JsonObject skin = new JsonObject();
        skin.add("SKIN", urlObj);
        JsonObject object = new JsonObject();
        object.add("textures", skin);
        return Base64.getEncoder().encodeToString(object.toString().getBytes(StandardCharsets.UTF_8));
    }
    
    public Set<PlaceableMaterial> getBuilding() {
        return new LinkedHashSet<>(List.of(building));
    }
    
    @Override
    public boolean isProtected() {
        return restricted;
    }
    
    public Set<Tag> tags() {
        return tags;
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public Rarity rarity() {
        return rarity;
    }
    
    @Override
    public String id() {
        return id;
    }
    
    public List<Component> itemLore() {
        final List<Component> list = ItemArchetype.super.itemLore();
        if (!tags.isEmpty()) {
            list.add(Component.text("Tags:").color(TextColor.color(200, 200, 200))
                .decoration(TextDecoration.ITALIC, false));
            for (Tag tag : tags) {
                list.add(Component.text("  " + tag.qualifiedName()).color(TextColor.color(255, 232, 146))
                    .decoration(TextDecoration.ITALIC, false));
            }
        }
        return list;
    }
    
    @Override
    public String description() {
        return description;
    }
    
    @Override
    public ItemStack create() {
        if (stack != null) return stack.clone();
        final ItemStack item = stack = new ItemStack(material, 1);
        item.addItemFlags(ItemFlag.values());
        final ItemMeta meta = item.getItemMeta();
        final PersistentDataContainer container = meta.getPersistentDataContainer();
        meta.setDestroyableKeys(new ArrayList<>());
        meta.setPlaceableKeys(new ArrayList<>());
        meta.setCustomModelData(customModelData);
        meta.setUnbreakable(unbreakable);
        meta.displayName(this.itemName());
        meta.lore(this.itemLore());
        container.set(WitchcraftAPI.plugin.getKey("custom_material"), PersistentDataType.BYTE, (byte) 1);
        container.set(WitchcraftAPI.plugin.getKey("custom_id"), PersistentDataType.STRING, id);
        if (restricted) container.set(WitchcraftAPI.plugin.getKey("protected"), PersistentDataType.BYTE, (byte) 1);
        if (meta instanceof SkullMeta skull) skull.setPlayerProfile(this.getProfile());
        if (damage > 0 && meta instanceof Damageable damageable) damageable.setDamage(damage);
        if (customColor && meta instanceof LeatherArmorMeta leather) leather.setColor(convert(color));
        if (customColor && meta instanceof PotionMeta potion) potion.setColor(convert(color));
        item.setItemMeta(meta);
        return item.clone();
    }
    
    public PlayerProfile getProfile() {
        if (profile == null) profile = this.createProfile();
        return profile;
    }
    
    public static org.bukkit.Color convert(Color color) {
        return org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }
    
    private PlayerProfile createProfile() {
        if (!this.isSkull() || (skinValue == null)) return null;
        UUID uuid;
        try {
            uuid = new UUID(
                skinValue.substring(skinValue.length() - 20).hashCode(),
                skinValue.substring(skinValue.length() - 10).hashCode()
            );
        } catch (Throwable throwable) {
            uuid = UUID.randomUUID();
        }
        final PlayerProfile profile = Bukkit.createProfile(uuid, name);
        profile.completeFromCache();
        profile.setProperty(new ProfileProperty("textures", skinValue));
//        profile.setProperty(new ProfileProperty("textures", skinValue, skinSignature));
        return profile;
    }
    
    public boolean isSkull() {
        return material == Material.PLAYER_HEAD;
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    public boolean isFinite() {
        return finite;
    }
    
    public Material material() {
        return material;
    }
    
    @Override
    public String toString() {
        return Fern.out(this, null);
    }
    
    
}
