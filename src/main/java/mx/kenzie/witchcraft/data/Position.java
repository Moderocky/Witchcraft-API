package mx.kenzie.witchcraft.data;

import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.item.Rarity;
import mx.kenzie.witchcraft.data.item.Tag;
import mx.kenzie.witchcraft.data.world.WorldData;
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
        if (!this.isValid()) return new ItemStack(Material.AIR);
        final WorldData data = WorldData.getData(this.getWorld());
        final ItemStack item = data.create();
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(this.displayName());
        final Block block = this.getLocation().getBlock();
        meta.lore(List.of(
            Component.text(block.getWorld().getName())
                .color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
            Component.text("(" + block.getX() + ", " + block.getY() + ", " + block.getX() + ")")
                .color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
            Component.text(""),
            data.getRealmType().color(data.rarity().color())
                .decoration(TextDecoration.ITALIC, false)
        ));
        if (this instanceof Person person && meta instanceof SkullMeta skull)
            skull.setPlayerProfile(person.player.getPlayerProfile());
        item.setItemMeta(meta);
        return item;
    }
    
    boolean isValid();
    
    World getWorld();
    
    Component displayName();
    
    @Override
    default boolean isEmpty() {
        return true;
    }
    
    record Major(Location getLocation, String name, String texture) implements Position {
        
        @Override
        public boolean isValid() {
            return true;
        }
        
        @Override
        public World getWorld() {
            return getLocation.getWorld();
        }
        
        @Override
        public Component displayName() {
            return Component.text(name);
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
        public Location getLocation() {
            if (location != null) return location;
            return location = new Location(this.getWorld(), x, y, z);
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
    }
    
    record Person(Player player) implements Position {
        
        @Override
        public Location getLocation() {
            return player.getLocation();
        }
        
        @Override
        public boolean isValid() {
            return player.isOnline();
        }
        
        @Override
        public World getWorld() {
            return player.getWorld();
        }
        
        @Override
        public Component displayName() {
            return player.displayName();
        }
    }
    
}
