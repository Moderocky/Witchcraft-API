package mx.kenzie.witchcraft.spell.single;

import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.Coven;
import mx.kenzie.witchcraft.data.Position;
import mx.kenzie.witchcraft.data.recipe.StorageGUI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class StandUnitedSpell extends AbstractTeleportSpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(Material.IRON_BARS);
    protected transient final VectorShape circle = creator.createCircle(new Vector(0, 1, 0), 1, 28);

    public StandUnitedSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return super.canCast(caster) && Coven.getCoven(caster) != null;
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        final Coven coven = Coven.getCoven(player);
        assert coven != null;
        final Position[] positions = coven.getPositions().toArray(new Position[0]);
        final List<ItemStack> buttons = new ArrayList<>(positions.length);
        for (Position position : positions) buttons.add(position.create());
        final PaginatedGUI gui = new StorageGUI(WitchcraftAPI.plugin, 54, "Destination");
        final BiConsumer<Player, InventoryClickEvent> consumer = (clicker, event) -> {
            final int slot = event.getSlot();
            if (slot < 0) return;
            if (slot >= positions.length) return;
            final Position position = positions[slot];
            clicker.closeInventory();
            this.doTeleport(position, player);
        };
        assembleMenu(player, buttons, gui, consumer);
    }

    protected void doTeleport(Position position, Player caster) {
        final Position.Person person = (Position.Person) position;
        final Player target = person.player();
        final Location location = person.getLocation();
        final PotionEffect effect = new PotionEffect(PotionEffectType.ABSORPTION, 300, 3, false, false, false);
        target.addPotionEffect(effect);
        caster.addPotionEffect(effect);
        caster.teleportAsync(location);
        WitchcraftAPI.executor.submit(() -> {
            for (int i = 0; i < 5; i++) {
                location.add(0, 0.3, 0);
                this.circle.draw(location);
                WitchcraftAPI.sleep(100);
            }
        });
    }

}
