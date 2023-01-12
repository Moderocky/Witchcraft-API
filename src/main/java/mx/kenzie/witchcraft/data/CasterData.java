package mx.kenzie.witchcraft.data;

import mx.kenzie.sloth.Cache;

import java.util.HashMap;
import java.util.UUID;

public abstract class CasterData extends LazyWrittenData {
    protected static final Cache<UUID, CasterData> DATA = Cache.soft(HashMap::new);
    protected transient UUID uuid;
}
