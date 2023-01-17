package mx.kenzie.witchcraft.data;

import mx.kenzie.witchcraft.data.item.ItemArchetype;

/**
 * A deity that a Warlock-class player can swear fealty to.
 * This has very little effect, except for informing the late-game item they get from Godsend.
 */
public enum WarlockDeity {
    ARCHANDER("rabaton", false),
    NEFAERIAN("endragora", true),
    RENOVAMEN("varrichor", true),
    TERRORACH("terrorach_staff", false);
    
    public final String weaponId;
    public final boolean locked;
    
    WarlockDeity(String weaponId, boolean locked) {
        this.weaponId = weaponId;
        this.locked = locked;
    }
    
    public ItemArchetype getWeapon() {
        return ItemArchetype.of(weaponId);
    }
}
