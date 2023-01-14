package mx.kenzie.witchcraft.entity;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.entity.LivingEntity;

public interface BroomstickEntity extends Handle {
    
    LivingEntity getBukkitLivingEntity();
    
    void setRainbow(boolean rainbow);
    
    void setParticleBuilder(ParticleBuilder builder);
    
}
