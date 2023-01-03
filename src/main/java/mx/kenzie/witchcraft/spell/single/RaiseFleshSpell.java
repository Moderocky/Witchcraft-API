package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class RaiseFleshSpell extends AbstractSummonSpell {
    
    private transient final ParticleCreator creator;
    
    public RaiseFleshSpell(Map<String, Object> map) {
        super(map);
        if (!WitchcraftAPI.isTest)
            creator = WitchcraftAPI.client.particles(new ParticleBuilder(Particle.BLOCK_CRACK)
                .data(Material.REDSTONE_BLOCK.createBlockData()));
        else creator = null;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation();
        this.creator.playSpiral(
            location.setDirection(new Vector(0, 1, 0)),
            0.8, 2, 10, 2
        );
        WitchcraftAPI.minecraft.summonZombie(caster, location);
    }
}
