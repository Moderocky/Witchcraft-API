package mx.kenzie.witchcraft.data;

import mx.kenzie.sloth.Cache;

import java.util.UUID;
import java.util.WeakHashMap;

public abstract class CasterData extends LazyWrittenData {
    protected static final Cache<UUID, CasterData> DATA = Cache.soft(WeakHashMap::new);
    protected transient UUID uuid;
}
