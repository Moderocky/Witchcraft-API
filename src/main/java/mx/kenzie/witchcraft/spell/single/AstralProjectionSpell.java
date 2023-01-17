package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
        final int lifetime = (int) Math.floor(10 + (amplitude * 2));
        final Location start = player.getLocation();
        final Minecraft minecraft = Minecraft.getInstance();
        final LivingEntity mirror = Minecraft.getInstance().spawnMirrorImageNoAI(start, player);
        mirror.setHealth(Math.min(caster.getHealth(), minecraft.getMaxHealth(mirror)));
        player.setGameMode(GameMode.SPECTATOR);
        final AtomicBoolean running = new AtomicBoolean(true);
        final Runnable end = () -> {
            if (!running.get()) return;
            player.setGameMode(GameMode.ADVENTURE);
            if (mirror.isDead()) {
                player.damage(50);
                return;
            }
            player.setHealth(Math.min(minecraft.getMaxHealth(player), mirror.getHealth()));
            player.teleport(mirror);
            mirror.remove();
            running.set(false);
        };
        WitchcraftAPI.scheduler.scheduleAtFixedRate(() -> {
            if (!player.isOnline()) throw new RuntimeException();
            if (player.getGameMode() != GameMode.SPECTATOR) throw new RuntimeException();
            if (mirror.isDead() || player.getWorld() != start.getWorld() || player.getLocation()
                .distanceSquared(start) > (30 * 30)) {
                Bukkit.getScheduler().runTask(WitchcraftAPI.plugin, end);
                throw new RuntimeException();
            }
        }, 300, 100, TimeUnit.MILLISECONDS);
        Bukkit.getScheduler().scheduleSyncDelayedTask(WitchcraftAPI.plugin, end, lifetime * 20L);
    }
    
}
