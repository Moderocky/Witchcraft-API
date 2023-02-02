package mx.kenzie.witchcraft;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;

public enum Flags {

    SPELL_DEMON_BALLS,
    SPELL_FIRE_RING, FLIGHT;

    private static final Map<Entity, FlagSet> MAP = new WeakHashMap<>();
    private static final FlagSet EMPTY = new EmptyFlagSet();

    public static @NotNull FlagSet of(Entity entity) {
        MAP.putIfAbsent(entity, new FlagSet());
        return MAP.getOrDefault(entity, EMPTY);
    }

    public static @NotNull FlagSet read(Entity entity) {
        return MAP.getOrDefault(entity, EMPTY);
    }

    public static boolean clean() {
        return MAP.values().removeIf(FlagSet::isEmpty);
    }

    public static class FlagSet extends HashSet<Flags> {

        public boolean has(Flags... flags) {
            for (Flags flag : flags) if (!this.contains(flag)) return false;
            return true;
        }

        public boolean hasAny(Flags... flags) {
            for (Flags flag : flags) if (this.contains(flag)) return true;
            return false;
        }

    }

    private static class EmptyFlagSet extends FlagSet {
        @Override
        public boolean add(Flags flags) {
            return false;
        }

        @Override
        public boolean addAll(@NotNull Collection<? extends Flags> c) {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NotNull Collection<?> c) {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }
    }

}
