package mx.kenzie.witchcraft.entity;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

public interface Human extends CustomEntity, LivingEntity, Mob {

    @NotNull
    PlayerProfile getPlayerProfile();

    void setPlayerProfile(@NotNull PlayerProfile profile);

}
