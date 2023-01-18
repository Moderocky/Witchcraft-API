package mx.kenzie.witchcraft.data.item;

import com.destroystokyo.paper.Namespaced;
import mx.kenzie.fern.Fern;
import mx.kenzie.fern.meta.Name;
import mx.kenzie.fern.meta.Optional;
import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.SpellManager;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.LearnedSpell;
import mx.kenzie.witchcraft.data.outfit.OutfitData;
import mx.kenzie.witchcraft.spell.Spell;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.InputStream;
import java.util.*;

public class Item implements ItemArchetype {
    
    private transient final Set<Tag> _tags = new HashSet<>();
    private final transient Collection<Namespaced> canBreak = Collections.emptySet();
    public transient String id;
    public String name, description, material;
    public Rarity rarity = Rarity.COMMON;
    public MagicData magic;
    public boolean fragile, soulbound, galvanised, helmet, placeable;
    public @Name("protected") boolean restricted;
    public int data;
    public String[] tags = new String[0];
    public @Optional OutfitData outfit;
    private transient ItemStack stack;
    private transient Collection<Namespaced> placedOn = Collections.emptySet();
    
    public Item(InputStream stream) {
        final Fern fern = new Fern(stream);
        fern.toObject(this);
        if (magic != null && magic.spells == null) throw new IllegalArgumentException(this.id);
        if (magic != null) {
            this.magic.spellKeys = new NamespacedKey[magic.spells.length];
            for (int i = 0; i < magic.spells.length; i++) {
                this.magic.spellKeys[i] = NamespacedKey.fromString(magic.spells[i]);
            }
        }
        if (placeable) placedOn = PlaceableMaterial.FULL_BLOCKS;
        for (String tag : tags) _tags.add(Tag.register(tag));
    }
    
    public Item() {
    }
    
    @Override
    public boolean isOutfit() {
        return outfit != null && outfit.isValid();
    }
    
    @Override
    public OutfitData asOutfit() {
        return outfit;
    }
    
    @Override
    public boolean isProtected() {
        return restricted;
    }
    
    @Override
    public Set<Tag> tags() {
        return _tags;
    }
    
    @Override
    public Component itemName() {
        return Component.translatable("item." + this.id).color(this.rarity().color())
            .decoration(TextDecoration.ITALIC, false);
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
    
    @Override
    public List<Component> itemLore() {
        return this.itemLore(null);
    }
    
    @Override
    public String description() {
        return description;
    }
    
    @Override
    public void update(ItemStack item) {
        if (item == null) return;
        final ItemStack template = this.create();
        if (template == null) return;
        final ItemMeta meta = item.getItemMeta();
        if (item.getType() != template.getType()) item.setType(template.getType());
        final PersistentDataContainer container = meta.getPersistentDataContainer();
        final PersistentDataContainer archetype = template.getItemMeta().getPersistentDataContainer();
        Minecraft.getInstance().merge(archetype, container);
        meta.lore(this.itemLore(meta));
        meta.displayName(this.itemName());
    }
    
    @Override
    public ItemStack create() {
        if (stack != null) return stack.clone();
        this.stack = Bukkit.getItemFactory().createItemStack(material);
        final ItemMeta meta = stack.getItemMeta();
        final PersistentDataContainer container = meta.getPersistentDataContainer();
        meta.setDestroyableKeys(canBreak);
        meta.setPlaceableKeys(placedOn);
        container.set(WitchcraftAPI.plugin.getKey("custom_item"), PersistentDataType.BYTE, (byte) 1);
        container.set(WitchcraftAPI.plugin.getKey("custom_id"), PersistentDataType.STRING, id);
        if (restricted) container.set(WitchcraftAPI.plugin.getKey("protected"), PersistentDataType.BYTE, (byte) 1);
        meta.setCustomModelData(data);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE);
        if (placedOn.size() > 6) meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        if (canBreak.size() > 6) meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.setUnbreakable(galvanised);
        meta.displayName(this.itemName());
        meta.lore(this.itemLore());
        if (magic != null) container.set(WitchcraftAPI.plugin.getKey("magic_item"), PersistentDataType.BYTE, (byte) 1);
        this.stack.setItemMeta(meta);
        return stack;
    }
    
    @Override
    public boolean isHelmet() {
        return helmet || this.hasTag(Tag.parse("HELMET"));
    }
    
    @Override
    public boolean isMagic() {
        return (magic != null) && magic.can_cast;
    }
    
    @Override
    public Set<LearnedSpell> getSpells() {
        if (magic == null) return ItemArchetype.super.getSpells();
        final Set<LearnedSpell> set = new HashSet<>();
        for (String id : magic.spells) {
            final Spell spell = WitchcraftAPI.spells.getSpell(id);
            if (spell == null) continue;
            set.add(new LearnedSpell(spell));
        }
        return set;
    }
    
    @Override
    public double bonusAmplitude() {
        if (magic != null) return magic.amplitude;
        return ItemArchetype.super.bonusAmplitude();
    }
    
    @Override
    public int castRange() {
        if (magic != null) return magic.range;
        return ItemArchetype.super.castRange();
    }
    
