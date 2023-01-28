package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.WarlockDeity;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class GodsendSpell extends StandardSpell {
    protected transient final ParticleCreator failure = ParticleCreator.of(Particle.FIREWORKS_SPARK),
        success = ParticleCreator.of(Particle.TOTEM);

    public GodsendSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return caster instanceof Player player && PlayerData.getData(player).isSworn();
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        final Location location = caster.getEyeLocation();
        final PlayerData data = PlayerData.getData(player);
        final WarlockDeity deity = data.getDeity();
        if (deity == null) {
            this.failure.drawPoof(location, 1, 8);
            caster.sendMessage(Component.text("You are not sworn to a deity...", WitchcraftAPI.colors().lowlight()));
            return;
        }
        final ItemArchetype archetype = deity.getWeapon();
        assert !archetype.isEmpty();
        if (WitchcraftAPI.resources.contains(player.getInventory(), archetype)) {
            this.failure.drawPoof(location, 1, 8);
            caster.sendMessage(Component.text("You already have your master's favour...", WitchcraftAPI.colors()
                .lowlight()));
            return;
        }
        this.success.drawPoof(location, 1, 10);
        location.getWorld().playSound(location, Sound.MUSIC_DRAGON, 0.6F, 1);
        archetype.giveSafely(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 3, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 50, 0, false, false, false));
        caster.sendMessage(Component.text("A gift for you, my chosen one...", WitchcraftAPI.colors().lowlight()));
    }
}
