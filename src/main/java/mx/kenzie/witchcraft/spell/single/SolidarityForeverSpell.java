package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.Coven;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.entity.Portal;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolidarityForeverSpell extends AbstractTeleportSpell {
    protected transient final ParticleCreator creator;
    protected transient List<Block> blocks;

    public SolidarityForeverSpell(Map<String, Object> map) {
        super(map);
        if (!WitchcraftAPI.isTest)
            creator = WitchcraftAPI.client.particles(new ParticleBuilder(Particle.SPELL_WITCH).count(2).force(true));
        else creator = null;
    }

    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Coven coven = Coven.getCoven(caster);
        final Set<Player> players = new HashSet<>();
        final Location centre = caster.getEyeLocation().add(0, 2.5, 0);
        final Portal portal = CustomEntityType.NETHER_PORTAL.spawn(centre);
        portal.setOrientation(new Vector(0, -1, 0));
        Bukkit.getScheduler().runTaskLater(WitchcraftAPI.plugin, portal::remove, 20 * 8L);
        if (coven == null) {
            caster.sendActionBar(Component.text("You have no coven..."));
            return;
        }
        for (OfflinePlayer member : coven.getMembers()) {
            if (member == caster) continue;
            if (!member.isOnline()) continue;
            players.add(member.getPlayer());
        }
        WitchcraftAPI.executor.submit(() -> { // todo give players a portal choice
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (Player player : players) {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!player.isOnline()) return;
                player.teleportAsync(centre);
                player.setVelocity(new Vector(0, 0, 0));
                player.setFallDistance(0);
            }
        });
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        final Location centre = caster.getEyeLocation();
        double radius = 6.0D;
        this.blocks = getValidTeleportSpaces(centre, radius);
        return blocks.size() >= 1;
    }
}
