package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

public class FlashSpell extends AbstractTeleportSpell {
    
    private transient List<Block> blocks;
    
    public FlashSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = caster.getEyeLocation();
        final World world = location.getWorld();
        final Location end = blocks.get(0).getLocation()
            .add(0.5, 0.1, 0.5);
        end.setDirection(location.getDirection());
        final ParticleCreator creator = WitchcraftAPI.client.particles(Particle.FIREWORKS_SPARK);
        creator.getBuilder().force(true).count(3);
        for (Vector vector : creator.createLine(location, end, 0.3)) {
            final Location point = location.clone().add(vector).add(ParticleCreator.random());
            creator.getBuilder().location(point).spawn();
        }
        caster.teleport(end, PlayerTeleportEvent.TeleportCause.PLUGIN);
        world.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8F, 1.4F);
        world.playSound(end, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8F, 1.4F);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        final Block test = caster.getTargetBlockExact(12);
        if (test != null) {
            final Location centre = test.getLocation();
            double radius = 2.5D;
            this.blocks = getUnoccupiedTeleportSpaces(centre, radius);
            return blocks.size() >= 1;
        } else {
            final Location eye = caster.getEyeLocation();
            eye.add(eye.getDirection().multiply(12));
            final Block block = eye.getBlock();
            if (isInvalidStand(block)) return false;
            if (isInvalidAbove(block)) return false;
            this.blocks = List.of(block);
            return true;
        }
    }
}
