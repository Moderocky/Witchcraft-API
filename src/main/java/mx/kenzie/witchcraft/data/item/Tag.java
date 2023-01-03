package mx.kenzie.witchcraft.data.item;

import mx.kenzie.witchcraft.ResourceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Tag(String key) {
    
    private static final Set<Tag> TAGS = new HashSet<>();
    
    public static List<Tag> tags() {
        return new ArrayList<>(TAGS);
    }
    
    public static Tag[] values() {
        return TAGS.toArray(new Tag[0]);
    }
    
    public static Tag register(String name) {
        final String unqualify = name.replace(' ', '_').toUpperCase();
        final Tag tag = new Tag(unqualify);
        TAGS.add(tag);
        return tag;
    }
    
    public static Tag parse(String name) {
        final String unqualify = name.replace(' ', '_').toUpperCase();
        return Tag.valueOf(unqualify);
    }
    
    private static Tag valueOf(String key) {
        for (Tag tag : TAGS) if (tag.key.equals(key)) return tag;
        return null;
    }
    
    public String qualifiedName() {
        return ResourceManager.pascalCase(key.replace("_", " "));
    }
    
    public int weight() {
        return 10;
    }
    
}
