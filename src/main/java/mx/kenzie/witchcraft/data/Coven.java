package mx.kenzie.witchcraft.data;

import mx.kenzie.eris.api.entity.Channel;
import mx.kenzie.sloth.Cache;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.collection.ArrayLinkedSet;
import mx.kenzie.witchcraft.entity.Summon;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.*;

public class Coven extends LazyWrittenData {
    
    private static final Cache<UUID, Coven> COVEN_CACHE = Cache.soft(WeakHashMap::new);
    private static final Cache<Entity, Coven> ENTITY_COVEN_CACHE = Cache.soft(WeakHashMap::new);
    
    protected UUID uuid;
    protected String name;
    protected UUID creator;
    protected UUID[] members = new UUID[0];
    protected Position.Static home;
    protected long discord_id;
    protected transient Team team;
    protected transient Set<UUID> _members = new ArrayLinkedSet<>(members, set -> members = set.toArray(new UUID[0]));
    private transient byte wasHomeValid;
    public transient Channel channel;
    
    public static Coven getCoven(Entity entity) {
        if (entity == null) return null;
        if (ENTITY_COVEN_CACHE.containsKey(entity)) return ENTITY_COVEN_CACHE.get(entity);
        final Coven coven;
        if (entity instanceof Player player) coven = getCoven(player);
        else if (WitchcraftAPI.minecraft.getHandle(entity) instanceof Summon summon) {
            final UUID id = summon.getOwnerID();
            if (id == null) coven = null;
            else {
                final Player player = Bukkit.getPlayer(id);
                if (player != null) coven = getCoven(player);
                else coven = null;
            }
        } else coven = null;
        ENTITY_COVEN_CACHE.put(entity, coven);
        return coven;
    }
    
    public static Coven getCoven(Player player) {
        return getCoven(PlayerData.getData(player).coven);
    }
    
    public static Coven getCoven(UUID id) {
        if (id == null) return null;
        if (COVEN_CACHE.isPresent(id)) return COVEN_CACHE.get(id);
        final Coven coven = new Coven();
        coven.uuid = id;
        coven.file = new File("data/coven/" + coven.uuid + ".fern");
        if (!coven.file.exists()) return null;
        coven.scheduleLoad();
        COVEN_CACHE.put(id, coven);
        return coven;
    }
    
    public static Team getTeam(Player player) {
        final PlayerData data = PlayerData.getData(player.getUniqueId());
        if (data.coven == null) return null;
        final Coven coven = getCoven(data.coven);
        return coven.getTeam();
    }
    
    public Team getTeam() {
        if (team != null) return team;
        final Team here = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(uuid.toString());
        if (here != null) return team = here;
        final Team created = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(uuid.toString());
        created.setCanSeeFriendlyInvisibles(true);
        created.setAllowFriendlyFire(false);
        return team = created;
    }
    
    public static Coven createNewCoven(UUID creator) {
        final Coven coven = new Coven();
        coven.uuid = UUID.randomUUID();
        coven.file = new File("data/coven/" + coven.uuid + ".fern");
        coven.creator = creator;
        coven._members.add(creator);
        final Player player = Bukkit.getPlayer(creator);
        if (player != null) coven.name = player.getName() + "'s Coven";
        coven.finish();
        COVEN_CACHE.put(coven.uuid, coven);
        coven.scheduleSave();
        return coven;
    }
    
    public void sendDiscordMessage(String message) {
        WitchcraftAPI.discord.sendMessage(this, message);
    }
    
    @Override
    public void load() {
        super.load();
        this._members = new ArrayLinkedSet<>(members, set -> members = set.toArray(new UUID[0]));
    }
    
    public long getDiscordId() {
        return discord_id;
    }
    
    public void setDiscordId(long id) {
        this.discord_id = id;
    }
    
    public UUID getId() {
        return uuid;
    }
    
