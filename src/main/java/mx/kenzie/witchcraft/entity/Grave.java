package mx.kenzie.witchcraft.entity;

public interface Grave extends Handle {
    boolean isGrowing();
    
    boolean canGrow();
    
    boolean attemptGrow(org.bukkit.entity.Entity entity);
}
