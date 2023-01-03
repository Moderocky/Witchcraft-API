package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.data.Coven;
import mx.kenzie.witchcraft.data.Position;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class ReturnHomeSpell extends TeleportSpell {
    
    public ReturnHomeSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Coven coven = Coven.getCoven(caster);
        final Position home = coven.getHome();
        super.doTeleport(caster, home);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        final Coven coven = Coven.getCoven(caster);
        return coven != null && coven.getHome() != null;
    }
    
}
