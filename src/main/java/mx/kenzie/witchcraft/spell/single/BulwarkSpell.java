package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Map;

public class BulwarkSpell extends StandardSpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(Material.MUD);
    protected transient final VectorShape circle = creator.createCircle(new Vector(0, 1, 0), 2.2, 60);
    protected transient Collection<Block> blocks;

    public BulwarkSpell(Map<String, Object> map) {
        super(map);
    }

    public static boolean isBlockOkay(Block block) { // needs to be a terrain block with space above it
        if (!block.isSolid()) return false;
        final Block up;
        if ((up = block.getRelative(BlockFace.UP)).isSolid()) return false;
        if (up.getRelative(BlockFace.UP).isSolid()) return false;
        final Material material = block.getType();
        if (!material.isOccluding()) return false;
        return (Tag.MINEABLE_PICKAXE.isTagged(material) || Tag.MINEABLE_SHOVEL.isTagged(material));
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return caster.isOnGround();
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        Minecraft.getInstance().spawnBlockShield(caster);
        final Location location = caster.getLocation();
        final World world = caster.getWorld();
        world.playSound(location, Sound.ENTITY_WARDEN_DIG, 1.3F, 0.8F);
        this.circle.draw(location, 30);
    }

}
