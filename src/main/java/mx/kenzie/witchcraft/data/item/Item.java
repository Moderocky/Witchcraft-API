package mx.kenzie.witchcraft.data.item;

import com.destroystokyo.paper.Namespaced;
import mx.kenzie.fern.Fern;
import mx.kenzie.fern.meta.Name;
import mx.kenzie.fern.meta.Optional;
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
        final List<Component> list = ItemArchetype.super.itemLore();
        if (fragile) list.add(Component.text("Fragile").decoration(TextDecoration.ITALIC, false).color(rarity.color()));
        if (soulbound)
            list.add(Component.text("Soulbound").decoration(TextDecoration.ITALIC, false).color(rarity.color()));
        if (galvanised)
            list.add(Component.text("Galvanised").decoration(TextDecoration.ITALIC, false).color(rarity.color()));
        if (this.bonusEnergy() > 0)
            list.add(Component.text(" ⚡ " + this.bonusEnergy() + " Energy")
                .color(TextColor.color(255, 225, 28)).decoration(TextDecoration.ITALIC, false));
        if (magic != null) {
            if (magic.amplitude > 0)
                list.add(Component.text(" ☀ " + (int) Math.round(magic.amplitude * 10) + " Amplitude")
                    .color(TextColor.color(255, 225, 28)).decoration(TextDecoration.ITALIC, false));
            if (magic.spells.length > 0) for (LearnedSpell spell : this.getSpells())
                list.add(Component.textOfChildren(//☽✶
                    Component.text(" ☽ ").decoration(TextDecoration.ITALIC, false)
                        .color(TextColor.color(139, 166, 199)),
                    spell.itemName().color(TextColor.color(139, 166, 199))
                ));
        }
        return list;
    }
    
    @Override
    public String description() {
        return description;
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
    public boolean isEmpty() {
        return false;
    }
    
    public static class MagicData {
        public boolean can_cast;
        public int range = 5;
        public double amplitude;
        public String[] spells = new String[0];
        public transient NamespacedKey[] spellKeys;
    }
}
