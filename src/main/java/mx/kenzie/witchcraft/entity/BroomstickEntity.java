package mx.kenzie.witchcraft.entity;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.entity.LivingEntity;

public interface BroomstickEntity extends Handle {
    
    LivingEntity getBukkitLivingEntity();
    
    float getMaxVerticalSpeed();
    
    void setMaxVerticalSpeed(float maxVerticalSpeed);
    
    float getMinSpeed();
    
    void setMinSpeed(float minSpeed);
    
    float getMaxSpeed();
    
    void setMaxSpeed(float maxSpeed);
    
    float getMaxStrafe();
    
    void setMaxStrafe(float maxStrafe);
    
    ParticleCreator getCreator();
    
    void setRainbow(boolean rainbow);
    
    void setParticleBuilder(ParticleBuilder builder);
    
}
