package mx.kenzie.witchcraft.spell.single;

import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.Protection;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.Position;
import mx.kenzie.witchcraft.data.recipe.StorageGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.EndGateway;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ThassalurgySpell extends AbstractTargetedSpell {
    protected transient Target target;
    
    public ThassalurgySpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final RayTraceResult result = target.result();
        final Block beneath = result.getHitBlock();
        final BlockFace face = result.getHitBlockFace();
        assert beneath != null && face != null;
        final Block first = beneath.getRelative(result.getHitBlockFace()), second = first.getRelative(BlockFace.UP);
        if (!(caster instanceof Player player)) return;
        final PlayerData data = PlayerData.getData(player);
        final List<Position> list = new ArrayList<>(data.getKnownLocations());
        list.removeIf(position -> !position.isValid());
        list.removeIf(position -> position.getWorld() != caster.getWorld());
        final Position[] positions = list.toArray(new Position[0]);
        final List<ItemStack> buttons = new ArrayList<>(positions.length);
        for (Position position : positions) buttons.add(position.create());
        final PaginatedGUI gui = new StorageGUI(WitchcraftAPI.plugin, 54, "Destination") {
            {inventory.setMaxStackSize(127);}
            
            @Override
            public void onInventoryClick(InventoryClickEvent event) {
                event.setCancelled(true);
            }
        };
        final BiConsumer<Player, InventoryClickEvent> consumer = (clicker, event) -> {
            final int slot = event.getSlot();
            if (slot < 0) return;
            if (slot >= positions.length) return;
            final Position position = positions[slot];
            clicker.closeInventory();
            this.doGateway(position, first, second);
            
        };
        assembleMenu(player, buttons, gui, consumer);
    }
    
    protected void doGateway(Position position, Block first, Block second) {
        final BlockData data = Material.END_GATEWAY.createBlockData();
        first.setBlockData(data);
        second.setBlockData(data);
        final Location location = position.getLocation();
        assert location.getWorld() == first.getWorld();
        if (first.getState() instanceof EndGateway gateway) {
            gateway.setAge(Integer.MIN_VALUE);
            gateway.setExitLocation(location);
            gateway.setExactTeleport(true);
            gateway.update(true, false);
        }
        if (second.getState() instanceof EndGateway gateway) {
            gateway.setAge(Integer.MIN_VALUE);
            gateway.setExitLocation(location);
            gateway.setExactTeleport(true);
            gateway.update(true, false);
        }
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        if (!(caster instanceof Player)) return false;
        if (!Protection.getInstance().canTeleport(caster, caster.getLocation())) return false;
        this.target = this.getTarget(caster, 10, false);
        return target.target() != null && target.result() != null;
    }
}
