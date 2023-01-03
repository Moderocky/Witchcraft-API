package mx.kenzie.witchcraft.data;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.item.Rarity;
import mx.kenzie.witchcraft.data.item.Tag;
import mx.kenzie.witchcraft.spell.Spell;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class LearnedSpell implements ItemArchetype {
    public int level = 1;
    public String id;
    private transient Spell spell;
    
    public LearnedSpell() {
    }
    
    public LearnedSpell(Spell spell) {
        this.id = spell.getId();
        this.spell = spell;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LearnedSpell spell)) return false;
        return Objects.equals(id, spell.id);
    }
    
    @Override
    public boolean isProtected() {
        return true;
    }
    
    @Override
    public Set<Tag> tags() {
        return new HashSet<>();
    }
    
    @Override
    public String name() {
        return spell.getName();
    }
    
    @Override
    public Rarity rarity() {
        return Rarity.values()[Math.min(Math.max(0, spell.getPoints() - 4), Rarity.values().length)];
    }
    
    @Override
    public String id() {
        return id;
    }
    
    @Override
    public String description() {
        return spell.getDescription();
    }
    
    @Override
    public ItemStack create() {
        final ItemStack item = (switch (this.getStyle()) {
            case PURE -> ItemArchetype.of("pure_spell_book");
            case HEDGE_WITCH -> ItemArchetype.of("wild_spell_book");
            case NECROMANCER -> ItemArchetype.of("necromancy_spell_book");
            case GLADIOMAGUS -> ItemArchetype.of("battle_spell_book");
            case THAUMATURGE -> ItemArchetype.of("thaumaturgy_spell_book");
            case WARLOCK -> ItemArchetype.of("warlock_spell_book");
            case DIVINE -> ItemArchetype.of("divine_spell_book");
        }).create();
        item.addItemFlags(ItemFlag.values());
        item.setAmount(Math.max(1, Math.min(127, level)));
        final ItemMeta meta = item.getItemMeta();
        final TextColor color = spell.getType().color;
        if (color != null && meta instanceof PotionMeta potion) potion.setColor(Color.fromRGB(color.value()));
        meta.displayName(this.itemName());
        final List<Component> lore = new ArrayList<>();
        lore.add(spell.getStyle().displayName().decoration(TextDecoration.ITALIC, false));
        lore.add(spell.getType().displayName().decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        lore.addAll(this.itemLore());
        lore.add(Component.empty());
        lore.add(Component.text("  " + spell.getPatternPicture()).color(NamedTextColor.GOLD)
            .decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        lore.add(Component.empty());
        lore.add(Component.empty());
        meta.lore(lore);
        meta.getPersistentDataContainer().set(WitchcraftAPI.plugin.getKey("spell_id"), PersistentDataType.STRING, id);
        item.setItemMeta(meta);
        return item;
    }
    
    public MagicClass getStyle() {
        return this.getSpell().getStyle();
    }
    
    public Spell getSpell() {
        if (spell != null) return spell;
        return spell = WitchcraftAPI.spells.getSpell(id);
    }
    
    @Override
    public Set<LearnedSpell> getSpells() {
        return new HashSet<>(List.of(this));
    }
}
