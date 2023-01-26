package mx.kenzie.witchcraft.spell.single;

import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.data.recipe.StorageGUI;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class SoulReconstructionSpell extends StandardSpell {
    public SoulReconstructionSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        final PaginatedGUI gui = new PaginatedGUI(WitchcraftAPI.plugin, InventoryType.HOPPER, "Soul Reconstruction");
        gui.setLayout(new String[] {
            "#A#B#"
        });
        gui.createButton('A', ItemArchetype.of("glowing_wither").mutate()
            .setName(Component.text("Dead Players")
                .decoration(TextDecoration.ITALIC, false))
            .setLore().create(), this::deadPlayers);
        gui.createButton('B', ItemArchetype.of("glowing_player").mutate()
            .setName(Component.text("Past Lives")
                .decoration(TextDecoration.ITALIC, false))
            .setLore().create(), this::pastLives);
        gui.finalise();
        gui.open(player);
    }
    
    protected void deadPlayers(Player player, InventoryClickEvent click) {
        // todo
    }
    
    protected void pastLives(Player player, InventoryClickEvent click) {
        final PlayerData data = PlayerData.getData(player);
        final PlayerData.Memory[] memories = data.history;
        final List<ItemStack> buttons = new ArrayList<>(memories.length);
        for (PlayerData.Memory memory : memories) buttons.add(memory.icon());
        final PaginatedGUI gui = new StorageGUI(WitchcraftAPI.plugin, 54, "Lifetime");
        final BiConsumer<Player, InventoryClickEvent> consumer = (clicker, event) -> {
            final int slot = event.getSlot();
            if (slot < 0) return;
            if (slot >= memories.length) return;
            final PlayerData.Memory memory = memories[slot];
            clicker.closeInventory();
            this.setMemory(clicker, memory);
        };
        assembleMenu(player, buttons, gui, consumer);
    }
    
    protected void setMemory(Player player, PlayerData.Memory memory) {
        final PlayerData data = PlayerData.getData(player);
        data.setMemory(memory);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 100, 4, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 4, false, false, false));
        ParticleCreator.of(Particle.FIREWORKS_SPARK.builder().count(0))
            .createSpiral(new Vector(0, 1, 0), 2.3, 0.8, 6, 20)
            .draw(player.getLocation(), 20);
        player.sendMessage(Component.text("Old memories have returned to you...", WitchcraftAPI.colors().lowlight()));
    }
    
}
