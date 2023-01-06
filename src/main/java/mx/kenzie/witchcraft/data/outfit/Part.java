package mx.kenzie.witchcraft.data.outfit;

import mx.kenzie.fern.meta.Optional;
import org.jetbrains.annotations.NotNull;

public class Part {
    
    public @NotNull
    @Optional Render inner = Render.NONE, outer = Render.NONE;
    
    public Part() {
    }
    
    public Part(@NotNull Render inner, @NotNull Render outer) {
        this.inner = inner;
        this.outer = outer;
    }
    
    public static Part empty() {
        return new EmptyPart();
    }
    
    public boolean isEmpty() {
        return inner == Render.NONE && outer == Render.NONE;
    }
    
    public enum Render {
        MERGE, REPLACE, NONE
    }
}

class EmptyPart extends Part {
    @Override
    public boolean isEmpty() {
        return true;
    }
}
