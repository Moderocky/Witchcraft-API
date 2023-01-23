package mx.kenzie.witchcraft;

import com.destroystokyo.paper.profile.PlayerProfile;
import mx.kenzie.witchcraft.data.Coven;
import mx.kenzie.witchcraft.data.WarlockDeity;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.entity.*;
import mx.kenzie.witchcraft.entity.handle.Grave;
import mx.kenzie.witchcraft.entity.handle.Handle;
import mx.kenzie.witchcraft.entity.handle.NPC;
import mx.kenzie.witchcraft.world.CollisionTraceResult;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * These methods access the world, the custom entity registry or other minecraft data.
 * It's recommended to write data (e.g. 'do' stuff) only on the main thread.
 * Data here is fairly safe to read (e.g. 'check' stuff) off the main thread.
 */
public interface Minecraft {
    
    static Minecraft getInstance() {
        return WitchcraftAPI.minecraft;
    }
    
    Projectile spawnProjectile(LivingEntity shooter, Location location, Vector velocity, float diameter, double range);
    
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
    
    boolean isValidToDamage(Entity target);
    
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
    
    CollisionTraceResult collisionCheck(Location location, Vector motion, @Nullable Entity source);
    
    boolean hasLineOfSight(Location bukkitStart, Location endStart);
    
    boolean hasLineOfSight(LivingEntity creature, Location end);
    
    boolean hasLineOfSight(Location start, Location end, Entity shape);
    
    Location getRelative(Location location, float horizontalAngle, float verticalAngle, double distance);
    
    /**
     * Summons an entity by its Minecraft ID.
     */
    Entity spawn(String id, Location location, boolean natural);
    
    Broomstick spawnBroomstick(ItemArchetype archetype, Location location);
    
    <Type extends CustomEntity> Type spawn(CustomEntityType<Type> type, Location location, boolean natural);
    
    /**
     * Returns the Summon handle of an entity.
     */
    Summon getAsSummon(Entity entity);
    
    /**
     * Summons a 'summon' entity by its Minecraft ID.
     */
    Summon summon(LivingEntity owner, String id, Location location);
    
    <Type extends Owned> Type summon(LivingEntity owner, CustomEntityType<Type> type, Location location);
    
    String getID(Entity entity);
    
    /**
     * Spawns a player 'NPC' entity. Natively unsafe to use.
     */
    NPC spawnNPC(Location loc, PlayerProfile profile, Consumer<Player> o);
    
    MirrorImage spawnMirrorImage(Location location, Player player);
    
    Demon spawnSoldierDemon(Location location, WarlockDeity deity);
    
    MirrorImage spawnMirrorImageNoAI(Location location, Player player);
    
    DeadSoldier spawnDeadSoldier(Location location, LivingEntity owner);
    
    Entity getTargetEntity(Location location, double maxDist, double accuracy);
    
    Set<Entity> getTargetEntities(Location location, double maxDist, double accuracy);
    
    <Type extends Handle> Type getHandle(Entity entity);
    
    /**
     * Returns the number of nearby summons of the given type.
     */
    int nearbySummons(LivingEntity owner, @Nullable CustomEntityType<? extends Owned> type);
    
    /**
     * Returns the number of nearby entities of the given type.
     */
    int nearbyEntities(LivingEntity centre, @Nullable CustomEntityType<?> type);
    
    List<Entity> getSummons(LivingEntity caster);
    
    List<Grave> nearbyGraves(Location location, double rangeX, double rangeY);
    
    LivingEntity spawnEnchantingTable(CustomEntityType<?> type, Location location, Coven coven);
    
    void updateEnchanter(Block block, Coven coven);
    
    <Type> Type ensureMain(Supplier<Type> runnable);
    
    void ensureMain(Runnable runnable);
    
    void breakBlock(Block block);
    
    void breakBlock(Block block, ItemStack tool);
    
    void setBlock(Block block, Material material);
    
    void setBlock(Block block, BlockData data);
    
    void merge(PersistentDataContainer from, PersistentDataContainer to);
    
    default double getMaxHealth(Entity entity) {
        if (!(entity instanceof LivingEntity living)) return 0;
        final AttributeInstance instance = living.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (instance == null) return 0;
        return instance.getValue();
    }
    
    void heal(Entity entity, double amount);
    
    void dispatchCommand(CommandSender sender, String command);
    
    void spawnFangs(LivingEntity caster, Location target, int range, float scale, double damage);
    
    void createDemonFangs(LivingEntity caster, double damage, double x, double z, double maxY, double y, double yaw, int warmup);
}
