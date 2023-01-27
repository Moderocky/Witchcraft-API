package mx.kenzie.witchcraft.data;

import mx.kenzie.witchcraft.ResourceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

public enum MagicClass {
    PURE {},
    HEDGE_WITCH {
        @Override
        public TextColor colour() {
            return TextColor.color(95, 207, 99);
        }
        
        @Override
        public long discordId() {
            return 1062365627404259368L;
        }
    },
    NECROMANCER {
        @Override
        public TextColor colour() {
            return TextColor.color(151, 86, 191);
        }
        
        @Override
        public long discordId() {
            return 1062691446353829978L;
        }
    },
    GLADIOMAGUS {
        @Override
        public TextColor colour() {
            return TextColor.color(219, 93, 70);
        }
        
        @Override
        public long discordId() {
            return 1062691533276585994L;
        }
    },
    THAUMATURGE {
        @Override
        public TextColor colour() {
            return TextColor.color(70, 137, 232);
        }
        
        @Override
        public long discordId() {
            return 1062691359154262056L;
        }
    },
    WARLOCK {
        @Override
        public TextColor colour() {
            return TextColor.color(232, 194, 56);
        }
        
        @Override
        public long discordId() {
            return 1062691586858811473L;
        }
    },
    DIVINE {
        @Override
        public TextColor colour() {
            return TextColor.color(247, 49, 148);
        }
        
        @Override
        public long discordId() {
            return 1062326851151855627L;
        }
    };
    
    public static MagicClass of(String text) {
        try {
            return MagicClass.valueOf(text.toUpperCase().replace(' ', '_'));
        } catch (Throwable ex) {
            return MagicClass.PURE;
        }
    }
    
    public Component displayName() {
        return Component.text(ResourceManager.pascalCase(this.name().replace('_', ' ')))
            .color(this.colour());
    }
    
    public TextColor colour() {
        return NamedTextColor.WHITE;
    }
    
    public long discordId() {
        return 1062802667816099961L;
    }
    
    public void apply(Player player) {
        final PlayerData data = PlayerData.getData(player);
        final MagicClass old = data.style;
        if (old == this) return;
        data.setClass(this);
        data.removeSpellsFrom(old);
    }
    
}
