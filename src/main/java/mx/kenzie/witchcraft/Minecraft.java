package mx.kenzie.witchcraft;

import com.destroystokyo.paper.profile.PlayerProfile;
import mx.kenzie.witchcraft.entity.*;
import mx.kenzie.witchcraft.spell.projectile.AbstractProjectile;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * These methods access the world, the custom entity registry or other minecraft data.
 * It's recommended to write data (e.g. 'do' stuff) only on the main thread.
 * Data here is fairly safe to read (e.g. 'check' stuff) off the main thread.
 */
public interface Minecraft {
    
    /**
     * If the target is an ally of the source.
     * Allies include:
     * - the source
     * - the source's summons
     * - the source's coven members
     * - the source's coven members' summons
     */
    boolean isAlly(Entity target, Entity source);
    
    /**
     * For checking who summons belong to.
     */
    boolean isPet(LivingEntity owner, LivingEntity pet);
    
    /**
     * The source damages the target safely.
     * Allies will not be damaged.
     */
    void damageEntitySafely(Entity target, Entity source, double amount, EntityDamageEvent.DamageCause reason);
    
    /**
     * If the target is an enemy of the source.
     * Enemies include:
     * - players in different covens
     * - summons of players in different covens
     * - monsters
     */
    boolean isEnemy(Entity target, Entity source);
    
    /**
     * The source damages the target.
     * This will damage allies, etc. indiscriminately.
     */
    void damageEntity(Entity target, Entity source, double amount, EntityDamageEvent.DamageCause reason);
    
    boolean isSameVehicle(Entity target, Entity passenger);
    
    /**
     * If entity responds to collisions, etc.
     */
    boolean isInteractible(Entity entity);
    
    CollisionTraceResult collisionCheck(AbstractProjectile projectile);
    
    CollisionTraceResult collisionCheck(Location location, Vector motion, @Nullable Entity source);
    
    boolean hasLineOfSight(Location bukkitStart, Location endStart);
    
    boolean hasLineOfSight(LivingEntity creature, Location end);
    
    boolean hasLineOfSight(Location start, Location end, Entity shape);
    
    Location getRelative(Location location, float horizontalAngle, float verticalAngle, double distance);
    
    /**
     * Summons an entity by its Minecraft ID.
     */
    Entity spawn(String id, Location location, boolean natural);
    
    /**
     * Returns the Summon handle of an entity.
     */
    Summon getAsSummon(Entity entity);
    
    /**
     * Summons a new ward cube.
     * This will rotate, but has no behaviour.
     */
    LivingEntity summonWardCube(LivingEntity owner, Location location);
    
    /**
     * Summons a 'summon' entity by its Minecraft ID.
     */
    Summon summon(LivingEntity owner, String id, Location location);
    
    /**
     * minecraft:marr_armour
     */
    LivingEntity summonMarrArmour(LivingEntity owner, Location location);
    
    /**
     * minecraft:arcana_summon
     */
    LivingEntity summonArcana(LivingEntity owner, Location location);
    
    /**
     * minecraft:armour_summon
     */
    LivingEntity summonArmour(LivingEntity owner, Location location);
    
    /**
     * minecraft:tang_portal
     */
    MalleablePortal tangPortal(Location location);
    
    /**
     * minecraft:nether_portal
     */
    MalleablePortal netherPortal(Location location);
    
    /**
     * minecraft:plant_guardian_summon
     */
    LivingEntity summonPlantGuardian(LivingEntity owner, Location location);
    
    /**
     * minecraft:skeleton_summon
     */
    LivingEntity summonSkeleton(LivingEntity owner, Location location);
    
    /**
     * minecraft:zombie_summon
     */
    LivingEntity summonZombie(LivingEntity owner, Location location);
    
    /**
     * minecraft:warp_warden_summon
     */
    LivingEntity summonWarpWarden(LivingEntity owner, Location location);
    
    /**
     * minecraft:wither_beast_summon
     */
    LivingEntity summonWitherBeast(LivingEntity owner, Location location);
    
    /**
     * minecraft:warhammer_totem
     */
    LivingEntity summonWarhammerTotem(LivingEntity owner, Location location);
    
    String getID(Entity entity);
    
    /**
     * Spawns a player 'NPC' entity. Natively unsafe to use.
     */
    NPC spawnNPC(Location loc, PlayerProfile profile, Consumer<Player> o);
    
    Entity getTargetEntity(Location location, double maxDist, double accuracy);
    
    @SuppressWarnings("unchecked")
    <Type extends Handle> Type getHandle(Entity entity);
    
    /**
     * Returns the number of nearby summons of the given ID.
     */
    int nearbySummons(LivingEntity owner, @Nullable String type);
}