    @Override
    public int bonusEnergy() {
        if (magic != null) return magic.energy;
        return ItemArchetype.super.bonusEnergy();
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    public List<Component> itemLore(ItemMeta meta) {
        final TextColor pop = WitchcraftAPI.colors().pop();
        final List<Component> list = ItemArchetype.super.itemLore();
        if (fragile) list.add(Component.text("Fragile").decoration(TextDecoration.ITALIC, false).color(rarity.color()));
        if (soulbound)
            list.add(Component.text("Soulbound").decoration(TextDecoration.ITALIC, false).color(rarity.color()));
        if (galvanised)
            list.add(Component.text("Galvanised").decoration(TextDecoration.ITALIC, false).color(rarity.color()));
        if (this.bonusEnergy() > 0)
            list.add(Component.text(" ⚡ " + this.bonusEnergy() + " Energy")
                .color(pop).decoration(TextDecoration.ITALIC, false));
        if (this.bonusAmplitude() > 0)
            list.add(Component.text(" ☀ " + (int) Math.round(magic.amplitude * 10) + " Amplitude")
                .color(pop).decoration(TextDecoration.ITALIC, false));
        this.writeSpellSlots(meta, list);
        return list;
    }
    
    public boolean storeSpell(ItemMeta meta, LearnedSpell spell) {
        final Set<LearnedSpell> known = this.getSpells();
        final List<LearnedSpell> stored = this.storedSpells(meta);
        final List<LearnedSpell> collection = new ArrayList<>(stored);
        final int slots = known.size() + this.getSpellSlots();
        if (slots < 1) return false;
        int count = 0;
        for (LearnedSpell learned : known) {
            if (count == slots) break;
            if (stored.remove(learned) || !learned.equals(spell)) count++;
            else {
                collection.add(spell);
                this.storeSpells(meta, collection);
                return true;
            }
        }
        if (count >= slots) return false;
        collection.add(spell);
        this.storeSpells(meta, collection);
        return true;
    }
    
    protected List<LearnedSpell> storedSpells(ItemMeta meta) {
        if (meta == null) return Collections.emptyList();
        final PersistentDataContainer container = meta.getPersistentDataContainer();
        final long[] codes = container.get(new NamespacedKey("witchcraft", "stored_spells"), PersistentDataType.LONG_ARRAY);
        if (codes == null) return Collections.emptyList();
        final List<LearnedSpell> list = new ArrayList<>(codes.length);
        final SpellManager manager = WitchcraftAPI.spells;
        for (long code : codes) {
            final LearnedSpell spell = manager.getSpell(code);
            if (spell != null) list.add(spell);
        }
        return list;
    }
    
    public int getSpellSlots() {
        if (magic == null) return 0;
        return magic.spell_slots;
    }
    
    protected void storeSpells(ItemMeta meta, List<LearnedSpell> list) {
        final PersistentDataContainer container = meta.getPersistentDataContainer();
        final SpellManager manager = WitchcraftAPI.spells;
        final long[] codes = new long[list.size()];
        int index = 0;
        for (LearnedSpell spell : list) {
            codes[index] = manager.getCode(spell);
            index++;
        }
        container.set(new NamespacedKey("witchcraft", "stored_spells"), PersistentDataType.LONG_ARRAY, codes);
    }
    
    protected void writeSpellSlots(ItemMeta meta, List<Component> list) {
        final Set<LearnedSpell> known = this.getSpells();
        final List<LearnedSpell> stored = this.storedSpells(meta);
        final ItemSpell[] spells = new ItemSpell[known.size() + this.getSpellSlots()];
        if (spells.length < 1) return;
        int index = 0;
        for (LearnedSpell spell : known) {
            if (index == spells.length) break;
            spells[index] = new ItemSpell(spell, stored.remove(spell));
            index++;
        }
        for (LearnedSpell spell : stored) {
            if (index == spells.length) break;
            spells[index] = new ItemSpell(spell, true);
            index++;
        }
        final TextColor pop = WitchcraftAPI.colors().pop();
        for (ItemSpell spell : spells) {
            final TextColor color = spell != null && spell.stored ? pop : TextColor.color(139, 166, 199);
            if (spell == null)
                list.add(Component.text(" ☄ Spell Slot").decoration(TextDecoration.ITALIC, false).color(color));
            else list.add(Component.textOfChildren(
                Component.text(" ☽ ").decoration(TextDecoration.ITALIC, false)
                    .color(color),
                spell.spell.itemName().color(color)
            ));
        }
    }
    
    public LearnedSpell castStoredSpell(ItemMeta meta) {
        final List<LearnedSpell> spells = this.storedSpells(meta);
        if (spells == null || spells.isEmpty()) return null;
        final LearnedSpell spell = spells.remove(0);
        this.storeSpells(meta, spells);
        return spell;
    }
    
    private record ItemSpell(LearnedSpell spell, boolean stored) {
    }
    
    public static class MagicData {
        public boolean can_cast;
        public int range = 5, energy;
        public double amplitude;
        public int spell_slots;
        public String[] spells = new String[0];
        public transient NamespacedKey[] spellKeys;
    }
}
