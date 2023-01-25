package mx.kenzie.witchcraft.entity.goal;

import org.bukkit.entity.Player;

public class PlayerInteraction extends Interaction {
    
    protected final Player player;
    
    public PlayerInteraction(Player player) {
        this.player = player;
    }
}
