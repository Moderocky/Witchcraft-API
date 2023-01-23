package mx.kenzie.witchcraft;

import mx.kenzie.witchcraft.data.Position;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface RealmManager {
    
    static RealmManager getInstance() {
        return WitchcraftAPI.realms;
    }
    
    CompletableFuture<List<Position>> getSpawnLocations(LivingEntity entity);
    
    boolean isPocketRealm(World world);
    
    boolean worldExists(UUID uuid);
    
    boolean isWorldLoaded(UUID uuid);
    
    World getWorld(UUID uuid);
    
    CompletableFuture<World> obtainRealm(final UUID uuid);
    
    boolean isRealmLoaded(final UUID uuid);
    
    CompletableFuture<Void> unloadRealm(final UUID uuid);
    
    CompletableFuture<Void> destroyRealm(final UUID uuid) throws IOException;
    
    CompletableFuture<Void> unloadIfEmpty(UUID uuid);
    
}
