package mx.kenzie.witchcraft.data;

import mx.kenzie.witchcraft.ResourceManager;
import mx.kenzie.witchcraft.data.item.BukkitMaterial;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * A deity that a Warlock-class player can swear fealty to.
 * This has very little effect, except for informing the late-game item they get from Godsend.
 */
public enum WarlockDeity {
    NONE(null, null, true),
    ARCHANDER("rabaton", "horns_soldier_demon", false),
    VAR("doxa_staff", "horns_goat", false),
    TERRORACH("terrorach_staff", "horns_curly", false),
    NEFAERIAN("endragora", "horns_halo", true),
    RENOVAMEN("varrichor", "horns_unicorn", true);

    public final String weaponId, hornsId;
    public final boolean locked;

    WarlockDeity(String weaponId, String hornsId, boolean locked) {
        this.weaponId = weaponId;
        this.hornsId = hornsId;
        this.locked = locked;
    }

    public ItemArchetype getWeapon() {
        if (this == NONE) return BukkitMaterial.AIR;
        return ItemArchetype.of(weaponId);
    }

    public Component displayName() {
        return Component.text(ResourceManager.pascalCase(this.name().toLowerCase().replace('_', ' ')));
    }

    public void updateHorns(@Nullable LivingEntity entity) {
        if (entity == null) return;
        final EntityEquipment equipment = entity.getEquipment();
        if (equipment == null) return;
        final ItemStack helmet = equipment.getHelmet();
        final ItemArchetype archetype = ItemArchetype.of(helmet);
        equipment.setHelmet(this.getHorns().create());
        if (archetype.isEmpty() || archetype.isProtected()) return;
        if (entity instanceof Player player) archetype.giveSafely(player);
    }

    public ItemArchetype getHorns() {
        if (this == NONE) return BukkitMaterial.AIR;
        return ItemArchetype.of(hornsId).mutate().meta(meta -> meta.addEnchant(Enchantment.BINDING_CURSE, 1, true));
    }

}
