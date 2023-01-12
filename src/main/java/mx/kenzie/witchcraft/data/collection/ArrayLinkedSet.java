package mx.kenzie.witchcraft.data.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;

public class ArrayLinkedSet<Type> extends LinkedHashSet<Type> {
    
    protected transient Consumer<ArrayLinkedSet<Type>> update = (thing) -> {};
    
    public ArrayLinkedSet(Type[] array, Consumer<ArrayLinkedSet<Type>> update) {
        super(array.length);
        super.addAll(List.of(array));
        this.update = update;
    }
    
    @Override
    public void clear() {
        super.clear();
        this.update.accept(this);
    }
    
    @Override
    public boolean removeAll(Collection<?> c) {
        this.flagDirty = true;
        boolean b = super.removeAll(c);
        this.flagDirty = false;
        if (b) this.update.accept(this);
        return b;
    }
    
    @Override
    public boolean remove(Object o) {
        boolean remove = super.remove(o);
        if (remove) this.update.accept(this);
        return remove;
    }
    
    @Override
    public boolean add(Type type) {
        boolean add = super.add(type);
        if (add && !flagDirty) this.update.accept(this);
        return add;
    }
    
    protected transient boolean flagDirty;
    
    @Override
    public boolean addAll(@NotNull Collection<? extends Type> c) {
        this.flagDirty = true;
        boolean b = super.addAll(c);
        this.flagDirty = false;
        if (b) this.update.accept(this);
        return b;
    }
}
