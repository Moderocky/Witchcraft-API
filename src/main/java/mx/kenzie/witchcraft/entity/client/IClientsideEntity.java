package mx.kenzie.witchcraft.entity.client;

import mx.kenzie.witchcraft.WitchcraftAPI;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Set;
import java.util.UUID;

public interface IClientsideEntity {
    
    void show(Player... players);
    
    void hide(Player... players);
    
    Set<Player> getViewers();
    
    Location getLocation();
    
    void setLocation(Location location);
    
    UUID getUUID();
    
    int getId();
    
    int getRawType();
    
    void updateMetadata();
    
    Object getNMSType();
    
    EntityType getType();
    
    void setType(EntityType type);
    
    default boolean isDead() {
        return false;
    }
    
    default void remove() {
        WitchcraftAPI.client.remove(this);
    }
    
    boolean isInvisible();
    
    void setInvisible(boolean boo);
    
    boolean isGlowing();
    
    void setGlowing(boolean boo);
    
    default void clearEquipment() {
        setEquipment(new ItemStack[6]);
    }
    
    default void setHelmet(ItemStack item) {
        final ItemStack[] equipment = getEquipment();
        equipment[0] = item;
        setEquipment(equipment);
    }
    
    ItemStack[] getEquipment();
    
    void setEquipment(ItemStack[] equipment);
    
    default void setItemInMainHand(ItemStack item) {
        final ItemStack[] equipment = getEquipment();
        equipment[4] = item;
        setEquipment(equipment);
    }
    
    default void setItemInOffHand(ItemStack item) {
        final ItemStack[] equipment = getEquipment();
        equipment[5] = item;
        setEquipment(equipment);
    }
    
    void updateEquipment(Player player);
    
    void velocity(Vector vector);
    
    default void teleport(Location location) {
        move(location);
    }
    
    void move(Location location);
    
}
