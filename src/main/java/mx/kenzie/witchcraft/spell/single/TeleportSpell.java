package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.Position;
import mx.kenzie.witchcraft.data.recipe.StorageGUI;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class TeleportSpell extends AbstractTeleportSpell {
    
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.REDSTONE)
        .color(Color.fromRGB(35, 173, 252))
        .count(0).force(true);
    protected final ParticleBuilder soul = new ParticleBuilder(Particle.SOUL).count(0).extra(0);
    
    public TeleportSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        final PlayerData data = PlayerData.getData(player);
        final Position[] positions = data.getKnownLocations().toArray(new Position[0]);
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
            this.doTeleport(clicker, position);
            
        };
        assembleMenu(player, buttons, gui, consumer);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player; // todo check if caster can teleport ?
    }
    
    protected void doTeleport(LivingEntity caster, Position target) {
        final Location start = caster.getLocation();
        final Location location = target.getLocation();
        final List<Block> blocks = getValidTeleportSpacesNoSight(location, 5);
        final Location result = blocks.isEmpty() ? location : blocks.get(0).getLocation().add(0.5, 0.1, 0.5);
        result.setDirection(location.toVector().add(new Vector(0, 1, 0)).subtract(result.toVector()));
        final VectorShape spiral = WitchcraftAPI.client.particles(soul)
            .createSpiral(new Vector(0, 1, 0), 2, 0.65, 2, 16);
        final VectorShape poof = WitchcraftAPI.client.particles(builder).createPoof(0.8, 12);
        caster.teleportAsync(result).thenAccept(thing -> {
            if (!thing) return;
            poof.draw(result.add(0, 1, 0));
            this.drawShape(spiral, result.setDirection(new Vector(0, 1, 0)));
        });
        poof.draw(start.add(0, 1, 0));
        this.drawShape(spiral, start);
    }
    
    private void drawShape(VectorShape shape, Location location) {
        WitchcraftAPI.executor.submit(() -> {
            final ParticleBuilder builder = shape.builder();
            for (Vector vector : shape) {
                final Location point = location.clone().add(vector);
                builder.location(point).spawn();
                WitchcraftAPI.sleep(10);
            }
        });
    }
    
}
