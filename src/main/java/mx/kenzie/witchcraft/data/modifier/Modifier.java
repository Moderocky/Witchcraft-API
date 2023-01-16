package mx.kenzie.witchcraft.data.modifier;

public record Modifier(Type type, double amount, long timeout) {
    
    public static Modifier of(Type type, double amount, int ticks) {
        return new Modifier(type, amount, System.currentTimeMillis() + (50L * ticks));
    }
    
    public enum Type {
        AMPLITUDE, ARMOUR, BONUS_ENERGY
    }
    
}
