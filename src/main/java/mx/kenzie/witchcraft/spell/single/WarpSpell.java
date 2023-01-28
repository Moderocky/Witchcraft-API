package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;

public class WarpSpell extends AbstractTeleportSpell {

    private transient List<Block> blocks;

    public WarpSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = caster.getLocation();
        final World world = location.getWorld();
        final Location end = blocks.get(0).getLocation()
            .add(0.5, 0.1, 0.5);
        end.setDirection(location.getDirection());
        ParticleCreator creator = WitchcraftAPI.client.particles(Particle.PORTAL);
        creator.getBuilder().force(true).count(9);
        Location s1 = caster.getLocation().clone();
        s1.add(0, 1.1, 0);
        s1.setPitch(-90);
        s1.setYaw(0);
        Location s2 = end.clone();
        s2.add(0, 1.1, 0);
        s2.setPitch(-90);
        s2.setYaw(0);
        creator.drawLine(s1, s2, 0.75);
        caster.teleport(end);
        world.playSound(s1, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 0.7F);
        world.playSound(s2, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.3F);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        final Block test = caster.getTargetBlockExact(25);
        if (test == null) return false;
        final Location centre = test.getLocation();
        double radius = 4.0D;
        this.blocks = getValidTeleportSpacesNoSight(centre, radius);
        return !blocks.isEmpty();
    }
}
