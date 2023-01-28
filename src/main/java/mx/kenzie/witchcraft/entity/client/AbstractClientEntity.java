package mx.kenzie.witchcraft.entity.client;

import mx.kenzie.witchcraft.WitchcraftAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractClientEntity implements IClientsideEntity {

    public static final Random RANDOM = new Random();
    public final UUID uuid;
    public final int id;
    public final ItemStack[] equipment = new ItemStack[6];
    public final double[] motion = new double[3];
    public final double[] momentum = new double[3];
    protected final HashSet<Player> players = new HashSet<>();
    public boolean invisible;
    public boolean glowing;
    public Component name;
    public boolean nameVisible = false;
    public boolean silent = true;
    public boolean gravity = false;
    public float yaw;
    public float pitch;
    protected Location location = Bukkit.getWorlds().get(0).getSpawnLocation();
    protected EntityType type = EntityType.PIG;

    public AbstractClientEntity(UUID uuid) {
        this(uuid, getRandom());
    }

    public AbstractClientEntity(UUID uuid, int id) {
        this.uuid = uuid;
        this.id = id;
    }

    public AbstractClientEntity(int id) {
        this(UUID.randomUUID(), id);
    }

    public AbstractClientEntity() {
        this(UUID.randomUUID(), getRandom());
    }

    protected static int getRandom() {
        return 5000 + RANDOM.nextInt(3000);
    }

    @Override
    public void show(Player... players) {
        WitchcraftAPI.client.spawnClientsideEntity(id, location, getType().getName(), players);
        updateMetadata();
        for (Player player : players) {
            if (this.players.contains(player)) continue;
            this.players.add(player);
            updateEquipment(player);
        }
    }

    @Override
    public void hide(Player... players) {
        WitchcraftAPI.client.destroyClientsideEntity(id, players);
        for (Player player : players) {
            this.players.remove(player);
        }
    }

    @Override
    public Set<Player> getViewers() {
        return new HashSet<>(players);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getRawType() {
        return type.getTypeId();
    }

    public abstract void updateMetadata();

    @Override
    public EntityType getType() {
        return type;
    }

    @Override
    public void setType(EntityType type) {
        this.type = type;
    }

    @Override
    public void remove() {
        synchronized (players) {
            this.hide(players.toArray(new Player[0]));
        }
        WitchcraftAPI.client.remove(this);
    }

    @Override
    public boolean isInvisible() {
        return invisible;
    }

    @Override
    public void setInvisible(boolean boo) {
        invisible = boo;
    }

    @Override
    public boolean isGlowing() {
        return glowing;
    }

    @Override
    public void setGlowing(boolean boo) {
        glowing = boo;
    }

    @Override
    public ItemStack[] getEquipment() {
        return equipment;
    }

    public abstract void updateEquipment(Player player);

}
