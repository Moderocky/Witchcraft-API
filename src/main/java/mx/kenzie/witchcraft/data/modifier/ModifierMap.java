package mx.kenzie.witchcraft.data.modifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ModifierMap extends HashMap<String, Modifier> {

    public static final ModifierMap DEFAULT = new ModifierMap(0) {
        @Override
        public void putAll(Map<? extends String, ? extends Modifier> m) {
        }

        @Override
        public double get(Modifier.Type type) {
            return 0;
        }

        @Override
        public Modifier put(String reason, Modifier modifier) {
            return null;
        }

        @Override
        public boolean isPresent(Modifier.Type type) {
            return false;
        }
    };

    public ModifierMap() {
    }

    private ModifierMap(int initialCapacity) {
        super(initialCapacity);
    }

    public double get(Modifier.Type type) {
        if (this.isEmpty()) return 0;
        final long time = System.currentTimeMillis();
        double count = 0;
        final Iterator<Modifier> iterator = this.values().iterator();
        while (iterator.hasNext()) {
            final Modifier modifier = iterator.next();
            if (modifier.timeout() <= time) {
                iterator.remove();
                continue;
            }
            if (modifier.type() != type) continue;
            count += modifier.amount();
        }
        return count;
    }

    @Override
    public Modifier get(Object key) {
        if (this.isEmpty()) return null;
        final Modifier modifier = super.get(key);
        if (modifier == null) return null;
        if (modifier.timeout() < System.currentTimeMillis()) return modifier;
        this.remove(key, modifier);
        return null;
    }

    @Override
    public Modifier put(String reason, Modifier modifier) {
        return super.put(reason, modifier);
    }

    public void removeAll(Modifier.Type type) {
        if (this.isEmpty()) return;
        this.values().removeIf(value -> value.type() == type);
    }

    public boolean isPresent(Modifier.Type type) {
        if (this.isEmpty()) return false;
        final long time = System.currentTimeMillis();
        final Iterator<Modifier> iterator = this.values().iterator();
        while (iterator.hasNext()) {
            final Modifier modifier = iterator.next();
            if (modifier.timeout() <= time) {
                iterator.remove();
                continue;
            }
            if (modifier.type() != type) return true;
            if (modifier.amount() > 0) return true;
        }
        return false;
    }

}
