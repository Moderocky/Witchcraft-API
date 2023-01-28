package mx.kenzie.witchcraft.entity;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Projectile extends CustomEntity, AbstractArrow {
    double getDamage();

    void setDamage(double damage);

    void onCollide(Runnable collide);

    boolean shouldCollide(Entity entity);

    boolean launch();

    void setVelocity(Vector velocity);

    boolean hasLaunched();

    boolean hasCollided();

    Location getPrevious();

    Location getPotentialNext();

    org.bukkit.entity.LivingEntity getSource();

    void setCanCollideWith(Function<org.bukkit.entity.Entity, Boolean> function);

    void onCollideWithBlock(BiFunction<Block, BlockFace, Boolean> function);

    void onCollideWithEntity(Function<org.bukkit.entity.Entity, Boolean> function);

    void onTick(Runnable tick);
}
