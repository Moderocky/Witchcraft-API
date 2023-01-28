package mx.kenzie.witchcraft.entity;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public interface Portal extends CustomEntity {

    Vector getOrientation();

    void setOrientation(Vector vector);

    void setCollideConsumer(Consumer<Entity> collideConsumer);

}
