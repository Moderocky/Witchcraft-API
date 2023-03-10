package mx.kenzie.witchcraft;

import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.data.LearnedSpell;
import mx.kenzie.witchcraft.data.MagicClass;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.recipe.StorageGUI;
import mx.kenzie.witchcraft.spell.*;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface SpellManager {
    int MIN = -2;
    int MAX = 2;
    int SCALE = MAX - MIN;
    // Maximum average deviation for a cast to be accepted as a given pattern as a factor of SCALE
    double MAXIMUM_DEVIATION = 0.15;

    static SpellManager getInstance() {
        return WitchcraftAPI.spells;
    }

    static void makeMenu(PaginatedGUI gui) {
        gui.setLayout(new String[]{
            "#########",
            "#########",
            "#########",
            "#########",
            "XXXXXXXXX",
            "A_______B",
        });
        gui.createButton('A', WitchcraftAPI.resources.back(), (clicker, event) -> {
            gui.prev();
            clicker.updateInventory();
        });
        gui.createButton('B', WitchcraftAPI.resources.next(), (clicker, event) -> {
            gui.next();
            clicker.updateInventory();
        });
    }

    Pattern generate(int points, String id);

    Pattern generate(int points, Random random);

    @NotNull SpellSupplier create(String id);

    boolean forceCast(LivingEntity caster, String id, float scale, int range);

    double getAmplitude(LivingEntity caster, EntityEquipment equipment);

    boolean cast(LivingEntity caster, Spell spell, float scale);

    int getRange(LivingEntity caster, EntityEquipment equipment);

    void handle(LivingEntity entity, Spell spell, SpellResult result);

    int getEnergy(LivingEntity caster, EntityEquipment equipment);

    int getBonusEnergy(LivingEntity caster, EntityEquipment equipment);

    Set<Spell> getSpells(MagicClass style);

    Map<String, Spell> getMap();

    default PaginatedGUI spellsList(Player player) {
        return this.spellsList(this.knownSpells(player, player.getEquipment()));
    }

    default PaginatedGUI spellsList(Collection<LearnedSpell> collection) {
        final List<LearnedSpell> spells = new ArrayList<>(collection);
        spells.sort(Comparator.comparing(LearnedSpell::getStyle).thenComparing(spell -> spell.getSpell().getPoints())
            .thenComparing(spell -> spell.getSpell().getEnergy()));
        final List<ItemStack> list = new ArrayList<>(spells.size());
        for (LearnedSpell spell : spells) list.add(spell.create());
        final PaginatedGUI gui = new StorageGUI(WitchcraftAPI.plugin, 54, "Known Spells");
        SpellManager.makeMenu(gui);
        gui.setEntryChar('#');
        gui.setEntries(list);
        gui.setEntryConsumer((clicker, event) -> {
            final ItemStack stack = event.getCurrentItem();
            assert stack != null;
            final String id = stack.getItemMeta().getPersistentDataContainer()
                .get(new NamespacedKey("witchcraft", "spell_id"), PersistentDataType.STRING);
            if (id == null) return;
            final Spell spell = getSpell(id);
            if (spell == null) return;
            if (event.isRightClick() && clicker.hasPermission("witchcraft.admin.spell")) {
                clicker.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                spell.cast(clicker, 25, 1.0F, 1.0 + (stack.getAmount() / 10.0));
            } else {
                clicker.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                final Location location = clicker.getEyeLocation();
                location.add(location.getDirection().multiply(1.2));
                WitchcraftAPI.spells.drawPattern(spell, location, 20, ParticleCreator.of(Particle.ELECTRIC_SPARK.builder()
                    .count(0).receivers(clicker)));
            }
        });
        gui.finalise();
        return gui;
    }

    default Set<LearnedSpell> knownSpells(LivingEntity caster, EntityEquipment equipment) {
        final Set<LearnedSpell> set;
        if (caster instanceof Player player) {
            if (WitchcraftAPI.resources.contains(player.getInventory(), ItemArchetype.of("admin_wand"))) {
                set = new HashSet<>();
                for (Spell spell : this.getSpells()) {
                    if (spell.getClass() == StandardSpell.NO_EFFECT) continue;
                    set.add(new LearnedSpell(spell));
                }
            } else {
                set = new HashSet<>(PlayerData.getData(player).getSpells());
                for (ItemStack stack : player.getInventory()) {
                    final ItemArchetype item = ItemArchetype.of(stack);
                    set.addAll(item.getSpells());
                }
            }
        } else {
            set = new HashSet<>();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                final ItemArchetype item = ItemArchetype.of(equipment.getItem(slot));
                set.addAll(item.getSpells());
            }
        }
        return set;
    }

    Spell getSpell(String id);

    void drawPattern(Spell spell, Location location, int delay, ParticleCreator creator);

    Set<Spell> getSpells();

    default LearnedSpell makeKnown(Spell spell, LivingEntity caster, EntityEquipment equipment) {
        final Set<LearnedSpell> set = this.knownSpells(caster, equipment);
        for (LearnedSpell learned : set) if (learned.getSpell().equals(spell)) return learned;
        return new LearnedSpell(spell, 1);
    }

    LearnedSpell getSpell(long code);

    long getCode(LearnedSpell spell);

    default PaginatedGUI spellsListAdmin() {
        final Set<LearnedSpell> set = new HashSet<>();
        for (Spell spell : this.getSpells()) {
            if (spell.getClass() == StandardSpell.NO_EFFECT) continue;
            set.add(new LearnedSpell(spell));
        }
        return this.spellsList(set);
    }

    LearnResult attemptLearnSpell(Player player, Value value);

    void castStoredSpell(LivingEntity entity, ItemStack item);

    default void cast(Player player, String id, float scale, ItemStack item) {
        if (this.cast(player, id, scale)) item.damage(1, player);
    }

    boolean cast(LivingEntity caster, String id, float scale);

    void regenerateEnergy(Player player, int amount);

    interface Value {
        boolean worth();

        int value();

        float amount();
    }

    record LearnResult(Spell spell, boolean success, boolean learned, boolean levelled) {
    }
}
