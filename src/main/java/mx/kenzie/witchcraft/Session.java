package mx.kenzie.witchcraft;

import mx.kenzie.witchcraft.spell.Pattern;
import mx.kenzie.witchcraft.spell.casting.Angle;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;

import java.time.Duration;
import java.util.List;

public interface Session {
    void resetCast();
    
    void input(PlayerInteractEvent event);
    
    void drawPattern(Pattern pattern, Location start, int delay);
    
    void drawPattern(Pattern pattern, Duration duration);
    
    void drawUI(List<Angle> angles);
}
