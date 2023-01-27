package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.Position;
import mx.kenzie.witchcraft.world.WorldEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class IncursionSpell extends VisitationSpell {
    public IncursionSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    protected void doPortal(Position position, Location location, Player player) {
        final WorldEvent event = Minecraft.getInstance()
            .startWorldEvent(WorldEvent.Type.DEMON_INVASION, position.getLocation(), player);
        player.sendMessage(Component.text("Your demon army is preparing for battle...", WitchcraftAPI.colors()
            .lowlight()));
        for (Player target : event.getInvolvedPlayers()) {
            target.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                Duration.of(1, ChronoUnit.SECONDS),
                Duration.of(3, ChronoUnit.SECONDS),
                Duration.of(1, ChronoUnit.SECONDS)
            ));
            target.sendTitlePart(TitlePart.SUBTITLE, Component.text("A demon invasion is approaching...", WitchcraftAPI.colors()
                .lowlight()));
            target.sendTitlePart(TitlePart.TITLE, Component.empty());
        }
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player;
    }
    
}
