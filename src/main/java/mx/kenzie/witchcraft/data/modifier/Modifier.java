package mx.kenzie.witchcraft.data.modifier;

import mx.kenzie.witchcraft.data.PlayerData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public record Modifier(Type type, double amount, long timeout) {

    public static Modifier of(Type type, double amount, int ticks) {
        return new Modifier(type, amount, System.currentTimeMillis() + (50L * ticks));
    }

    public static ModifierMap get(Entity entity) {
        if (entity instanceof Player player) return PlayerData.getData(player).temporary.modifiers;
        return ModifierMap.DEFAULT;
    }

    public enum Type {
        AMPLITUDE, ARMOUR, BONUS_ENERGY, DEMON_COOLDOWN, ENERGY_REGENERATION
    }

}
