package mx.kenzie.witchcraft.entity;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.Coven;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class CustomEntityType<EntityClass extends CustomEntity> {
    public static final CustomEntityType<? extends Summon> SKELETON_SUMMON = new CustomEntityType<>("SKELETON_SUMMON", true);
    public static final CustomEntityType<? extends Summon> ZOMBIE_SUMMON = new CustomEntityType<>("ZOMBIE_SUMMON", true);
    public static final CustomEntityType<? extends Summon> PLANT_GUARDIAN_SUMMON = new CustomEntityType<>("PLANT_GUARDIAN_SUMMON", true);
    public static final CustomEntityType<? extends Summon> GROTESQUE_SUMMON = new CustomEntityType<>("GROTESQUE_SUMMON", true);
    public static final CustomEntityType<? extends Summon> ARCANA_SUMMON = new CustomEntityType<>("ARCANA_SUMMON", true);
    public static final CustomEntityType<? extends Summon> ARMOUR_SUMMON = new CustomEntityType<>("ARMOUR_SUMMON", true);
    public static final CustomEntityType<? extends Summon> WARP_WARDEN_SUMMON = new CustomEntityType<>("WARP_WARDEN_SUMMON", true);
    public static final CustomEntityType<? extends Summon> WITHER_BEAST_SUMMON = new CustomEntityType<>("WITHER_BEAST_SUMMON", true);
    public static final CustomEntityType<? extends Summon> BAT_SUMMON = new CustomEntityType<>("BAT_SUMMON", true);
    public static final CustomEntityType<? extends Summon> PUMPKIN_SUMMON = new CustomEntityType<>("PUMPKIN_SUMMON", true);
    public static final CustomEntityType<? extends Summon> MARR_ARMOUR_SUMMON = new CustomEntityType<>("marr_armour", true);
    public static final CustomEntityType<? extends Hammer> WARHAMMER_TOTEM = new CustomEntityType<>("WARHAMMER_TOTEM", true);
    public static final CustomEntityType<? extends WardCube> WARD_CUBE_TOTEM = new CustomEntityType<>("ward_cube", true);
    public static final CustomEntityType<? extends Portal> NETHER_PORTAL = new CustomEntityType<>("NETHER_PORTAL");
    public static final CustomEntityType<? extends Portal> TANG_PORTAL = new CustomEntityType<>("TANG_PORTAL");
    public static final CustomEntityType<?> PIRATE_ENEMY = new CustomEntityType<>("PIRATE_ENEMY");
    public static final CustomEntityType<?> AXE_PIRATE_ENEMY = new CustomEntityType<>("AXE_PIRATE_ENEMY");
    public static final CustomEntityType<? extends Human> HUMAN = new CustomEntityType<>("HUMAN");
    public static final CustomEntityType<? extends Human> KNIGHT_HUMAN = new CustomEntityType<>("KNIGHT_HUMAN");
    public static final CustomEntityType<? extends Human> WIZARD_HUMAN = new CustomEntityType<>("WIZARD_HUMAN");
    public static final CustomEntityType<? extends Human> OGDEN_MARR_HUMAN = new CustomEntityType<>("OGDEN_MARR_HUMAN");
    public static final CustomEntityType<?> RAINBOW_BRIDGE = new CustomEntityType<>("RAINBOW_BRIDGE");
    public static final CustomEntityType<? extends MirrorImage> MIRROR_IMAGE = new CustomEntityType<>("MIRROR_IMAGE", true) {
        @Override
        public MirrorImage summon(LivingEntity owner, Location location) {
            return WitchcraftAPI.minecraft.spawnMirrorImage(location, ((Player) owner));
        }
    };
    public static final CustomEntityType<?> ENCHANTING_TABLE = new CustomEntityType<>("ENCHANTING_TABLE") {
        @Override
        public LivingEntity spawn(Location location, Coven coven) {
            return WitchcraftAPI.minecraft.spawnEnchantingTable(this, location, coven);
        }
    };
    public static final CustomEntityType<? extends Broomstick> BROOMSTICK = new CustomEntityType<>("BROOMSTICK");
    public static final CustomEntityType<?> GRAVESTONE = new CustomEntityType<>("GRAVESTONE");
    public static final CustomEntityType<? extends Summon> BLADE_OF_GLORY = new CustomEntityType<>("BLADE_OF_GLORY", true);
    public static final CustomEntityType<? extends DeadSoldier> DEAD_SOLDIER_SUMMON = new CustomEntityType<>("DEAD_SOLDIER_SUMMON", true) {
        @Override
        public DeadSoldier summon(LivingEntity owner, Location location) {
            return WitchcraftAPI.minecraft.spawnDeadSoldier(location, owner);
        }
    };
    public static final CustomEntityType<? extends Summon> SHADOW = new CustomEntityType<>("SHADOW", true);
    public static final CustomEntityType<?> PROJECTILE = new CustomEntityType<>("PROJECTILE");
    public static final CustomEntityType<? extends Summon> HELL_BAT_SUMMON = new CustomEntityType<>("HELL_BAT_SUMMON", true);
    public static final CustomEntityType<? extends Portal> DEMON_SPAWN_PORTAL = new CustomEntityType<>("DEMON_SPAWN_PORTAL");
    public static final CustomEntityType<? extends Demon> SOLDIER_DEMON = new CustomEntityType<>("SOLDIER_DEMON");
    public static final CustomEntityType<?> DEMON_FANGS = new CustomEntityType<>("DEMON_FANGS");
    
    public final String key;
    public final boolean summon;
    public Object internalType;
    
    
    CustomEntityType(String key) {
        this(key, false);
    }
    
    CustomEntityType(String key, boolean summon) {
        this.key = key.trim().toLowerCase().replace(' ', '_');
        this.summon = summon;
    }
    
    public String name() {
        return key;
    }
    
    public EntityClass spawn(Location location) {
        return WitchcraftAPI.minecraft.spawn(this, location, false);
    }
    
    @SuppressWarnings("unchecked")
    public EntityClass summon(LivingEntity owner, Location location) {
        return (EntityClass) WitchcraftAPI.minecraft.summon(owner, (CustomEntityType<? extends Owned>) this, location);
    }
    
    public LivingEntity spawn(Location location, Coven coven) {
        throw new RuntimeException("Not an enchanting table.");
    }
    
}
