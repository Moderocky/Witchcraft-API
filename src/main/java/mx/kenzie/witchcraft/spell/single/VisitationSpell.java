package mx.kenzie.witchcraft.spell.single;

import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.Position;
import mx.kenzie.witchcraft.data.recipe.StorageGUI;
import mx.kenzie.witchcraft.entity.MalleablePortal;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

public class VisitationSpell extends TeleportationCircleSpell {
    protected transient Block target;
    
    public VisitationSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0, 0.5, 0);
        if (!(caster instanceof Player player)) return;
        location.setDirection(location.toVector().subtract(caster.getLocation().toVector()));
        location.setPitch(0);
        final List<Position> list = new ArrayList<>();
        for (Player online : Bukkit.getOnlinePlayers()) list.add(new Position.Person(online));
        final Position[] positions = list.toArray(new Position[0]);
        final List<ItemStack> buttons = new ArrayList<>(positions.length);
        for (Position position : positions) buttons.add(position.create());
        final PaginatedGUI gui = new StorageGUI(WitchcraftAPI.plugin, 54, "Destination");
        final BiConsumer<Player, InventoryClickEvent> consumer = (clicker, event) -> {
            final int slot = event.getSlot();
            if (slot < 0) return;
            if (slot >= positions.length) return;
            final Position position = positions[slot];
            clicker.closeInventory();
            this.doPortal(position, location);
            
        };
        assembleMenu(player, buttons, gui, consumer);
        this.target = null;
    }
    
    protected void doPortal(Position position, Location location) {
        final MalleablePortal portal = WitchcraftAPI.minecraft.tangPortal(location);
        portal.setCollideConsumer(entity -> {
            if (!(entity instanceof LivingEntity living)) return;
            this.doTeleport(living, position, 5);
            portal.getBukkitEntity().remove();
        });
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
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
        if (blocks.size() == 1) this.target = blocks.get(0);
        else this.target = blocks.get(ThreadLocalRandom.current().nextInt(blocks.size()));
        return true;
    }
}
