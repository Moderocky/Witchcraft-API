package mx.kenzie.witchcraft.data.world;

import com.destroystokyo.paper.profile.PlayerProfile;
import mx.kenzie.fern.Fern;
import mx.kenzie.fern.meta.Name;
import mx.kenzie.sloth.Cache;
import mx.kenzie.witchcraft.data.LazyWrittenData;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.item.ItemMaterial;
import mx.kenzie.witchcraft.data.item.Rarity;
import mx.kenzie.witchcraft.data.item.Tag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;

public class WorldData extends LazyWrittenData implements ItemArchetype {
    // Mortal realm 6a4bdb660d934b3d8be5993790f26ee805dcaa65336671cfdba7f6fe37775179
    // Demon realm 6ec58c1efdbd3307632022638d8db5bf63635cbc620b26e5d4d2f3fe32284cfd
    // Death realm d23fe3671b3c916c2f24fdcbebd7cf131aad38673a56be63e0ee50932efad84d
    protected static final Map<UUID, WorldData> ALL = new HashMap<>();
    protected static final Cache<World, WorldData> DATA = Cache.soft(WeakHashMap::new);
    protected static final String DEFAULT_HASH = "71208163dbbefe6a9f4d4e19c39c89d84d5cb2897e5d924983b38f6ab94163";
    public String id, name = "Unknown World", description = "", hash = DEFAULT_HASH;
    public boolean major_realm, pocket_realm, safe, indestructible;
    public @Name("protected") boolean restricted;
    protected transient UUID uuid;
    protected transient World world;
    
    public WorldData() {
    }
    
    public WorldData(World world) {
        this.world = world;
        this.uuid = world.getUID();
        this.id = world.key().asString();
        this.name = world.getName();
    }
    
    public static void reloadAll() {
        if (ALL.size() > 0) {
            for (WorldData value : ALL.values()) value.save();
            ALL.clear();
        }
        final File folder = new File("data/world/");
        if (!folder.exists()) folder.mkdirs();
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (!file.getName().endsWith(".fern")) continue; // ignore data system files.
            final UUID uuid = UUID.fromString(file.getName().substring(0, file.getName().indexOf('.')));
            final WorldData data = new WorldData();
            data.file = file;
            data.load();
            try (final Fern fern = new Fern(new FileInputStream(file))) {
                fern.toObject(data);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            data.uuid = uuid;
            ALL.put(uuid, data);
        }
    }
    
    public static WorldData getData(UUID uuid) {
        return ALL.get(uuid);
    }
    
    public static WorldData getData(World world) {
        final WorldData initial = WorldData.DATA.get(world);
        if (initial != null) return initial;
        final WorldData second = ALL.get(world.getUID());
        if (second != null) {
            WorldData.DATA.put(world, second);
            return second;
        }
        final WorldData data = new WorldData(world);
        data.uuid = world.getUID();
        data.file = new File("data/world/" + data.uuid + ".fern");
        data.load();
        WorldData.ALL.put(data.uuid, data);
        WorldData.DATA.put(world, data);
        data.scheduleSave();
        return data;
    }
    
    public World getWorld() {
        if (world != null) return world;
        return world = Bukkit.getWorld(uuid);
    }
    
    @Override
    public boolean isProtected() {
        return restricted;
    }
    
    @Override
    public Set<Tag> tags() {
        return Collections.emptySet();
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public Rarity rarity() {
        if (pocket_realm) return Rarity.UNCOMMON;
        if (major_realm) return Rarity.EPIC;
        return Rarity.VERY_RARE;
    }
    
    @Override
    public String id() {
        return id;
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
        components.add(this.getRealmType().color(this.rarity().color())
            .decoration(TextDecoration.ITALIC, false));
        return components;
    }
    
    @Override
    public String description() {
        return description;
    }
    
    @Override
    public ItemStack create() {
        final ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(this.itemName());
        meta.lore(this.itemLore());
        if (meta instanceof SkullMeta skull) {
            final PlayerProfile profile = hash != null
                ? ItemMaterial.createProfile(hash)
                : ItemMaterial.createProfile(DEFAULT_HASH);
            skull.setPlayerProfile(profile);
        }
        item.setItemMeta(meta);
        return item;
    }
    
    public Component getRealmType() {
        if (pocket_realm) return Component.text("Pocket Realm");
        if (major_realm) return Component.text("Major Realm");
        return Component.text("Minor Realm");
    }
}
