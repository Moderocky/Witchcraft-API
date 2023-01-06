package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("all")
public class NaturesFriendSpell extends AbstractSummonSpell {
    
    protected transient final Class[] animals = {Sheep.class, Cow.class, Pig.class, Chicken.class, Donkey.class};
    
    public NaturesFriendSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0.5, 0.2, 0.5);
        WitchcraftAPI.client.particles(Particle.COMPOSTER)
            .playSpiral(location.setDirection(new Vector(0, 1, 0)), 0.8, 2, 10, 2);
        location.getWorld().spawn(location, animals[ThreadLocalRandom.current().nextInt(animals.length)]);
    }
}
