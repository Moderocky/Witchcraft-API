package mx.kenzie.witchcraft.entity.goal;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PlayerInteraction extends Interaction {
    
    protected final Player player;
    protected final Hand hand;
    
    public PlayerInteraction(Player player) {
        this(player, Hand.MAIN_HAND);
    }
    
    public PlayerInteraction(Player player, Hand hand) {
        this.player = player;
        this.hand = hand;
    }
    
    public Hand getHand() {
        return hand;
    }
    
    public enum Hand {
        MAIN_HAND, OFF_HAND
    }
    
    public static class Position extends PlayerInteraction {
        
        protected final Vector position;
        
        public Position(Player player, Hand hand, Vector position) {
            super(player, hand);
            this.position = position;
        }
        
        public Vector getPosition() {
            return position;
        }
        
    }
}
