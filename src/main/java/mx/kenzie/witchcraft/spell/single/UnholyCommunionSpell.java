package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CustomEntity;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Map;

public class UnholyCommunionSpell extends StandardSpell {
    
    protected transient final ParticleCreator creator = ParticleCreator.of(Particle.SPELL_WITCH);
    protected transient final VectorShape circle = creator.createCircle(new Vector(0, 1, 0), 1, 20);
    
    public UnholyCommunionSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        Block block = caster.getLocation().add(0, 2.5, 0).getBlock();
        int checks = 0;
        while (++checks < 12 && !block.getRelative(BlockFace.DOWN).isSolid()) block = block.getRelative(BlockFace.DOWN);
        final Location start = block.getLocation().add(0.5, 0.2, 0.5);
        final CustomEntity entity = CustomEntityType.SEAT.spawn(start);
        entity.addPassenger(caster);
        start.getWorld().playSound(start, Sound.BLOCK_BEACON_AMBIENT, 1.0F, 1.0F);
        WitchcraftAPI.executor.submit(() -> {
            final Minecraft minecraft = Minecraft.getInstance();
            for (int i = 0; i < 100; i++) {
                if (entity.isDead()) break;
                if (caster.isDead()) break;
                if (!caster.isInsideVehicle()) break;
                if (caster.getVehicle() != entity) break;
                this.circle.draw(start.clone().add(0, (1 + Math.sin(i / 4.0F)) * 0.9F, 0));
                minecraft.ensureMain(() -> {
                    minecraft.heal(caster, 0.2);
                    if (caster instanceof Player player) player.setFoodLevel(player.getFoodLevel() + 1);
                });
                WitchcraftAPI.sleep(80);
            }
            if (!entity.isDead()) minecraft.ensureMain(entity::remove);
        });
    }
    
}
