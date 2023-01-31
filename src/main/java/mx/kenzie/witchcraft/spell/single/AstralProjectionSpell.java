package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Map;

public class AstralProjectionSpell extends StandardSpell {
    public AstralProjectionSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player;
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        final int lifetime = (int) Math.floor(8 + (amplitude * 2));
        final Location start = player.getLocation();
        final Minecraft minecraft = Minecraft.getInstance();
        final LivingEntity mirror = Minecraft.getInstance().spawnMirrorImageNoAI(start, player);
        mirror.setHealth(Math.min(caster.getHealth(), minecraft.getMaxHealth(mirror)));
        player.setGameMode(GameMode.SPECTATOR);
        final Listener listener = new Listener() {
            @EventHandler
            public void event(PlayerTeleportEvent event) {
                if (event.getPlayer() != player) return;
                if (event.getCause() != PlayerTeleportEvent.TeleportCause.SPECTATE) return;
                event.setCancelled(true);
            }
        };
        Bukkit.getPluginManager().registerEvents(listener, WitchcraftAPI.plugin);
        final Runnable end = () -> {
            PlayerTeleportEvent.getHandlerList().unregister(listener);
            player.setGameMode(GameMode.ADVENTURE);
            if (mirror.isDead()) {
                player.damage(50);
                return;
            }
            player.setHealth(Math.min(minecraft.getMaxHealth(player), mirror.getHealth()));
            player.teleport(mirror);
            mirror.remove();
        };
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, end, lifetime * 20L);
    }

}