    public boolean isHomeValid(boolean guarantee) {
        if (home == null) return false;
        final World world = home.getWorld();
        final Location location = home.getLocation();
        if (home.getLocation().isChunkLoaded() || guarantee) {
            if (location.getBlock().getType() == Material.ENCHANTING_TABLE) wasHomeValid = 2;
            else wasHomeValid = 1;
        } else if (wasHomeValid == 0) {
            if (home.getLocation().getBlock().getType() == Material.ENCHANTING_TABLE) wasHomeValid = 2;
            else wasHomeValid = 1;
        } else world.getChunkAtAsync(location).thenAccept(chunk -> {
            if (home.getLocation().getBlock().getType() == Material.ENCHANTING_TABLE) wasHomeValid = 2;
            else wasHomeValid = 1;
        });
        return (wasHomeValid == 2);
    }
    
    public Position getHome() {
        return home;
    }
    
    public void setHome(Block block) {
        final Position old = home;
        if (block == null) home = null;
        else home = new Position.Static(block.getLocation(), "Coven Home");
        this.scheduleSave();
        final boolean changed = old != home;
        if (changed && home == null) {
            this.messagePlayers(Component.textOfChildren(
                Component.text(" !! ", WitchcraftAPI.plugin.getColors().pop()),
                Component.text("Your coven home was destroyed.", WitchcraftAPI.plugin.getColors().lowlight())
            ));
            this.sendDiscordMessage("Your coven home was destroyed.");
        } else if (changed) {
            assert block != null;
            final Component position = Component.text("(" + block.getX() + ", " + block.getY() + ", " + block.getX() + ")");
            this.messagePlayers(Component.textOfChildren(
                Component.text(" !! ", WitchcraftAPI.plugin.getColors().pop()),
                Component.text("Your coven home was placed at ", WitchcraftAPI.plugin.getColors().lowlight()),
                position.color(WitchcraftAPI.plugin.getColors().highlight()),
                Component.text(".", WitchcraftAPI.plugin.getColors().lowlight())
            ));
            this.sendDiscordMessage("Your coven home was placed at " + "`" + block.getX() + ", " + block.getY() + ", " + block.getX() + "`.");
        }
    }
    
    public void delete() {
        if (file != null) file.delete();
        COVEN_CACHE.remove(this.uuid);
        this.file = null;
        this.home = null;
        this.members = new UUID[0];
        if (team != null) team.unregister();
        final Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(this.uuid.toString());
        if (team != null) team.unregister();
    }
    
    public void messagePlayers(Component component) {
        for (UUID member : members) {
            final Player player = Bukkit.getPlayer(member);
            if (player == null || !player.isOnline()) continue;
            player.sendMessage(component);
        }
    }
    
    public int size() {
        return _members.size();
    }
    
    public Collection<Position> getPositions() {
        final List<Position> positions = new ArrayList<>(_members.size());
        for (UUID member : members) {
            final Player player = Bukkit.getPlayer(member);
            if (player == null || !player.isOnline()) continue;
            positions.add(new Position.Person(player));
        }
        return positions;
    }
    
    public String getName() {
        return name;
    }
    
    public Component displayName() {
        if (name != null) return Component.text(name);
        return Component.text("Unknown Coven");
    }
    
    public boolean addMember(UUID id) {
        final boolean add = _members.add(id);
        this.scheduleSave();
        return add;
    }
    
    public boolean removeMember(UUID id) {
        final boolean remove = _members.remove(id);
        this.scheduleSave();
        return remove;
    }
    
    public List<OfflinePlayer> getMembers() {
        final List<OfflinePlayer> list = new ArrayList<>(_members.size());
        for (UUID member : members) list.add(Bukkit.getOfflinePlayer(member));
        return list;
    }
    
    public OfflinePlayer getCreator() {
        return Bukkit.getOfflinePlayer(creator);
    }
    
    public boolean isOwner(UUID id) {
        return creator.equals(id);
    }
    
    public boolean isOwner(OfflinePlayer entity) {
        return creator.equals(entity.getUniqueId());
    }
    
    public boolean isOwner(Entity entity) {
        return creator.equals(entity.getUniqueId());
    }
    
    public boolean isMember(UUID id) {
        return _members.contains(id);
    }
    
    public boolean isMember(OfflinePlayer entity) {
        return _members.contains(entity.getUniqueId());
    }
    
    public boolean isMember(Entity entity) {
        return _members.contains(entity.getUniqueId());
    }
    
}
