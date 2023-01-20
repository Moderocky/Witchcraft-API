package mx.kenzie.witchcraft.spell.single;

import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.item.PlayerIcon;
import mx.kenzie.witchcraft.data.recipe.StorageGUI;
import mx.kenzie.witchcraft.data.world.WorldData;
import mx.kenzie.witchcraft.spell.StandardSpell;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;

public class PlanarExpulsionSpell extends StandardSpell {
    public PlanarExpulsionSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final World world = caster.getWorld();
        final WorldData data = WorldData.getData(world);
        if (!(caster instanceof Player player)) return;
        if (data.isMainWorld() || data.safe) {
            caster.sendMessage(Component.text("You cannot expel somebody from the Mortal Realm.", WitchcraftAPI.colors()
                .lowlight()));
            return;
        }
        final Set<Player> set = new HashSet<>(Bukkit.getOnlinePlayers());
        set.remove(caster);
        final Player[] players = set.toArray(new Player[0]);
        final List<ItemStack> buttons = new ArrayList<>(players.length);
        for (Player online : players) buttons.add(new PlayerIcon(online).create());
        final PaginatedGUI gui = new StorageGUI(WitchcraftAPI.plugin, 54, "Target");
        final BiConsumer<Player, InventoryClickEvent> consumer = (clicker, event) -> {
            final int slot = event.getSlot();
            if (slot < 0) return;
            if (slot >= players.length) return;
            final Player position = players[slot];
            clicker.closeInventory();
            this.doExpel(position, world);
            world.playSound(clicker.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.6F, 1.0F);
        };
        assembleMenu(player, buttons, gui, consumer);
    }
    
    protected void doExpel(Player player, World world) {
        if (player.getWorld() != world) return;
        player.sendMessage(Component.text("You were expelled from this realm...", WitchcraftAPI.colors().lowlight()));
        WitchcraftAPI.plugin.getSpawn().teleport(player);
    }
}
