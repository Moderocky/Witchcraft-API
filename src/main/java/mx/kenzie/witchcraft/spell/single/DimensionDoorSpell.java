package mx.kenzie.witchcraft.spell.single;

import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.RealmManager;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.Position;
import mx.kenzie.witchcraft.data.recipe.StorageGUI;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.entity.DimensionDoor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class DimensionDoorSpell extends ThassalurgySpell {
    protected transient Target target;
    
    public DimensionDoorSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final RayTraceResult result = target.result();
        final Block beneath = result.getHitBlock();
        final BlockFace face = result.getHitBlockFace();
        if (beneath == null || face == null) return;
        final Block first = beneath.getRelative(result.getHitBlockFace());
        if (!(caster instanceof Player player)) return;
        RealmManager.getInstance().getSpawnLocations(player).thenAccept(list -> {
            list.removeIf(position -> !position.isValid());
            list.removeIf(position -> position.getWorld() == caster.getWorld());
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
                this.doGateway(position, first);
            };
            Minecraft.getInstance().ensureMain(() -> assembleMenu(player, buttons, gui, consumer));
        });
    }
    
    protected void doGateway(Position position, Block first) {
        final DimensionDoor door = CustomEntityType.DIMENSION_DOOR.spawn(first.getLocation().add(0.5, 0, 0.5));
        door.setOutcome(position.getLocation());
    }
    
}
