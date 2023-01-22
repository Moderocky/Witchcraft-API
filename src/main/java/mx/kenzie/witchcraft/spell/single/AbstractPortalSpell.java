package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.data.Coven;
import mx.kenzie.witchcraft.data.Position;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.entity.Portal;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;

public abstract class AbstractPortalSpell extends TeleportationCircleSpell {
    protected transient Block target;
    
    public AbstractPortalSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        final Coven coven = Coven.getCoven(caster);
        if (coven == null) return false;
        if (coven.getHome() == null) return false;
        Block block = caster.getTargetBlockExact(25, FluidCollisionMode.NEVER);
        if (block == null) return false;
        block = block.getRelative(BlockFace.UP);
        attempt:
        {
            if (AbstractTeleportSpell.isInvalidStand(block)) break attempt;
            if (AbstractTeleportSpell.isInvalidAbove(block)) break attempt;
            if (AbstractTeleportSpell.isInvalidBelow(block)) break attempt;
            this.target = block;
            return true;
        }
        final List<Block> blocks = AbstractSummonSpell.getValidSpawnSpaces(block.getLocation(), 3);
        if (blocks.isEmpty()) return false;
        this.target = blocks.get(0);
        return true;
    }
    
    protected void doPortal(Position position, Location location) {
        final Portal portal = CustomEntityType.TANG_PORTAL.spawn(location);
        portal.setOrientation(location.getDirection());
        portal.setCollideConsumer(entity -> {
            if (!(entity instanceof LivingEntity living)) return;
            this.doTeleport(living, position);
            portal.remove();
        });
    }
}
