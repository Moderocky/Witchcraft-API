package mx.kenzie.witchcraft.entity;

import mx.kenzie.witchcraft.world.WorldEvent;

public interface EventMarker extends CustomEntity {

    WorldEvent getEvent();

    void clearEvent();

}
