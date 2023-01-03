package mx.kenzie.witchcraft.data;

import mx.kenzie.witchcraft.ResourceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum MagicClass {
    PURE {},
    HEDGE_WITCH {
        @Override
        public TextColor colour() {
            return TextColor.color(95, 207, 99);
        }
    },
    NECROMANCER {
        @Override
        public TextColor colour() {
            return TextColor.color(151, 86, 191);
        }
    },
    GLADIOMAGUS {
        @Override
        public TextColor colour() {
            return TextColor.color(219, 93, 70);
        }
    },
    THAUMATURGE {
        @Override
        public TextColor colour() {
            return TextColor.color(70, 137, 232);
        }
    },
    WARLOCK {
        @Override
        public TextColor colour() {
            return TextColor.color(232, 194, 56);
        }
    },
    DIVINE {
        @Override
        public TextColor colour() {
            return TextColor.color(247, 49, 148);
        }
    };
    
    public Component displayName() {
        return Component.text(ResourceManager.pascalCase(this.name().replace('_', ' ')))
            .color(this.colour());
    }
    
    public TextColor colour() {
        return NamedTextColor.WHITE;
    }
    
}
