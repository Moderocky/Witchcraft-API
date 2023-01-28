package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.RealmManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MinorGenesisSpell extends PocketPortalSpell {

    public MinorGenesisSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return super.canCast(caster) && !RealmManager.getInstance().worldExists(caster.getUniqueId());
    }

    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final RealmManager manager = RealmManager.getInstance();
        final CompletableFuture<World> future = manager.obtainRealm(caster.getUniqueId());
        final Location start = caster.getLocation();
        caster.getWorld().playSound(start, Sound.MUSIC_DRAGON, 1.0F, 1.0F);
        this.enterRealm(caster, future, start);
    }

}
