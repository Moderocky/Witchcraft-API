package mx.kenzie.witchcraft.spell.projectile;

import mx.kenzie.witchcraft.WitchcraftAPI;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

public interface IMagicProjectile extends IProjectile, Synchroniser {
    
    @Override
    default void onCollide(Entity entity) {
        this.explode(getLocation());
        if (!(entity instanceof final LivingEntity living)) return;
        if (this.hasCollided()) return;
        this.sync(() -> {
            WitchcraftAPI.minecraft.damageEntity(living, this.getSource(), this.getDamage(), EntityDamageEvent.DamageCause.MAGIC);
            return null;
        });
    }
    
    @Override
    default void onCollide(Block block) {
        explode(getLocation());
    }
    
    void explode(Location location);
    
    double getDamage();
    
}
