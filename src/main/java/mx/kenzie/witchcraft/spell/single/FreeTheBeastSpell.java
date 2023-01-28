package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.Map;

public class FreeTheBeastSpell extends AbstractSummonSpell {

    transient final Color color = new Color(20, 18, 21);
    transient final ParticleBuilder builder = new ParticleBuilder(Particle.SPELL_MOB)
        .offset(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0)
        .count(0)
        .force(true);

    public FreeTheBeastSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return WitchcraftAPI.minecraft.nearbySummons(caster, CustomEntityType.WITHER_BEAST_SUMMON) < 1 && super.canCast(caster);
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0.5, 0.2, 0.5);
        WitchcraftAPI.client.particles(builder).playSpiral(
            location.setDirection(new Vector(0, 1, 0)),
            1.1, 3, 20, 3
        );
        CustomEntityType.WITHER_BEAST_SUMMON.summon(caster, location);
    }
}
