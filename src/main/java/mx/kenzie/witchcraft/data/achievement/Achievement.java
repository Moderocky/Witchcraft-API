package mx.kenzie.witchcraft.data.achievement;

import mx.kenzie.advancements.Advancement;
import mx.kenzie.advancements.Display;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.Title;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

public enum Achievement {

    ROOT("Witchcraft", "A record of your magical journey.", "book", "mangrove") {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.NOVICE);
        }
    },
    CAST_SPELL("Hex", "Cast your first spell.", "stick", ROOT),
    CAST_ENCHANT_SPELL("Enchanter", "Cast your first enchantment.", "spell_book", CAST_SPELL) {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.ENCHANTER);
        }
    },
    CAST_BIG_SPELL("Adept Magician", "Cast a spell that requires all your energy.", "energy", CAST_SPELL, "goal") {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.WIZARD);
        }
    },
    CAST_NINE_SPELL("Master Magician", "Cast a grade nine spell.", "ender_star", CAST_BIG_SPELL, "challenge") {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.MASTER);
        }
    },
    CAST_EDICT_SPELL("Lawmaker", "Cast an edict.", "battle_spell_book", CAST_SPELL) {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.CLERIC);
        }
    },
    CAST_PURE_SPELL("No Strings Attached", "Cast a spell from an item.", "nether_star", CAST_SPELL),
    SEVER_MAGIC("Sever Magic", "Give up your natural magic.", "darkfire_eye_icon", CAST_PURE_SPELL, "goal") {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.SORCERER);
        }
    },
    CREATE_POCKET_REALM("Luna's Pocket Galaxy", "Create your own pocket realm.", "pocket_realm", CAST_PURE_SPELL, "goal"),
    CRAFT_ITEM("Invention", "Craft your first item.", "forge", ROOT),
    CRAFT_LEGENDARY_ITEM("Grand Design", "Craft a legendary item.", "forge_strike", CRAFT_ITEM, "goal"),
    CRAFT_FINITE_ITEM("One of a Kind", "Craft an item with a finite ingredient.", "forge_finite", CRAFT_LEGENDARY_ITEM, "challenge"),
    CRAFT_ARCANE_ITEM("Arcane Artificer", "Craft an item with magical properties.", "crystal_ball_icon", CRAFT_ITEM) {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.ARTIFICER);
        }
    },
    LEARN_SPELL("Apprentice", "Learn your first spell.", "letter", ROOT),
    LEARN_ALL_SPELLS("Scholar", "Learn every spell for your class.", "abacus", LEARN_SPELL, "goal") {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.SCHOLAR);
        }
    },
    MASTER_ALL_SPELLS("Grandmaster Magician", "Master every spell.", "obsidian_tablet", LEARN_ALL_SPELLS, "challenge") {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.GRANDMASTER);
        }
    },
    DEMONOLOGY_ROOT("Demonology", "Start your journey as a warlock.", "warlock_spell_book", "bricks"),
    SUMMON_IMP("Satan's Little Helper", "Summon assistance from the Demon Realm.", "summon_demon", DEMONOLOGY_ROOT),
    CAST_PLANAR_INVERSION("Hell's Coming With Me", "Bring a bit of the Demon Realm to you.", "inversion", DEMONOLOGY_ROOT),
    ENTER_HELL("Damnation", "Travel to the Demon Realm.", "nether_portal", CAST_PLANAR_INVERSION),
    KILL_BIG_DEMON("Power Struggle", "Kill an inquisitor demon.", "kill_demon", ENTER_HELL),
    HOME_HELL("Better to Rule in Hell", "Make your coven home in the Demon Realm.", "hell_home", ENTER_HELL, "goal"),
    CAST_INCURSION("Invasion", "Invade a realm with a demon army.", "incursion", SUMMON_IMP, "goal"),
    CAST_REDEDICATION("Crisis of Faith", "Rededicate yourself to a new Dark Lord.", "rededication", SUMMON_IMP, "challenge"),
    CAST_APOTHEOSIS("Call Me God", "Become a minor deity.", "glowing_player", DEMONOLOGY_ROOT) {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.DEITY);
        }
    },
    CAST_GODSEND("Kindly Call Me God", "Kill something with a divine weapon.", "divine_weapon", CAST_APOTHEOSIS, "goal"),
    KILL_DEITY("God Calls Me God", "Kill a deity by summoning the Brightfire.", "summon_brightfire", CAST_GODSEND, "challenge"),
    BATTLE_ROOT("Battle Magic", "Start your journey as a gladiomagus.", "battle_spell_book", "stone"),
    CAST_HAMMER("My Little Friend", "Summon your Warhammer.", "hammer_drop", BATTLE_ROOT),
    CAST_DEFIANCE("Stand Behind Me", "Shield damage with your Warhammer.", "hammer_shield", CAST_HAMMER),
    CAST_HAMMERFALL("Catch This", "Drop your Warhammer into another realm.", "hammer_portal", CAST_DEFIANCE, "challenge"),
    CAST_SPEAR("Hammers are Old News", "Throw a spear of destiny.", "spear", CAST_HAMMER),
    KILL_MONSTER("Fighter", "Kill a monster with magic.", "wooden_sword", BATTLE_ROOT),
    KILL_PLAYER("Challenger", "Kill a player with magic.", "iron_sword", KILL_MONSTER) {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.BATTLEMAGE);
        }
    },
    KILL_ARCANA("Champion", "Kill a boss with magic.", "bad_omen", KILL_PLAYER, "goal"),
    CAST_EXECUTE("Balanced Damage", "Execute an enemy from full health.", "strength", KILL_PLAYER, "challenge"),
    CAST_STEADFAST("You Can't Knock Me Down", "Become steadfast.", "resistance_shield", BATTLE_ROOT),
    CAST_IMMORTAL_WILL("That Sign Can't Stop Me", "Bypass enemy wards using Immortal Will.", "fire_resistance", CAST_STEADFAST),
    CAST_INVULNERABILITY("Can't Touch This", "Become immune to damage.", "absorption", CAST_IMMORTAL_WILL, "goal"),
    CAST_STAND_UNITED("Nobody Left Behind", "Help an ally in need.", "help_friend", CAST_DEFIANCE),
    CAST_GRAND_STARFALL("Dramatic Entrance", "Ride into battle on a meteorite.", "meteorite", CAST_STAND_UNITED, "goal"),
    THAUMATURGY_ROOT("Thaumaturgy", "Start your journey as a thaumaturge.", "thaumaturgy_spell_book", "sculk"),
    TELEPORT("Fast Travel", "Cast a spatial manipulation.", "speed", THAUMATURGY_ROOT),
    CREATE_PORTAL("Quantum Gate", "Create a portal.", "portal", TELEPORT),
    VISIT_REALM("The Other Side", "Travel to another dimension.", "night_vision", CREATE_PORTAL),
    CREATE_REALM("The Genesis Ritual", "Create your own world.", "genesis", VISIT_REALM, "challenge"),
    EXPEL("My House", "Expel somebody from your domain.", "expelled", THAUMATURGY_ROOT),
    BANISH("My Rules", "Banish somebody from your domain.", "banished", EXPEL, "challenge"),
    VISIT_OTHER_REALM("Uninvited Guest", "Enter another player's pocket realm.", "door", THAUMATURGY_ROOT),
    DISPEL_WARDS("I Will Not Be Denied", "Destroy the wards on a player's domain.", "broken_shield", VISIT_OTHER_REALM, "goal"),
    DESTROY_REALM("Mage Noir", "Destroy a world using cataclysm.", "cataclysm", DISPEL_WARDS, "challenge"),
    CAST_WONDERWORK("Miracle", "Conjure an item.", "light_bulb", THAUMATURGY_ROOT),
    CAST_WISH("Wish", "Make a wish.", "light", CAST_WONDERWORK, "challenge"),
    HEDGE_ROOT("Wild Magic", "Start your journey as a hedge witch.", "wild_spell_book", "leaves"),
    USE_INGREDIENT("Like a Potion!", "Use ingredients when casting a spell.", "potion_bottle", HEDGE_ROOT) {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.WITCH);
        }
    },
    CAST_CHANGE_TERRAIN("Master of My World", "Alter the terrain.", "blocks", USE_INGREDIENT),
    CAST_ENCHANTMENT_SELF("Master of Myself", "Evolve your body.", "brain_item", CAST_CHANGE_TERRAIN),
    CAST_FLIGHT("Skywalker", "Fly without a broomstick.", "jump_boost", CAST_ENCHANTMENT_SELF, "challenge"),
    SUMMON_GUARD("I Have Friends", "Summon an ally.", "dolphins_grace", HEDGE_ROOT),
    CAST_COOPERATION("The Power of Friendship", "Use cooperative magic to empower your coven.", "sparkly_heart", SUMMON_GUARD, "goal"),
    CAST_WARD("Keep Out", "Ward your coven base.", "padlock", HEDGE_ROOT),
    CAST_TELEPORT_WARD("No Entry", "Put an anti-teleportation ward on your coven base.", "glowing_padlock", CAST_WARD),
    CAST_INVULNERABILITY_WARD("Our Castle", "Put a ward of safety on your coven base.", "invulnerable_padlock", CAST_TELEPORT_WARD, "challenge"),
    NECROMANCY_ROOT("Necromancy", "Start your journey as a necromancer.", "necromancy_spell_book", "death"),
    RAISE_DEAD("Raise Dead", "Raise a corpse to fight for you.", "glowing_zombie", NECROMANCY_ROOT),
    SUMMON_ARMY("Army of Darkness", "Raise 5 summons at once.", "glowing", RAISE_DEAD),
    SUMMON_WITHER("Judgement Day", "Summon the great beast.", "glowing_blue_wither", SUMMON_ARMY, "challenge"),
    ENTER_DEATH_REALM("Cross the Veil", "Enter the Death Realm.", "darkness", NECROMANCY_ROOT),
    BECOME_SHADOWLORD("The Shadowlord Ritual", "Take control of death itself.", "shadow_item", ENTER_DEATH_REALM) {
        @Override
        public void complete(Player player) {
            super.complete(player);
            final PlayerData data = PlayerData.getData(player);
            data.giveTitle(Title.SHADOWLORD);
        }
    },
    GET_NECRONOMICON("Book of the Dead", "Find a copy of the Necronomicon.", "necronomicon", BECOME_SHADOWLORD),
    RESURRECT("Resurrection", "Bring a player back from the dead.", "totem", BECOME_SHADOWLORD),
    EXECUTE("The Killing Curse", "Kill a player permanently.", "wither", RESURRECT, "goal"),
    REINCARNATE("Time and Time Again", "Reincarnate as a past life.", "health_boost", RESURRECT, "challenge");


    public final String name, description;
    public final String icon, background, frame;
    public final NamespacedKey parent;

    Achievement(String name, String description, String icon, String background) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.background = background;
        this.parent = null;
        this.frame = "task";
    }

    Achievement(String name, String description, String icon, Achievement parent) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.background = null;
        this.parent = parent.key();
        this.frame = "task";
    }

    Achievement(String name, String description, String icon, Achievement parent, String frame) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.background = null;
        this.parent = parent.key();
        this.frame = frame;
    }

    public NamespacedKey key() {
        if (WitchcraftAPI.plugin == null) return null; // test only
        return WitchcraftAPI.plugin.getKey(this.name().toLowerCase());
    }

    public void register() {
        try {
            final Advancement advancement = this.createAdvancement();
            advancement.register(WitchcraftAPI.plugin);
        } catch (Throwable ex) {
            throw new RuntimeException("Error registering advancement for " + name(), ex);
        }
    }

    public Advancement createAdvancement() {
        final Advancement advancement = new Advancement(this.name().toLowerCase());
        advancement.display = new Display();
        final ItemArchetype archetype = ItemArchetype.of(icon);
        advancement.display.icon.setItem(archetype.create());
        advancement.display.setTitle(this.displayName());
        advancement.display.setDescription(this.description());
        if (frame != null) advancement.display.frame = frame;
        if (WitchcraftAPI.plugin == null) return advancement;
        if (this.parent == null) {
            advancement.display.show_toast = false;
            advancement.display.announce_to_chat = false;
            advancement.display.hidden = false;
            advancement.display.setBackground(background);
        } else advancement.setParent(parent);
        return advancement;
    }

    public Component displayName() {
        return Component.text(name).hoverEvent(this.description());
    }

    public Component description() {
        return Component.text(this.description);
    }

    public void give(Player player) {
        final PlayerData data = PlayerData.getData(player);
        if (data.hasAchievement(this)) return;
        data.addAchievement(this);
        this.complete(player);
    }

    public void complete(Player player) {
        final org.bukkit.advancement.Advancement advancement = Bukkit.getAdvancement(this.key());
        if (advancement == null) return;
        final AdvancementProgress progress = player.getAdvancementProgress(advancement);
        if (progress.isDone()) return;
        for (String criterion : progress.getRemainingCriteria()) progress.awardCriteria(criterion);
    }

}
