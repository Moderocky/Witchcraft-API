package mx.kenzie.witchcraft.spell.casting;

import org.bukkit.entity.Player;

public record Angle(double yaw, double pitch) {
    public static Angle forPlayer(final Player player) {
        return new Angle(
            player.getLocation().getYaw(),
            player.getLocation().getPitch()
        );
    }

    @Override
    public String toString() {
        return "[%06.2f ψ + %06.2f θ]".formatted(yaw, pitch);
    }
}
