package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.Protection;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class FortifySpell extends TunnelSpell {
    
    private transient final ParticleCreator creator = ParticleCreator.of(Particle.ASH),
        repel = ParticleCreator.of(Particle.WAX_OFF);
    private transient Block start;
    
    public FortifySpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        this.start = caster.getTargetBlockExact(5);
        if (start == null) return false;
        return this.isAllowed(start);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location point = start.getLocation();
        this.fortify(point, caster);
        this.creator.drawPoof(point, 3, 30);
    }
    
    public void fortify(Location location, LivingEntity caster) {
        final Block centre = location.getBlock();
        if (!this.isAllowed(centre)) return;
        final Protection protection = Protection.getInstance();
        if (!protection.canPlace(caster, location)) return;
        final Minecraft minecraft = Minecraft.getInstance();
        for (int x = -2; x < 3; x++)
            for (int y = -2; y < 3; y++)
                for (int z = -2; z < 3; z++) {
                    final Location point = location.clone().add(x, y, z);
                    final Block block = point.getBlock();
                    if (!this.isAllowed(block)) repel.drawPoof(point, 0.8, 10);
                    else if (!protection.canPlace(caster, point)) repel.drawPoof(point, 0.8, 10);
                    else minecraft.setBlock(block, this.getSolid());
                }
    }
    
    protected BlockData getSolid() {
        return Material.STONE.createBlockData();
    }
    
}
