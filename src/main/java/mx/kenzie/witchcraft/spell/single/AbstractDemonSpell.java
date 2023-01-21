package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.WarlockDeity;
import mx.kenzie.witchcraft.data.modifier.Modifier;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.entity.Grave;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class AbstractDemonSpell extends AbstractSummonSpell {
    public AbstractDemonSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return !Modifier.get(caster).isPresent(Modifier.Type.DEMON_COOLDOWN) && super.canCast(caster);
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Location location = target.getLocation().add(0.5, 0.2, 0.5);
        final Minecraft minecraft = Minecraft.getInstance();
        final Entity portal = CustomEntityType.DEMON_SPAWN_PORTAL.spawn(location);
        final Grave grave = minecraft.getHandle(portal);
        final WarlockDeity deity = caster instanceof Player player ? PlayerData.getData(player)
            .getDeity() : WarlockDeity.ARCHANDER;
        final DemonMaker maker = this.getDemon();
        final LivingEntity demon = maker.make(grave.getStart(), deity);
        grave.attemptGrow(demon);
        Modifier.get(caster).put("cooldown", Modifier.of(Modifier.Type.DEMON_COOLDOWN, 1, 100));
    }
    
    protected abstract DemonMaker getDemon();
    
    @FunctionalInterface
    protected interface DemonMaker {
        LivingEntity make(Location location, WarlockDeity deity);
    }
    
}
