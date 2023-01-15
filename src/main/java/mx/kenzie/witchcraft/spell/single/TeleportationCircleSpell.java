package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Protection;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.Position;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class TeleportationCircleSpell extends TeleportSpell {
    
    public TeleportationCircleSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        final PlayerData data = PlayerData.getData(player);
        final List<Position> list = new ArrayList<>(data.getKnownLocations());
        list.removeIf(position -> !position.isValid());
        list.removeIf(position -> position.getWorld() != caster.getWorld());
        this.createMenu(player, list);
    }
    
    @Override
    protected void doTeleport(LivingEntity caster, Position target, int range) {
        final Location start = caster.getLocation();
        final Location location = target.getLocation();
        final List<LivingEntity> list = new ArrayList<>(start.getNearbyLivingEntities(range, 10));
        final List<Block> blocks = getValidTeleportSpacesNoSight(location, 12);
        for (LivingEntity entity : list) {
            if (!Protection.getInstance().canTeleport(entity, location)) {
                entity.sendMessage(Component.text("Something blocked your teleport from the other side...", WitchcraftAPI.colors()
                    .lowlight()));
                return;
            }
            final Location result = blocks.isEmpty() ? location : blocks
                .remove(ThreadLocalRandom.current().nextInt(blocks.size())).getLocation().add(0.5, 0.1, 0.5);
            final VectorShape spiral = WitchcraftAPI.client.particles(soul)
                .createSpiral(new Vector(0, 1, 0), 2, 0.65, 2, 16);
            final VectorShape poof = WitchcraftAPI.client.particles(builder).createPoof(0.8, 12);
            entity.teleportAsync(result).thenAccept(thing -> {
                if (!thing) return;
                poof.draw(result.add(0, 1, 0));
                this.drawShape(spiral, result);
            });
            poof.draw(start.add(0, 1, 0));
            this.drawShape(spiral, start);
        }
    }
    
}
