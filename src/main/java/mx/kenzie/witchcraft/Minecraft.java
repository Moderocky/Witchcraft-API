package mx.kenzie.witchcraft;

import com.destroystokyo.paper.profile.PlayerProfile;
import mx.kenzie.witchcraft.entity.*;
import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface Minecraft {
    boolean isAlly(org.bukkit.entity.Entity target, org.bukkit.entity.Entity source);
    
    boolean isPet(org.bukkit.entity.LivingEntity owner, org.bukkit.entity.LivingEntity pet);
    
    void damageEntitySafely(org.bukkit.entity.Entity target, org.bukkit.entity.Entity source, double amount, EntityDamageEvent.DamageCause reason);
    
    boolean isEnemy(org.bukkit.entity.Entity target, org.bukkit.entity.Entity source);
    
    void damageEntity(org.bukkit.entity.Entity target, org.bukkit.entity.Entity damager, double amount, EntityDamageEvent.DamageCause reason);
    
    boolean isSameVehicle(org.bukkit.entity.Entity target, org.bukkit.entity.Entity passenger);
    
    boolean isInteractible(org.bukkit.entity.Entity entity);
    
    CollisionTraceResult collisionCheck(AbstractProjectile projectile);
    
    CollisionTraceResult collisionCheck(Location location, Vector motion, @Nullable org.bukkit.entity.Entity source);
    
    boolean hasLineOfSight(Location bukkitStart, Location endStart);
    
    boolean hasLineOfSight(org.bukkit.entity.LivingEntity creature, Location end);
    
    boolean hasLineOfSight(Location start, Location end, org.bukkit.entity.Entity shape);
    
    Location getRelative(Location location, float horizontalAngle, float verticalAngle, double distance);
    
    org.bukkit.entity.Entity spawn(String id, Location location, boolean natural);
    
    Summon getAsSummon(org.bukkit.entity.Entity entity);
    
    org.bukkit.entity.LivingEntity summonWardCube(org.bukkit.entity.LivingEntity owner, Location location);
    
    Summon summon(org.bukkit.entity.LivingEntity owner, String id, Location location);
    
    org.bukkit.entity.LivingEntity summonMarrArmour(org.bukkit.entity.LivingEntity owner, Location location);
    
    org.bukkit.entity.LivingEntity summonArcana(org.bukkit.entity.LivingEntity owner, Location location);
    
    org.bukkit.entity.LivingEntity summonArmour(org.bukkit.entity.LivingEntity owner, Location location);
    
    MalleablePortal netherPortal(Location location);
    
    org.bukkit.entity.LivingEntity summonPlantGuardian(org.bukkit.entity.LivingEntity owner, Location location);
    
    org.bukkit.entity.LivingEntity summonSkeleton(org.bukkit.entity.LivingEntity owner, Location location);
    
    org.bukkit.entity.LivingEntity summonZombie(org.bukkit.entity.LivingEntity owner, Location location);
    
    org.bukkit.entity.LivingEntity summonWarpWarden(org.bukkit.entity.LivingEntity owner, Location location);
    
    org.bukkit.entity.LivingEntity summonWitherBeast(org.bukkit.entity.LivingEntity owner, Location location);
    
    org.bukkit.entity.LivingEntity summonWarhammerTotem(org.bukkit.entity.LivingEntity owner, Location location);
    
    String getID(org.bukkit.entity.Entity entity);
    
    NPC spawnNPC(Location loc, PlayerProfile profile, Consumer<Player> o);
    
    org.bukkit.entity.Entity getTargetEntity(Location location, double maxDist, double accuracy);
    
    @SuppressWarnings("unchecked")
    <Type extends Handle> Type getHandle(org.bukkit.entity.Entity entity);
    
    int nearbySummons(org.bukkit.entity.LivingEntity owner, @Nullable String type);
}
