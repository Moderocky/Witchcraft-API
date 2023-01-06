package mx.kenzie.witchcraft.data.outfit;

import mx.kenzie.fern.meta.Optional;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An outfit's texture data, used by the renderer.
 */
public class OutfitData implements Comparable<Integer> {
    private static final Part EMPTY = Part.empty();
    
    /**
     * The ID of the outfit texture in the file source.
     */
    public String texture_id;
    
    /**
     * The weight given to this in the outfit renderer.
     * Heavier options are rendered last, making sure they are prioritised.
     * <p>
     * It is not recommended to use the 'overwrite' option in a heavy part,
     * since this could erase previously-rendered items.
     */
    public int weight = 0;
    
    /**
     * What kind of outfit this counts as.
     */
    public Clothing slot = Clothing.JACKET;
    
    /**
     * The body parts, where this will be rendered.
     */
    public @NotNull
    @Optional Part head = EMPTY, body = EMPTY,
        right_arm = EMPTY, left_arm = EMPTY,
        right_leg = EMPTY, left_leg = EMPTY;
    
    /**
     * If this outfit can actually be rendered.
     */
    public boolean isValid() {
        return texture_id != null && !this.isEmpty();
    }
    
    /**
     * If this outfit is empty (has no actual texturing.)
     */
    public boolean isEmpty() {
        return head.isEmpty() && body.isEmpty()
            && right_arm.isEmpty() && left_arm.isEmpty()
            && right_leg.isEmpty() && left_leg.isEmpty();
    }
    
    @Override
    public int compareTo(@NotNull Integer o) {
        return Integer.compare(weight, o);
    }
    
    public Map<String, Part> getParts() {
        final Map<String, Part> map = new LinkedHashMap<>(6);
        map.put("head", head);
        map.put("body", body);
        map.put("right_arm", right_arm);
        map.put("left_arm", left_arm);
        map.put("right_leg", right_leg);
        map.put("left_leg", left_leg);
        return map;
    }
}
