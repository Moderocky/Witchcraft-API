package mx.kenzie.witchcraft.data.item;

import net.kyori.adventure.text.format.TextColor;

public enum Rarity {
    
    COMMON {
        final TextColor color = TextColor.color(105, 175, 90);
        
        @Override
        public String qualifiedName() {
            return "Common";
        }
        
        @Override
        public int weight() {
            return 10;
        }
        
        @Override
        public TextColor color() {
            return color;
        }
        
        @Override
        public String[] description() {
            return new String[] {"Found in a wide variety of places.", "Easy to obtain."};
        }
    },
    UNCOMMON {
        final TextColor color = TextColor.color(86, 252, 161);
        
        @Override
        public String qualifiedName() {
            return "Uncommon";
        }
        
        @Override
        public int weight() {
            return 20;
        }
        
        @Override
        public TextColor color() {
            return color;
        }
        
        @Override
        public String[] description() {
            return new String[] {"Found in a variety of places.", "Fairly easy to obtain."};
        }
    },
    RARE {
        final TextColor color = TextColor.color(65, 177, 236);
        
        @Override
        public String qualifiedName() {
            return "Rare";
        }
        
        @Override
        public int weight() {
            return 30;
        }
        
        @Override
        public TextColor color() {
            return color;
        }
        
        @Override
        public String[] description() {
            return new String[] {"Found in a few places.", "Can be difficult to obtain."};
        }
    },
    VERY_RARE {
        final TextColor color = TextColor.color(89, 109, 250);
        
        @Override
        public String qualifiedName() {
            return "Very Rare";
        }
        
        @Override
        public int weight() {
            return 40;
        }
        
        @Override
        public TextColor color() {
            return color;
        }
        
        @Override
        public String[] description() {
            return new String[] {"Found in a select few places.", "Likely to be difficult to obtain."};
        }
    },
    EPIC {
        final TextColor color = TextColor.color(138, 80, 250);
        
        @Override
        public String qualifiedName() {
            return "Epic";
        }
        
        @Override
        public int weight() {
            return 50;
        }
        
        @Override
        public TextColor color() {
            return color;
        }
        
        @Override
        public String[] description() {
            return new String[] {"Occasionally found in a few places.", "Hard to obtain."};
        }
    },
    LEGENDARY {
        final TextColor color = TextColor.color(248, 185, 27);
        
        @Override
        public String qualifiedName() {
            return "Legendary";
        }
        
        @Override
        public int weight() {
            return 60;
        }
        
        @Override
        public TextColor color() {
            return color;
        }
        
        @Override
        public String[] description() {
            return new String[] {"Rarely found in certain areas.", "Very hard to obtain."};
        }
    },
    MYTHICAL {
        final TextColor color = TextColor.color(250, 17, 145);
        
        @Override
        public String qualifiedName() {
            return "Mythical";
        }
        
        @Override
        public int weight() {
            return 70;
        }
        
        @Override
        public TextColor color() {
            return color;
        }
        
        @Override
        public String[] description() {
            return new String[] {"Very rarely found in specific locations.", "Extremely hard to obtain."};
        }
    },
    FINITE {
        final TextColor color = TextColor.color(245, 15, 57);
        
        @Override
        public String qualifiedName() {
            return "Finite";
        }
        
        @Override
        public int weight() {
            return 100;
        }
        
        @Override
        public TextColor color() {
            return color;
        }
        
        @Override
        public String[] description() {
            return new String[] {"A limited number exists in the universe.", "Possibly unobtainable by natural means."};
        }
    };
    
    public int weight() {
        return 10;
    }
    
    public String qualifiedName() {
        return "Unknown";
    }
    
    public TextColor color() {
        return TextColor.color(255, 255, 255);
    }
    
    public String id() {
        return this.toString();
    }
    
    public String[] description() {
        return new String[0];
    }
    
}
