package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

public class CommandWardenSpell extends AbstractSummonSpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(Particle.SOUL_FIRE_FLAME.builder().count(0));
    
    public CommandWardenSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return WitchcraftAPI.minecraft.nearbySummons(caster, CustomEntityType.WARP_WARDEN_SUMMON) < 1 && super.canCast(caster);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0.5, 0.2, 0.5);
        final Entity entity = CustomEntityType.WARP_WARDEN_SUMMON.summon(caster, location);
        this.creator.createSpiral(
            location.setDirection(new Vector(0, 1, 0)),
            1.2, 3, 25, 3
        ).draw(30);
        Bukkit.getScheduler()
            .scheduleSyncDelayedTask(WitchcraftAPI.plugin, entity::remove, (40 + (long) (amplitude * 7)) * 20L);
    }
}
