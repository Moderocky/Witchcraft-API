package mx.kenzie.witchcraft.spell.single;

import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.WarlockDeity;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class RededicationSpell extends StandardSpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(Particle.TOTEM);

    public RededicationSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player;
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        final PaginatedGUI gui = new PaginatedGUI(WitchcraftAPI.plugin, InventoryType.CHEST, "Rededication");
        gui.setLayout(new String[]{
            "##A#X#B##",
            "#C###D#E#",
            "#F#######"
        });
        gui.setEntries();
        gui.createButton('X', WarlockDeity.NONE.getIcon(player).create(), this::selectDeity);
        gui.createButton('A', WarlockDeity.VAR.getIcon(player).create(), this::selectDeity);
        gui.createButton('B', WarlockDeity.NEFAERIAN.getIcon(player).create(), this::selectDeity);
        gui.createButton('C', WarlockDeity.ARCHANDER.getIcon(player).create(), this::selectDeity);
        gui.createButton('F', WarlockDeity.TERRORACH.getIcon(player).create(), this::selectDeity);
        gui.createButton('D', WarlockDeity.MALTHERIDOM.getIcon(player).create(), this::selectDeity);
        gui.createButton('E', WarlockDeity.RENOVAMEN.getIcon(player).create(), this::selectDeity);
        gui.finalise();
        gui.open(player);
    }

    protected void selectDeity(Player player, InventoryClickEvent click) {
        final NamespacedKey key = WitchcraftAPI.plugin.getKey("deity");
        final ItemStack stack = click.getCurrentItem();
        if (stack == null || stack.getType() == Material.AIR) return;
        final PersistentDataContainer container = stack.getItemMeta().getPersistentDataContainer();
        if (!container.has(key)) return;
        final String name = container.getOrDefault(key, PersistentDataType.STRING, "NONE");
        final WarlockDeity deity = WarlockDeity.valueOf(name);
        final PlayerData data = PlayerData.getData(player);
        final WarlockDeity old = data.getDeity();
        player.getInventory().remove(old.getWeapon().create());
        data.memory.deity = deity;
        deity.updateHorns(player);
        player.sendMessage(Component.text("You have dedicated yourself to a new master...", WitchcraftAPI.colors().lowlight()));
    }

}
