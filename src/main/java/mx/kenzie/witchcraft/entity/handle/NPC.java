package mx.kenzie.witchcraft.entity.handle;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface NPC {

    Player getBukkitEntity();

    void setOnDamage(BiConsumer<NPC, EntityDamageEvent> onDamage);

    void setOnDeath(BiConsumer<NPC, PlayerDeathEvent> onDeath);

    void setOnTick(Consumer<NPC> onTick);

    void discard();

    float getYHeadRot();

    void setYHeadRot(float v);
}
