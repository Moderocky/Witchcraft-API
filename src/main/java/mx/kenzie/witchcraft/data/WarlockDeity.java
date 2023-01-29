package mx.kenzie.witchcraft.data;

import mx.kenzie.witchcraft.ResourceManager;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.item.BukkitMaterial;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

/**
 * A deity that a Warlock-class player can swear fealty to.
 * This has very little effect, except for informing the late-game item they get from Godsend.
 */
public enum WarlockDeity {
    NONE(null, null, "You dedicate yourself to no master.", false, false, MagicClass.PURE),
    ARCHANDER("rabaton", "horns_soldier_demon", "Tyrant of the Demon Realm and Lord of Flesh.", false, true, MagicClass.FALLEN_DEITY),
    VAR("doxa_staff", "horns_goat", "First Lord of Darkness", false, true, MagicClass.FALLEN_DEITY),
    TERRORACH("terrorach_staff", "horns_curly", "Master of the lower temple, Lord of the Boiling Sea.", false, true, MagicClass.ARCH_DEMON),
    NEFAERIAN("endragora", "horns_halo", "The firstborn goddess, Lord of Light", true, false, MagicClass.DIVINE),
    RENOVAMEN("varrichor", "horns_unicorn", "Master of the Arcana, Lord of High Water", true, false, MagicClass.DIVINE),
    MALTHERIDOM("petrichor", "horns_antlers", "Master of the natural world, Lord of Earth", true, false, MagicClass.MINOR_GOD);

    public final String weaponId, hornsId, text;
    public final boolean locked, evil;
    public final MagicClass magicClass;

    WarlockDeity(String weaponId, String hornsId, String text, boolean locked, boolean evil, MagicClass magicClass) {
        this.weaponId = weaponId;
        this.hornsId = hornsId;
        this.text = text;
        this.locked = locked;
        this.evil = evil;
        this.magicClass = magicClass;
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

    public ItemArchetype getIcon(@Nullable Player player) {
        final ItemArchetype archetype;
        if (this == NONE) archetype = new BukkitMaterial(Material.STRUCTURE_VOID);
        else archetype = this.getHorns();
        return archetype.mutate()
            .setName(Component.text(ResourceManager.pascalCase(this.name()), magicClass.colour()).decoration(TextDecoration.ITALIC, false))
            .setLore(ItemArchetype.wrapText(text))
            .addLore(
                Component.empty(),
                magicClass.displayName().decoration(TextDecoration.ITALIC, false),
                (evil
                    ? Component.text("Evil", NamedTextColor.RED)
                    : Component.text("Good", NamedTextColor.GREEN)).decoration(TextDecoration.ITALIC, false),
                (this.canUse(player)
                    ? Component.text("Click to Select", WitchcraftAPI.colors().pop())
                    : Component.text("Locked", NamedTextColor.RED)).decoration(TextDecoration.ITALIC, false)
            )
            .meta(meta -> meta.getPersistentDataContainer().set(WitchcraftAPI.plugin.getKey("deity"), PersistentDataType.STRING, this.name()));
    }

    public boolean canUse(Player player) {
        if (locked) return false;
        if (player == null) return true;
        if (PlayerData.getData(player).style == MagicClass.WARLOCK) return evil;
        else return !evil;
    }

}
