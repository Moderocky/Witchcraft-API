package mx.kenzie.witchcraft.data;

import mx.kenzie.fern.Fern;
import mx.kenzie.fern.meta.Optional;
import mx.kenzie.witchcraft.Session;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.achievement.Achievement;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.modifier.Modifier;
import mx.kenzie.witchcraft.data.modifier.ModifierMap;
import mx.kenzie.witchcraft.data.outfit.Clothing;
import mx.kenzie.witchcraft.data.world.WorldData;
import mx.kenzie.witchcraft.spell.Spell;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class PlayerData extends CasterData<PlayerData> {
    public static final Map<Player, PlayerData> LOCAL_CACHE = new WeakHashMap<>();
    public transient final Temporary temporary = new Temporary();
    public Memory memory = new Memory();
    public @Optional String[] clothes = new String[0];
    public MagicClass style = MagicClass.PURE;
    public UUID coven;
    public String nickname, name;
    public long joined, seen, discord_id;
    public Memory[] history = new Memory[0];
    public Title[] titles = new Title[] {Title.NOVICE};
    public Achievement[] achievements = new Achievement[0];
    public Title current_title = Title.NOVICE;
    public int generation = 1;
    private transient Player player;
    private transient List<ItemArchetype> outfit;
    
    PlayerData() {
    }
    
    public static PlayerData getData(Player player) {
        if (LOCAL_CACHE.containsKey(player)) return LOCAL_CACHE.get(player);
        final PlayerData data = getData(player.getUniqueId());
        data.player = player;
        data.name = player.getName();
        LOCAL_CACHE.put(player, data);
        return data;
    }
    
    public static PlayerData getData(UUID uuid) {
        if (CasterData.DATA.get(uuid) instanceof PlayerData player) return player;
        final PlayerData data = new PlayerData();
        data.uuid = uuid;
        data.file = new File("data/player/" + uuid + "/data.fern");
        data.load();
        CasterData.DATA.put(uuid, data);
        return data;
    }
    
    public void regenerate() {
        this.generation++;
        this.style = MagicClass.PURE;
        this.current_title = Title.NOVICE;
        final Coven coven = Coven.getCoven(this.coven);
        if (coven != null) coven.removeMember(uuid);
        this.coven = null;
        final Memory[] memories = new Memory[history.length + 1];
        memories[memories.length - 1] = memory;
        this.memory = new Memory();
        this.history = memories;
        this.scheduleSave();
    }
    
    public long getDiscordId() {
        return discord_id;
    }
    
    public void setDiscordId(long discord) {
        this.discord_id = discord;
        this.scheduleSave();
    }
    
    public boolean isDiscordLinked() {
        return discord_id > 0;
    }
    
    public boolean hasModifier(Modifier.Type type) {
        return temporary.modifiers.isPresent(type);
    }
    
    public double getModifier(Modifier.Type type) {
        return temporary.modifiers.get(type);
    }
    
    public boolean hasAchievement(Achievement achievement) {
        return this.getAchievements().contains(achievement);
    }
    
    public Set<Achievement> getAchievements() {
        return new HashSet<>(List.of(achievements));
    }
    
    public void addAchievement(Achievement... achievements) {
        final Set<Achievement> set = new HashSet<>(List.of(achievements));
        set.addAll(List.of(achievements));
        this.achievements = set.toArray(new Achievement[0]);
        this.scheduleSave();
    }
    
    public boolean learnSpell(Spell spell) { // true if new, false if levelled
        final Set<LearnedSpell> set = this.getSpells();
        for (LearnedSpell learned : set) {
            if (learned.getSpell() != spell) continue;
            learned.level++;
            return false;
        }
        set.add(new LearnedSpell(spell));
        this.memory.spells = set.toArray(new LearnedSpell[0]);
        this.scheduleSave();
        return true;
    }
    
    public Set<LearnedSpell> getSpells() {
        return new HashSet<>(List.of(memory.spells));
    }
    
    public boolean forgetSpell(Spell spell) { // true if yes, false if none
        final Set<LearnedSpell> set = this.getSpells();
        final boolean result = set.removeIf(learned -> learned.getSpell() == spell);
        this.memory.spells = set.toArray(new LearnedSpell[0]);
        this.scheduleSave();
        return result;
    }
    
    public boolean knows(Spell spell) {
        for (LearnedSpell learned : memory.spells) if (learned.getSpell() == spell) return true;
        return false;
    }
    
    public void giveTitle(Title... titles) {
        final Set<Title> set = new LinkedHashSet<>(List.of(this.titles));
        set.addAll(List.of(titles));
        this.titles = set.toArray(new Title[0]);
        this.scheduleSave();
    }
    
    public String getTitle() {
        if (current_title == null) return null;
        return current_title.toString();
    }
    
    public boolean hasTitle(Title title) {
        for (Title test : titles) if (test == title) return true;
        return false;
    }
    
    private Player findPlayer() {
        if (player != null) return player;
        return this.player = Bukkit.getPlayer(uuid);
    }
    
    public MagicClass setClass(MagicClass style) {
        final MagicClass old = this.style;
        this.style = style;
        if (player != null) player.displayName(this.displayName());
        this.scheduleSave();
        WitchcraftAPI.plugin.syncToDiscord(this);
        return old;
    }
    
    public Component displayName() {
        return Component.textOfChildren(
                Component.text('('),
                this.displayTitle().color(style.colour()),
                Component.text(')'), Component.space(), this.nickname())
            .hoverEvent(Component.textOfChildren(
                style.displayName(), Component.newline(),
                this.covenDisplayName()
            ));
    }
    
    public Component displayTitle() {
        if (current_title == null) return style.displayName();
        return current_title.displayName();
    }
    
    public Component nickname() {
        return (nickname != null && !nickname.isBlank())
            ? Component.text(nickname)
            : (player != null)
            ? player.name()
            : Component.text(this.name);
    }
    
    public Component covenDisplayName() {
        final Coven coven = this.getCoven();
        if (coven == null) return Component.text("No Coven");
        else return coven.displayName();
    }
    
    public Coven getCoven() {
        return Coven.getCoven(coven);
    }
    
    public void update() {
        if (player != null) player.displayName(this.displayName());
    }
    
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }
    
    public boolean hasCoven() {
        return coven != null;
    }
    
    public boolean hasStyle() {
        return style != null && style != MagicClass.PURE;
    }
    
    public List<Position> getKnownLocations() {
        final Coven coven = this.getCoven();
        final int online = coven == null ? 0 : coven.size();
        final List<Position> positions = new ArrayList<>(memory.locations.length + online + 5);
        positions.addAll(List.of(memory.locations));
        if (coven != null) positions.addAll(coven.getPositions());
        if (coven != null && coven.isHomeValid(false)) positions.add(coven.getHome());
        return positions;
    }
    
    public void learnLocation(Location location) {
        final List<Position.Static> list = new ArrayList<>(List.of(memory.locations));
        list.add(new Position.Static(location));
        this.memory.locations = list.toArray(new Position.Static[0]);
        this.scheduleSave();
    }
    
    @Override
    public String toString() {
        return Fern.out(this, "\t");
    }
    
    @Override
    public void save() {
        this.memory.style = style;
        super.save();
    }
    
    public void banish(World world) {
        final WorldData data = WorldData.getData(world);
        if (data.main_world) return;
        if (data.restricted) return;
        if (data.safe) return;
        final Set<UUID> set = new HashSet<>(List.of(memory.banished_planes));
        set.add(world.getUID());
        this.memory.banished_planes = set.toArray(new UUID[0]);
        this.scheduleSave();
    }
    
    public boolean isBanished(World world) {
        final WorldData data = WorldData.getData(world);
        if (data.main_world) return false;
        if (data.restricted) return true;
        if (data.safe) return false;
        final Set<UUID> set = new HashSet<>(List.of(memory.banished_planes));
        return set.contains(world.getUID());
    }
    
    public void wear(ItemArchetype archetype) {
        if (!archetype.isOutfit()) return;
        final Clothing slot = archetype.asOutfit().slot;
        this.getClothes(); // updates transient outfit
        this.outfit.removeIf(item -> !item.isOutfit() || item.asOutfit().slot == slot);
        this.saveOutfit();
    }
    
    public ItemArchetype[] getClothes() {
        if (outfit != null) return outfit.toArray(new ItemArchetype[0]);
        final String[] clothes = this.clothes == null ? new String[0] : this.clothes;
        this.outfit = new ArrayList<>(6);
        for (String clothe : clothes) outfit.add(ItemArchetype.of(clothe));
        return outfit.toArray(new ItemArchetype[0]);
    }
    
    private void saveOutfit() {
        final ItemArchetype[] items = outfit.toArray(new ItemArchetype[0]);
        final String[] clothes = new String[items.length];
        for (int i = 0; i < items.length; i++) clothes[i] = items[i].id();
        this.clothes = clothes;
        this.scheduleSave();
    }
    
    public boolean canWear(ItemArchetype archetype) {
        return archetype.isOutfit();
    }
    
    public class Temporary {
        public final ModifierMap modifiers = new ModifierMap();
        public Session session;
    }
    
    public class Memory {
        public MagicClass style;
        public WarlockDeity deity;
        public LearnedSpell[] spells = new LearnedSpell[0];
        public Position.Static[] locations = new Position.Static[0];
        public UUID[] banished_planes = new UUID[0];
    }
}
