package mx.kenzie.witchcraft.entity;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.entity.goal.Behaviour;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

public interface FloatingBlock extends CustomEntity, ArmorStand, NoSpawnGrave, Active {

    void display(Material material);

    void display(Block block);

    ParticleBuilder getBuilder();

    Behaviour<? extends FloatingBlock> getBehaviour();

    void setDamage(double damage);

    enum Type {
        STATIC, ROTATING, BURNING_SPHERE, DEMON_BALL
    }

}
