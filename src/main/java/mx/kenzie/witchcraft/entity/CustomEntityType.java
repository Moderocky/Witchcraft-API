package mx.kenzie.witchcraft.entity;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.Coven;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public enum CustomEntityType {
    SKELETON_SUMMON(true),
    ZOMBIE_SUMMON(true),
    PLANT_GUARDIAN_SUMMON(true),
    GROTESQUE_SUMMON(true),
    ARCANA_SUMMON(true),
    ARMOUR_SUMMON(true),
    WARP_WARDEN_SUMMON(true),
    WITHER_BEAST_SUMMON(true),
    BAT_SUMMON(true),
    PUMPKIN_SUMMON(true),
    MARR_ARMOUR_SUMMON("marr_armour", true),
    WARHAMMER_TOTEM(true),
    WARD_CUBE_TOTEM("ward_cube", true),
    NETHER_PORTAL,
    TANG_PORTAL,
    PIRATE_ENEMY,
    AXE_PIRATE_ENEMY,
    HUMAN,
    KNIGHT_HUMAN,
    WIZARD_HUMAN,
    OGDEN_MARR_HUMAN,
    RAINBOW_BRIDGE,
    MIRROR_IMAGE(true) {
        @Override
        public Entity summon(LivingEntity owner, Location location) {
            return WitchcraftAPI.minecraft.spawnMirrorImage(location, ((Player) owner));
        }
    },
    ENCHANTING_TABLE {
        @Override
        public LivingEntity spawn(Location location, Coven coven) {
            return WitchcraftAPI.minecraft.spawnEnchantingTable(this, location, coven);
        }
    },
    BROOMSTICK,
    GRAVESTONE,
    BLADE_OF_GLORY(true),
    DEAD_SOLDIER_SUMMON(true) {
        @Override
        public LivingEntity summon(LivingEntity owner, Location location) {
            return WitchcraftAPI.minecraft.spawnDeadSoldier(location, owner);
        }
    },
    SHADOW(true), PROJECTILE;
    
    public final String key;
    public final boolean summon;
    public Object handle;
    
    
    CustomEntityType(String key) {
        this(key, false);
    }
    
    CustomEntityType(String key, boolean summon) {
        this.key = key;
        this.summon = summon;
    }
    
    CustomEntityType() {
        this(false);
    }
    
    CustomEntityType(boolean summon) {
        this.key = this.name().toLowerCase();
        this.summon = summon;
    }
    
    public Entity spawn(Location location) {
        return WitchcraftAPI.minecraft.spawn(this, location, false);
    }
    
    public Entity summon(LivingEntity owner, Location location) {
        return WitchcraftAPI.minecraft.summon(owner, this, location).getBukkitEntity();
    }
    
    public LivingEntity spawn(Location location, Coven coven) {
        throw new RuntimeException("Not an enchanting table.");
    }
}