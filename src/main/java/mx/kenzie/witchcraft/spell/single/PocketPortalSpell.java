package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.Protection;
import mx.kenzie.witchcraft.RealmManager;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.spell.effect.LocatedShape;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PocketPortalSpell extends AbstractTeleportSpell {
    
    protected transient final ParticleCreator creator = ParticleCreator.of(Particle.GLOW_SQUID_INK);
    
    public PocketPortalSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        return super.canCast(caster) && RealmManager.getInstance().worldExists(caster.getUniqueId());
    }
    
    @Override
    protected void run(LivingEntity caster, int range, float scale, double amplitude) {
        final RealmManager manager = RealmManager.getInstance();
        final CompletableFuture<World> future;
        if (!manager.isWorldLoaded(caster.getUniqueId())) future = manager.obtainRealm(caster.getUniqueId());
        else future = CompletableFuture.completedFuture(manager.getWorld(caster.getUniqueId()));
        final Location start = caster.getLocation();
        this.enterRealm(caster, future, start);
    }
    
    protected void enterRealm(LivingEntity caster, CompletableFuture<World> future, Location start) {
        caster.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 90, 1, false, false, false));
        caster.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 90, 1, false, false, false));
        caster.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 90, 1, false, false, false));
        WitchcraftAPI.executor.submit(() -> {
            final ParticleBuilder builder = creator.getBuilder();
            final LocatedShape shape = creator.createSpiral(start, new Vector(0, 1, 0), 1, 3, 80);
            for (Location location : shape.getLocations()) {
                builder.location(location).spawn();
                WitchcraftAPI.sleep(50);
            }
            final World world = future.join();
            if (!Protection.getInstance().canTeleport(caster, world.getSpawnLocation())) return;
            caster.teleportAsync(world.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
        });
    }
    
}
