package mx.kenzie.witchcraft.npc;

import mx.kenzie.fern.Fern;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public class CharacterArchetype implements Keyed {
    public transient final String id;
    
    public CharacterArchetype(String id, InputStream stream) {
        this.id = id.toLowerCase().trim().replace(' ', '_');
        final Fern fern = new Fern(stream);
        fern.toObject(this);
    }
    
    protected CharacterArchetype() {
        this.id = "unknown";
    }
    
    @Override
    public @NotNull NamespacedKey getKey() {
        return NamespacedKey.minecraft(id);
    }
    
}
