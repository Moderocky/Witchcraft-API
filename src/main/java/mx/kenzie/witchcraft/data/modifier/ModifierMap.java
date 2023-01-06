package mx.kenzie.witchcraft.data.modifier;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.HashMap;

public class ModifierMap extends HashMap<String, Modifier> {
    
    public static final ModifierMap DEFAULT = new ModifierMap(0);
    
    public ModifierMap() {
    }
    
    private ModifierMap(int initialCapacity) {
        super(initialCapacity);
    }
    
    public double get(Modifier.Type type) {
        if (this.isEmpty()) return 0;
        final long time = System.currentTimeMillis();
        final AtomicDouble count = new AtomicDouble(0);
        this.values().removeIf(value -> {
            if (value.timeout() >= time) return true;
            if (value.type() != type) return false;
            count.addAndGet(value.amount());
            return false;
        });
        return count.get();
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
        for (Modifier value : this.values()) {
            if (value.type() != type) continue;
            if (value.amount() > 0) return true;
        }
        return false;
    }
}
