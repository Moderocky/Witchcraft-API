package mx.kenzie.witchcraft.data;

import mx.kenzie.witchcraft.data.item.ItemArchetype;
import net.kyori.adventure.text.Component;

/**
 * A deity that a Warlock-class player can swear fealty to.
 * This has very little effect, except for informing the late-game item they get from Godsend.
 */
public enum WarlockDeity {
    ARCHANDER("rabaton", false,
        Component.text("")
        ),
    NEFAERIAN("endragora", true),
    RENOVAMEN("varrichor", true);
    
    public final String weaponId;
    public final boolean locked;
    public final Component[] speech;
    
    WarlockDeity(String weaponId, boolean locked, Component... speech) {
        this.weaponId = weaponId;
        this.locked = locked;
        this.speech = speech;
    }
    
    public ItemArchetype getWeapon() {
        return ItemArchetype.of(weaponId);
    }
}
