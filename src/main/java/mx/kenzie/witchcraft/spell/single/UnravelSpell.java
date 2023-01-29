package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.Protection;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.Portal;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.Set;

public class UnravelSpell extends StandardSpell {
    protected transient ParticleCreator creator = ParticleCreator.of(Particle.SOUL_FIRE_FLAME.builder().count(0));

    public UnravelSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return Protection.getInstance().canBreak(caster, caster.getLocation());
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Minecraft minecraft = WitchcraftAPI.minecraft;
        final Block block = caster.getTargetBlockExact(Math.max(5, Math.min(20, range)));
        if (block != null && !Protection.getInstance().canBreak(caster, block.getLocation())) return;
        if (this.destroyConnected(block)) return; // cheaper to attempt this before entities due to square rooting
        final Set<Entity> set = WitchcraftAPI.minecraft.getTargetEntities(caster.getEyeLocation(), range, 0.82);
        for (Entity entity : set) {
            if (!(entity instanceof Portal)) continue;
            this.creator.drawPoof(entity.getLocation().add(0, 1, 0), 1.5, 10);
            entity.remove();
            break;
        }
    }

    protected boolean destroyConnected(Block block) {
        if (!this.isPortal(block)) return false;
        block.setType(Material.AIR, false);
        this.creator.drawPoof(block.getLocation().add(0.5, 0.5, 0.5), 1.2, 5);
        for (BlockFace value : BlockFace.values()) this.destroyConnected(block.getRelative(value));
        return true;
    }

    protected boolean isPortal(Block block) {
        if (block == null) return false;
        return switch (block.getType()) {
            case END_GATEWAY, END_PORTAL, NETHER_PORTAL -> true;
            default -> false;
        };
    }
}
