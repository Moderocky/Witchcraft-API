package mx.kenzie.witchcraft.spell.projectile;

import mx.kenzie.witchcraft.WitchcraftAPI;
import org.bukkit.Bukkit;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface Synchroniser {
    
    static <Q> Future<Q> sync0(Callable<Q> runnable) {
        return Bukkit.getScheduler().callSyncMethod(WitchcraftAPI.plugin, runnable);
    }
    
    default <Q> Future<Q> sync(Callable<Q> runnable) {
        return Bukkit.getScheduler().callSyncMethod(WitchcraftAPI.plugin, runnable);
    }
    
}
