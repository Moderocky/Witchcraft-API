package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.entity.Facsimile;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Map;

public class FacsimileSpell extends StandardSpell {
    public FacsimileSpell(Map<String, Object> map) {
        super(map);
    }

    public static boolean tryUseFacsimile(Player player) {
        if (!hasFacsimile(player, true)) return false;
        final Facsimile image = getFacsimile(player);
        if (image == null) return false;
        final Location start = image.getLocation();
        image.remove();
        Minecraft.getInstance().heal(player, 30.0F);
        player.setHealth(WitchcraftAPI.minecraft.getMaxHealth(player));
        player.teleport(start);
        start.getWorld().playSound(start, Sound.ITEM_TOTEM_USE, 1.2F, 1.0F);
        drawRings(start);
        player.sendMessage(Component.text("You have been returned to your facsimile...", WitchcraftAPI.colors()
            .lowlight()));
        registerFacsimile(player, null);
        return true;
    }

    public static boolean hasFacsimile(Player player, boolean guarantee) {
        final PlayerData data = PlayerData.getData(player);
        return data.hasFacsimile(guarantee);
    }

    public static Facsimile getFacsimile(Player player) {
        final PlayerData data = PlayerData.getData(player);
        return data.getFacsimile();
    }

    private static void drawRings(Location start) {
        final ParticleCreator creator = ParticleCreator.of(Material.CRYING_OBSIDIAN);
        final VectorShape circle = creator.createCircle(new Vector(0, 1, 0), 0.8, 26);
        WitchcraftAPI.executor.submit(() -> {
            for (int i = 1; i < 8; i++) {
                start.add(0, 0.2, 0);
                creator.draw(start, circle);
                WitchcraftAPI.sleep(100);
            }
        });
    }

    public static void registerFacsimile(Player player, Facsimile image) {
        final PlayerData data = PlayerData.getData(player);
        data.setFacsimile(image);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player;
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        Component component = Component.text("You have created a facsimile..." +
            "\nInstead of dying, your essence will transfer to this body.", WitchcraftAPI.colors()
            .lowlight());
        if (FacsimileSpell.hasFacsimile(player, true)) {
            final Facsimile image = getFacsimile(player);
            if (image != null) image.remove();
            component = Component.text("You have replaced your facsimile.", WitchcraftAPI.colors()
                .lowlight());
        }
        final Location start = player.getLocation();
        drawRings(start);
        final Facsimile image = CustomEntityType.FACSIMILE.summon(player, start);
        registerFacsimile(player, image);
        player.sendMessage(component);
    }

}
