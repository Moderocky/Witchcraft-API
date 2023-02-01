package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
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

public class RagnarokSpell extends StandardSpell {
    public RagnarokSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        final Location location = caster.getLocation().toCenterLocation().add(0, -0.5, 0);
        final WorldEvent event = Minecraft.getInstance().startWorldEvent(WorldEvent.Type.RAGNAROK, location, player);
        for (Player target : event.getInvolvedPlayers()) {
            target.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                Duration.of(1, ChronoUnit.SECONDS),
                Duration.of(3, ChronoUnit.SECONDS),
                Duration.of(1, ChronoUnit.SECONDS)
            ));
            target.sendTitlePart(TitlePart.SUBTITLE, Component.text("The sky is falling...", WitchcraftAPI.colors()
                .lowlight()));
            target.sendTitlePart(TitlePart.TITLE, Component.empty());
        }
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player;
    }

}
