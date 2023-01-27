package mx.kenzie.witchcraft.spell;

import com.moderocky.mask.gui.PaginatedGUI;
import mx.kenzie.argo.meta.Name;
import mx.kenzie.witchcraft.SpellManager;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.MagicClass;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.SpellType;
import mx.kenzie.witchcraft.data.modifier.Modifier;
import mx.kenzie.witchcraft.data.recipe.Ingredient;
import mx.kenzie.witchcraft.event.SpellCastEvent;
import mx.kenzie.witchcraft.event.SpellPreCastEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiConsumer;

public abstract class StandardSpell implements Spell {
    public static final Class<? extends StandardSpell> NO_EFFECT = NoEffectSpell.class;
    public static final TextColor COLOR = TextColor.color(3, 219, 252);
    public final @Name("class") MagicClass style;
    public final String id, name, description;
    public final SpellType type;
    public final Ingredient[] materials;
    public final String circumstances, realm;
    public final int points, energy;
    public final Pattern pattern;
    public String patternChar;
    
    @SuppressWarnings("unchecked")
    public StandardSpell(Map<String, Object> map) {
        this.id = map.get("id").toString().trim();
        this.name = map.get("name").toString().trim();
        this.description = map.get("description").toString().trim();
        this.style = MagicClass.valueOf(map.get("class").toString().trim().replace(' ', '_').toUpperCase());
        this.circumstances = (String) map.get("circumstances");
        this.realm = (String) map.get("realm");
        final List<Object> list = (List<Object>) map.get("materials");
        if (!WitchcraftAPI.isTest && list != null) {
            final Object[] objects = list.toArray();
            this.materials = new Ingredient[objects.length];
            for (int i = 0; i < objects.length; i++) materials[i] = Ingredient.fromJson(objects[i]);
        } else materials = new Ingredient[0];
        this.type = SpellType.get(map.get("type").toString());
        this.points = (int) map.get("points");
        this.energy = (int) map.get("energy");
        this.pattern = WitchcraftAPI.spells.generate(points, new Random(id.hashCode()));
    }
    
    public static void assembleMenu(Player player, List<ItemStack> buttons, PaginatedGUI gui, BiConsumer<Player, InventoryClickEvent> consumer) {
        SpellManager.makeMenu(gui);
        gui.setEntryConsumer(consumer);
        gui.setEntryChar('#');
        gui.setEntries(buttons);
        gui.finalise();
        gui.open(player);
    }
    
    @Override
    public MagicClass getStyle() {
        return style;
    }
    
    @Override
    public SpellType getType() {
        return type;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getPatternPicture() {
        return patternChar;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public Ingredient[] getMaterials() {
        return materials;
    }
    
    @Override
    public String getCircumstances() {
        return circumstances;
    }
    
    @Override
    public String getRealm() {
        return realm;
    }
    
    @Override
    public int getPoints() {
        return points;
    }
    
    @Override
    public int getEnergy() {
        return energy;
    }
    
    @Override
    public Pattern getPattern() {
        return pattern;
    }
    
    @Override
    public void cast(LivingEntity caster, int range, float scale, double amplitude) {
        if (!this.canCast(caster)) return; // update any requirements
        final SpellCastEvent event = new SpellCastEvent(caster, this);
        Bukkit.getPluginManager().callEvent(event);
        this.run(caster, range, scale, amplitude);
    }
    
    @Override
    public SpellResult checkCast(LivingEntity caster, int range, float scale, double amplitude) {
        if (caster instanceof Player player) check:{
            if (!this.canCast(caster)) return SpellResult.MISSING_CIRCUMSTANCE;
            if (player.getGameMode() == GameMode.CREATIVE) break check;
            final int energy = WitchcraftAPI.spells.getEnergy(caster, caster.getEquipment());
            if (this.getEnergy() > energy) return SpellResult.NO_ENERGY;
            for (Ingredient material : this.getMaterials())
                if (!material.canComplete(player))
                    return SpellResult.NO_INGREDIENT;
            final SpellPreCastEvent event = new SpellPreCastEvent(caster, this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return SpellResult.CANCELLED;
            this.takeIngredients(caster);
            final int taken = this.takeEnergy(caster);
            if (!PlayerData.getData(player).isSorcerer()) {
                final int restore = (int) Math.min(taken, 3 + Modifier.get(caster)
                    .get(Modifier.Type.ENERGY_REGENERATION));
                SpellManager.getInstance().regenerateEnergy(player, restore * 2);
            }
        }
        this.cast(caster, range, scale, amplitude);
        return SpellResult.SUCCESS;
    }
    
    /**
     * Whether the spell is literally capable of being cast.
     * This is verified in every cast before the `run` portion.
     */
    public abstract boolean canCast(LivingEntity caster);
    
    public void takeIngredients(LivingEntity caster) {
        if (!(caster instanceof Player player)) return;
        for (Ingredient material : this.getMaterials()) material.remove(player.getInventory());
    }
    
    public int takeEnergy(LivingEntity caster) {
        final int bonus = WitchcraftAPI.spells.getBonusEnergy(caster, caster.getEquipment());
        if (!(caster instanceof Player player)) return 0;
        final int energy = this.getEnergy();
        final int taken = ((energy - bonus) * 2);
        player.setFoodLevel(player.getFoodLevel() - ((energy - bonus) * 2));
        return taken;
    }
    
    protected abstract void run(LivingEntity caster, int range, float scale, double amplitude);
    
}

class NoEffectSpell extends StandardSpell {
    
    public NoEffectSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        caster.sendMessage(Component.textOfChildren(
            Component.text("You cast "),
            Component.text(this.getName()).color(COLOR),
            Component.text("!")).color(NamedTextColor.WHITE)
        );
        caster.sendMessage(Component.text("It wasn't very effective...").color(NamedTextColor.WHITE));
    }
}
