package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Map;

public class BoneCageSpell extends AbstractEntitySpell {
    protected transient final ParticleCreator creator = ParticleCreator.of(Material.BONE_BLOCK);
    protected transient final VectorShape circle = creator.createCircle(new Vector(0, 1, 0), 0.8, 26);
    
    public BoneCageSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (target == null) throw new RuntimeException();
        final Location centre = target.getLocation().add(0, 0.2, 0);
        this.target.setVelocity(new Vector(0, 0, 0));
        final int duration = (int) (5 + (amplitude * 2)) * 20;
        this.target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration, 128, false, false, false));
        this.target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 128, false, false, false));
        centre.getWorld().playSound(centre, Sound.BLOCK_BONE_BLOCK_BREAK, 1.2F, 1.0F);
        WitchcraftAPI.executor.submit(() -> {
            for (int i = 1; i < 8; i++) {
                centre.add(0, 0.2, 0);
                this.creator.draw(centre, circle);
                WitchcraftAPI.sleep(100);
            }
        });
    }
}
