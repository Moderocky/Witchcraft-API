package mx.kenzie.witchcraft.spell;

public enum SpellResult {
    SUCCESS(true),
    NO_ENERGY(false),
    NO_INGREDIENT(false),
    WRONG_WORLD(false),
    MISSING_CIRCUMSTANCE(false),
    NOT_IN_HOME(false),
    OTHER(false);
    
    public final boolean success;
    
    SpellResult(boolean success) {
        this.success = success;
    }
}
