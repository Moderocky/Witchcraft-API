package mx.kenzie.witchcraft.data.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public record PlayerIcon(PlayerProfile profile) implements ItemArchetype {

    public PlayerIcon(Player player) {
        this(player.getPlayerProfile());
    }

    @Override
    public boolean isProtected() {
        return true;
    }

    @Override
    public Set<Tag> tags() {
        return new HashSet<>();
    }

    @Override
    public Component itemName() {
        return Component.text(this.name(), WitchcraftAPI.colors().highlight())
            .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public String name() {
        return Objects.requireNonNullElse(profile.getName(), "Player");
    }

    @Override
    public Rarity rarity() {
        return Rarity.RARE;
    }

    @Override
    public String id() {
        return Objects.requireNonNull(profile.getId()).toString();
    }

    @Override
    public List<Component> itemLore() {
        final List<Component> components = new ArrayList<>(1);
        components.add(PlayerData.getData(profile.getId()).style.displayName()
            .decoration(TextDecoration.ITALIC, false)
            .color(WitchcraftAPI.colors().lowlight()));
        return components;
    }

    @Override
    public String description() {
        return "Consumes resources when placed.";
    }

    @Override
    public ItemStack create() {
        final ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        final ItemMeta meta = stack.getItemMeta();
        if (meta instanceof SkullMeta skull) skull.setPlayerProfile(profile);
        meta.displayName(this.itemName());
        meta.lore(this.itemLore());
        meta.addItemFlags(ItemFlag.values());
        final PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(WitchcraftAPI.plugin.getKey("ephemeral"), PersistentDataType.BYTE, (byte) 1);
        stack.setItemMeta(meta);
        return stack;
    }
}
