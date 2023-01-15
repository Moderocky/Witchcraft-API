package mx.kenzie.witchcraft;

import org.bukkit.World;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface RealmManager {
    
    static RealmManager getInstance() {
        return WitchcraftAPI.realms;
    }
    
    CompletableFuture<World> obtainRealm(final UUID uuid);
    
    boolean isRealmLoaded(final UUID uuid);
    
    CompletableFuture<Void> unloadRealm(final UUID uuid);
    
    CompletableFuture<Void> destroyRealm(final UUID uuid) throws IOException;
}
