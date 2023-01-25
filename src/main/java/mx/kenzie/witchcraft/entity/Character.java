package mx.kenzie.witchcraft.entity;

import mx.kenzie.witchcraft.entity.goal.Behaviour;
import mx.kenzie.witchcraft.entity.goal.CharacterGoal;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;

public interface Character extends NPC, CustomEntity, LivingEntity, Active {
    
    boolean isNamed();
    
    CharacterGoal getGoal();
    
    void setGoal(CharacterGoal goal);
    
    Behaviour<? extends Character> getBehaviour();
    
}
