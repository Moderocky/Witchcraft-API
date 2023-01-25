package mx.kenzie.witchcraft.entity;

import mx.kenzie.witchcraft.entity.goal.Behaviour;
import mx.kenzie.witchcraft.entity.goal.CharacterGoal;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;

public interface Character extends NPC, CustomEntity, LivingEntity {
    
    boolean isNamed();
    
    CharacterGoal getGoal();
    
    void setGoal(CharacterGoal goal);
    
    <EntityType extends Character> Behaviour<EntityType> getBehaviour();
    
    <EntityType extends Character> void setBehaviour(Behaviour<EntityType> behaviour);
    
}
