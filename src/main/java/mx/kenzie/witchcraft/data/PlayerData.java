package mx.kenzie.witchcraft.data;

import mx.kenzie.fern.Fern;
import mx.kenzie.witchcraft.Session;
import mx.kenzie.witchcraft.data.achievement.Achievement;
import mx.kenzie.witchcraft.spell.Spell;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class PlayerData extends CasterData {
    public transient final Temporary temporary = new Temporary();
    public final Memory memory = new Memory();
    public MagicClass style = MagicClass.PURE;
    public UUID coven;
    public String nickname, name;
    public long joined, seen;
    public Title[] titles = new Title[] {Title.NOVICE};
    public Achievement[] achievements = new Achievement[0];
    public Title current_title = Title.NOVICE;
    public int generation = 1;
    private transient Player player;
    
    PlayerData() {
    }
    
    public static PlayerData getData(Player player) {
        final PlayerData data = getData(player.getUniqueId());
        data.player = player;
        data.name = player.getName();
        return data;
    }
    
    public static PlayerData getData(UUID uuid) {
        if (CasterData.DATA.get(uuid) instanceof PlayerData player) return player;
        final PlayerData data = new PlayerData();
        data.uuid = uuid;
        data.file = new File("data/player/" + uuid + ".fern");
        data.load();
        CasterData.DATA.put(uuid, data);
        return data;
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
        final List<Position> positions = new ArrayList<>(memory.locations.length + online + 3);
        positions.addAll(List.of(memory.locations));
        if (coven != null) positions.addAll(coven.getPositions());
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
    
    public class Temporary {
        public Session session;
    }
    
    public class Memory {
        public MagicClass style;
        public LearnedSpell[] spells = new LearnedSpell[0];
        public Position.Static[] locations = new Position.Static[0];
    }
}
