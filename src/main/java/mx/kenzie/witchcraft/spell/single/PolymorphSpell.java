package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class PolymorphSpell extends AbstractTargetedSpell {

    protected transient final Color color = new Color(147, 18, 183);
    protected transient final ParticleCreator creator = ParticleCreator.of(new ParticleBuilder(Particle.SPELL_MOB)
        .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
        .count(0)
        .force(true));
    public static final Class<? extends Animals>[] ANIMALS = new Class[]{Pig.class, Sheep.class, Cow.class};

    public PolymorphSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final AbstractTargetedSpell.Target trace = this.getTarget(caster, range, true);
        final Location target = trace.target();
        final Entity found = trace.entity();
        creator.drawLine(caster.getEyeLocation(), target, 0.3);
        if (found == null) return;
        if (!(found instanceof Player player)) return;
        if (player.getGameMode() != GameMode.ADVENTURE) return;
        final int duration = (int) Math.floor(4 + (amplitude * 2));
        final Location start = player.getLocation();
        final World world = player.getWorld();
        final Animals animal = world.spawn(start, ANIMALS[ThreadLocalRandom.current().nextInt(ANIMALS.length)]);
        final Listener listener = new Listener() {
            @EventHandler
            public void event(PlayerStopSpectatingEntityEvent event) {
                if (event.getPlayer() != player) return;
                if (event.getPlayer().getGameMode() != GameMode.SPECTATOR) return;
                if (event.getSpectatorTarget() != animal) return;
                event.setCancelled(true);
            }

            @EventHandler
            public void event(PlayerTeleportEvent event) {
                if (event.getPlayer() != player) return;
                if (event.getCause() != PlayerTeleportEvent.TeleportCause.SPECTATE) return;
                event.setCancelled(true);
            }

            @EventHandler
            public void event(EntityDeathEvent event) {
                if (event.getEntity() != animal) return;
                player.setGameMode(GameMode.ADVENTURE);
            }
        };
        final Runnable end = () -> end(player, animal, listener);
        Bukkit.getPluginManager().registerEvents(listener, WitchcraftAPI.plugin);
        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(animal);
        WitchcraftAPI.scheduler.schedule(end, duration, TimeUnit.SECONDS);
    }

    public void end(Player player, Animals animals, Listener listener) {
        Minecraft.getInstance().ensureMain(() -> {
            try {
                final Location location = animals.getLocation();
                animals.remove();
                PlayerStopSpectatingEntityEvent.getHandlerList().unregister(listener);
                PlayerTeleportEvent.getHandlerList().unregister(listener);
                EntityDeathEvent.getHandlerList().unregister(listener);
                if (player.getGameMode() == GameMode.SPECTATOR) {
                    if (player.isDead()) return;
                    player.teleport(location);
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
            player.setGameMode(GameMode.ADVENTURE);
        });
    }

    public void event(PlayerStopSpectatingEntityEvent event) {
        event.setCancelled(true);
    }

}
