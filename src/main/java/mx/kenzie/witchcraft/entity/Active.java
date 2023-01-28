package mx.kenzie.witchcraft.entity;

import mx.kenzie.witchcraft.entity.goal.Behaviour;

public interface Active extends CustomEntity {
    Behaviour<? extends Active> getBehaviour();

    void setBehaviour(Behaviour<?> behaviour);
}
