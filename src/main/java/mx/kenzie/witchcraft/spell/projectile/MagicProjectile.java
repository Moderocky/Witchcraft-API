package mx.kenzie.witchcraft.spell.projectile;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public abstract class MagicProjectile extends AbstractProjectile implements IMagicProjectile {
    
    protected double damage;
    
    public MagicProjectile(@Nullable Entity source, Location location, double damage) {
        super(source, location);
        this.damage = damage;
    }
    
    @Override
    public double getDamage() {
        return damage;
    }
}
