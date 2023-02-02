package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.Flags;
import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FlightSpell extends StandardSpell {

    protected final Duration duration = Duration.of(3, ChronoUnit.MINUTES);
    private transient final ParticleCreator creator = ParticleCreator.of(Particle.WAX_ON.builder().count(0));

    public FlightSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return !Flags.read(caster).contains(Flags.FLIGHT);
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        Flags.of(caster).add(Flags.FLIGHT);
        final Minecraft minecraft = Minecraft.getInstance();
        if (caster instanceof Player player) {
            player.setAllowFlight(true);
            WitchcraftAPI.scheduler.schedule(() -> minecraft.ensureMain(() -> {
                Flags.of(caster).remove(Flags.FLIGHT);
                if (player.isOnline() && !player.isDead())
                    player.setAllowFlight(false);
            }), duration.toSeconds(), TimeUnit.SECONDS);
        } else {
            final Object controller = minecraft.switchFlyController(caster);
            WitchcraftAPI.scheduler.schedule(() -> minecraft.ensureMain(() -> {
                Flags.of(caster).remove(Flags.FLIGHT);
                if (caster.isDead()) return;
                minecraft.resetController(caster, controller);
            }), duration.toSeconds(), TimeUnit.SECONDS);
        }
        WitchcraftAPI.executor.submit(() -> {
            final ParticleBuilder builder = creator.getBuilder();
            for (int i = 0; i < 3; i++) {
                final Location centre = caster.getEyeLocation();
                for (Vector vector : creator.createPoof(3, 50)) {
                    final Location point = centre.clone().add(vector);
                    final Vector offset = point.toVector().subtract(centre.toVector()).multiply(0.2);
                    builder.offset(offset.getX(), offset.getY(), offset.getZ());
                    builder.location(point).spawn();
                    WitchcraftAPI.sleep(20);
                }
            }
        });
    }


}
