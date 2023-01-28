package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.Position;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class RememberSpell extends StandardSpell {
    protected transient final ParticleCreator creator;

    public RememberSpell(Map<String, Object> map) {
        super(map);
        if (!WitchcraftAPI.isTest)
            creator = WitchcraftAPI.client.particles(new ParticleBuilder(Particle.FIREWORKS_SPARK)
                .count(0).force(true));
        else creator = null;
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        if (!(caster instanceof Player player)) return false;
        final PlayerData data = PlayerData.getData(player);
        for (Position.Static location : data.memory.locations) {
            if (location.world != caster.getWorld().getUID()) continue;
            if (location.getLocation().distanceSquared(caster.getLocation()) < 100) return false;
        }
        return true;
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        final PlayerData data = PlayerData.getData(player);
        data.learnLocation(caster.getLocation().getBlock().getLocation().add(0.5, 0.1, 0.5));
        this.creator.createPoof(0.9, 16).draw(caster.getEyeLocation());
    }
}
