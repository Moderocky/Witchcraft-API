package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Vibration;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HealingRitualSpell extends StandardSpell {
    
    public HealingRitualSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Minecraft minecraft = Minecraft.getInstance();
        final Location centre = caster.getEyeLocation();
        final World world = caster.getWorld();
        final double amount = 4 + amplitude;
        final List<LivingEntity> entities = new ArrayList<>(centre.getNearbyLivingEntities(10, 5));
        entities.removeIf(target -> !minecraft.isAlly(caster, target));
        for (int i = 0; i < 5; i++) {
            WitchcraftAPI.scheduler.schedule(() -> {
                for (LivingEntity entity : entities) {
                    if (entity.isDead()) continue;
                    final Vibration vibration = new Vibration(new Vibration.Destination.EntityDestination(entity), 20);
                    world.spawnParticle(Particle.VIBRATION, centre, 1, vibration);
                }
            }, i * 380, TimeUnit.MILLISECONDS);
        }
        WitchcraftAPI.scheduler.schedule(() -> {
            for (LivingEntity entity : entities)
                entity.setHealth(Math.min(minecraft.getMaxHealth(entity), entity.getHealth() + amount));
        }, 2, TimeUnit.SECONDS);
    }
}
