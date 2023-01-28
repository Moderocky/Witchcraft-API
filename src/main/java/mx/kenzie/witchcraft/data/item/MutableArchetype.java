package mx.kenzie.witchcraft.data.item;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class MutableArchetype implements ItemArchetype {

    protected final ItemArchetype initial;
    protected final ItemStack stack;
    protected final ItemMeta meta;

    MutableArchetype(ItemArchetype archetype) {
        this.initial = archetype;
        this.stack = archetype.create();
        this.meta = stack.getItemMeta();
    }

    @SuppressWarnings("unchecked")
    public <TypeMeta extends ItemMeta> MutableArchetype meta(Consumer<TypeMeta> consumer) {
        consumer.accept((TypeMeta) meta);
        return this;
    }

    public <TypeMeta extends ItemMeta> MutableArchetype meta(Class<TypeMeta> type, Consumer<TypeMeta> consumer) {
        if (!type.isInstance(meta)) return this;
        final TypeMeta initial = type.cast(meta);
        consumer.accept(initial);
        return this;
    }

    public MutableArchetype setName(Component name) {
        this.meta.displayName(name);
        return this;
    }

    public MutableArchetype setName(String name) {
        this.meta.displayName(Component.text(name));
        return this;
    }

    public MutableArchetype setLore(List<Component> lore) {
        this.meta.lore(lore);
        return this;
    }

    public MutableArchetype setLore(Component... lore) {
        this.meta.lore(List.of(lore));
        return this;
    }

    @Override
    public boolean isProtected() {
        return true;
    }

    @Override
    public Set<Tag> tags() {
        return initial.tags();
    }

    @Override
    public String name() {
        return initial.name();
    }

    @Override
    public Rarity rarity() {
        return initial.rarity();
    }

    @Override
    public String id() {
        return "mutated_" + initial.id();
    }

    @Override
    public String description() {
        return initial.description();
    }

    @Override
    public ItemStack create() {
        final ItemStack item = stack.clone();
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public MutableArchetype mutate() {
        return this;
    }
}
