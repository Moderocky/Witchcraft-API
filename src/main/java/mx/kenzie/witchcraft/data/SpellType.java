package mx.kenzie.witchcraft.data;

import mx.kenzie.witchcraft.ResourceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public enum SpellType {
    CONJURY(TextColor.color(95, 207, 99)),
    SUMMONING(TextColor.color(162, 105, 197)),
    TRANSMUTATION(TextColor.color(232, 194, 56)),
    SPATIAL_MANIPULATION(TextColor.color(70, 137, 232)),
    ENCHANTMENT(TextColor.color(219, 122, 70)),
    EDICT(TextColor.color(234, 65, 65)),
    WARD(TextColor.color(73, 143, 138));
    public final TextColor color;
    
    SpellType(TextColor color) {
        this.color = color;
    }
    
    public static SpellType get(String name) {
        return valueOf(name.trim().replace(' ', '_').toUpperCase());
    }
    
    public Component displayName() {
        return Component.text(ResourceManager.pascalCase(this.name().replace('_', ' ')))
            .color(this.colour());
    }
    
    public TextColor colour() {
        return color;
    }
}
