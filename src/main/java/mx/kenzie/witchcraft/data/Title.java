package mx.kenzie.witchcraft.data;

import mx.kenzie.witchcraft.ResourceManager;
import net.kyori.adventure.text.Component;

public enum Title {
    NOVICE("We all start somewhere.", "Start your magical journey."),
    WITCH("A spell is only as good as its ingredients.", "Cast a spell that costs ingredients."),
    ENCHANTER("Magic is what we leave behind.", "Enchant something."),
    WIZARD("Power is drawn from the world, not the self.", "Cast a spell that requires all your energy."),
    MASTER("I could beat you with my eyes shut.", "Cast a grade nine spell."),
    CLERIC("Arcane power has a voice.", "Cast an edict."),
    SCHOLAR("Real magic is in the books we read.", "Learn every spell from your magic class."),
    SORCERER("Power comes to those who take it.", "Cast severance."),
    ARTIFICER("Invention is our legacy.", "Craft an arcane item."),
    SHADOWLORD("Death is only the beginning.", "Cast the Shadowlord ritual."),
    DEITY("Death is but an inconvenience to a god.", "Cast apotheosis."),
    DEMIURGE("All the world according to our will.", "Unobtainable."),
    DIVINE("All that you know is at an end.", "Unobtainable."),
    BATTLEMAGE("Magic is sharper than any sword.", "Kill a player with magic."),
    GRANDMASTER("The great mystery is revealed.", "Master every spell.");

    public final String description, goal;

    Title(String description, String goal) {
        this.description = description;
        this.goal = goal;
    }

    public Component displayName() {
        return Component.text(this.toString())
            .hoverEvent(Component.textOfChildren(Component.text(this.description), Component.newline(), Component.text(this.goal)));
    }

    @Override
    public String toString() {
        return ResourceManager.pascalCase(this.name().replace('_', ' '));
    }
}
