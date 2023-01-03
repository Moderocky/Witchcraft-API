package mx.kenzie.witchcraft.data;

import com.destroystokyo.paper.profile.PlayerProfile;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.item.ItemMaterial;
import mx.kenzie.witchcraft.data.item.Rarity;
import mx.kenzie.witchcraft.data.item.Tag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface Position extends ItemArchetype {
    
    World getWorld();
    
    default void teleport(LivingEntity entity) {
        entity.teleportAsync(this.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
    
    Location getLocation();
    
    @Override
    default boolean isProtected() {
        return true;
    }
    
    @Override
    default Set<Tag> tags() {
        return Collections.emptySet();
    }
    
    @Override
    default String name() {
        return "Location";
    }
    
    @Override
    default Rarity rarity() {
        return Rarity.UNCOMMON;
    }
    
    @Override
    default String id() {
        return "position";
    }
    
    @Override
    default String description() {
        return "";
    }
    
    @Override
    default ItemStack create() {
        final ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(this.displayName());
        if (this.isValid()) {
            final Block block = this.getLocation().getBlock();
            meta.displayName(this.displayName());
            meta.lore(List.of(
                Component.text(block.getWorld().getName())
                    .color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("(" + block.getX() + ", " + block.getY() + ", " + block.getX() + ")")
                    .color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
            ));
            if (meta instanceof SkullMeta skull) {
                final PlayerProfile profile;
                if (this instanceof Major major) profile = ItemMaterial.createProfile(major.texture);
                else if (this instanceof Person person) profile = person.player.getPlayerProfile();
                else
                    profile = ItemMaterial.createProfile("6a4bdb660d934b3d8be5993790f26ee805dcaa65336671cfdba7f6fe37775179");
                skull.setPlayerProfile(profile);
            }
        }
        item.setItemMeta(meta);
        return item;
    }
    
    Component displayName();
    
    boolean isValid();
    
    @Override
    default boolean isEmpty() {
        return true;
    }
    
    record Major(Location getLocation, String name, String texture) implements Position {
        // Mortal realm 6a4bdb660d934b3d8be5993790f26ee805dcaa65336671cfdba7f6fe37775179
        // Demon realm 6ec58c1efdbd3307632022638d8db5bf63635cbc620b26e5d4d2f3fe32284cfd
        // Death realm d23fe3671b3c916c2f24fdcbebd7cf131aad38673a56be63e0ee50932efad84d
        
        @Override
        public World getWorld() {
            return getLocation.getWorld();
        }
        
        @Override
        public Component displayName() {
            return Component.text(name);
        }
        
        @Override
        public boolean isValid() {
            return true;
        }
    }
    
    class Static implements Position {
        public UUID world;
        public double x, y, z;
        public String name;
        protected transient Location location;
        
        public Static() {
        }
        
        public Static(Location location) {
            this.location = location;
            this.world = location.getWorld().getUID();
            this.x = location.getX();
            this.y = location.getY();
            this.z = location.getZ();
            this.name = "Known Location";
        }
        
        @Override
        public boolean isValid() {
            return location != null || Bukkit.getWorld(world) != null;
        }
        
        @Override
        public World getWorld() {
            return Bukkit.getWorld(world);
        }
        
        @Override
        public Component displayName() {
            return Component.text(name);
        }
        
        @Override
        public Location getLocation() {
            if (location != null) return location;
            return location = new Location(this.getWorld(), x, y, z);
        }
    }
    
    record Person(Player player) implements Position {
        
        @Override
        public World getWorld() {
            return player.getWorld();
        }
        
        @Override
        public Location getLocation() {
            return player.getLocation();
        }
        
        @Override
        public Component displayName() {
            return player.displayName();
        }
        
        @Override
        public boolean isValid() {
            return player.isOnline();
        }
    }
    
}
