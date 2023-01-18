package mx.kenzie.witchcraft.ward;

import mx.kenzie.witchcraft.data.Coven;

public enum WardType {
    WARLOCK_ONLY,
    FREEZE,
    NOTIFY,
    REVEAL,
    PROTECT_MEMBERS,
    BLOCK_TELEPORT,
    PROTECT_TERRAIN;
    
    public AreaWardInstance create(Coven coven) {
        return new CovenHomeWardInstance(this, coven, -1);
    }
    
}
