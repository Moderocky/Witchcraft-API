package mx.kenzie.witchcraft.spell.single;

import org.bukkit.Material;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BarricadeSpell extends RaiseWallSpell {
    public BarricadeSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected Material getMaterial() {
        final Material[] materials = new Material[]{Material.STONE, Material.ANDESITE, Material.TUFF, Material.COBBLESTONE};
        return materials[ThreadLocalRandom.current().nextInt(materials.length)];
    }

}
