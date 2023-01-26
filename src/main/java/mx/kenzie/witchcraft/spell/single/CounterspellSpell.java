package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.Session;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.FloatingBlock;
import mx.kenzie.witchcraft.entity.Projectile;
import mx.kenzie.witchcraft.entity.Summon;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CounterspellSpell extends CounteractSpell {
    public CounterspellSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final List<Entity> entities = new ArrayList<>(caster.getNearbyEntities(range, range / 2F, range));
        final Minecraft minecraft = Minecraft.getInstance();
        for (Entity entity : entities) {
            if (entity == caster) continue;
            if (minecraft.isAlly(entity, caster)) continue;
            if (entity instanceof Projectile projectile) projectile.remove();
            else if (entity instanceof FloatingBlock block) block.remove();
            else if (entity instanceof Player player) {
                final Session session = WitchcraftAPI.plugin.get(player);
                if (session != null) session.resetCast();
            } else if (entity instanceof Summon summon) summon.remove();
        }
        this.creator.createCircle(new Vector(0, -1, 0), 8, 260).draw(caster.getEyeLocation(), 10);
        final Location location = caster.getLocation();
        ParticleCreator.of(Particle.SOUL_FIRE_FLAME.builder().count(0).extra(0.5).offset(0, 1, 0))
            .createCircle(new Vector(0, 1, 0), 1, 35).draw(location);
        caster.getWorld().playSound(location, Sound.BLOCK_BEACON_DEACTIVATE, 1.0F, 0.5F);
    }
    
}
