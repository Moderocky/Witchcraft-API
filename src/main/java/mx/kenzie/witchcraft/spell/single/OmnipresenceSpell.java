package mx.kenzie.witchcraft.spell.single;

import com.destroystokyo.paper.ParticleBuilder;
import com.destroystokyo.paper.profile.PlayerProfile;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.NPC;
import mx.kenzie.witchcraft.spell.StandardSpell;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class OmnipresenceSpell extends StandardSpell {
    
    protected transient final ParticleBuilder builder = new ParticleBuilder(Particle.DRAGON_BREATH)
        .extra(0.03)
        .force(true);
    private transient Entity target;
    private transient List<Block> blocks;
    
    public OmnipresenceSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        final Player player = ((Player) caster);
        final Location centre = target.getLocation();
        final int count = Math.min(blocks.size(), 3 + (int) amplitude);
        final Random random = ThreadLocalRandom.current();
        for (int i = 0; i < count; i++) {
            Block block = blocks.remove(ThreadLocalRandom.current().nextInt(blocks.size()));
            Location loc = block.getLocation();
            splash(loc);
            loc.setDirection(centre.toVector().subtract(loc.toVector()));
            if (i == 0)
                player.teleport(loc);
            else {
                PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), player.getName());
                profile.setProperties(player.getPlayerProfile().getProperties());
                final NPC npc = WitchcraftAPI.minecraft.spawnNPC(loc, profile, fake -> {
                    final PlayerInventory inventory = fake.getInventory();
                    inventory.setHeldItemSlot(player.getInventory().getHeldItemSlot());
                    inventory.setContents(player.getInventory().getContents());
                    inventory.setArmorContents(player.getInventory().getArmorContents());
                });
                npc.setOnTick(playerNPC -> {
                    int nextInt = random.nextInt(40);
                    if ((nextInt == 19)) {
                        playerNPC.setYHeadRot((float) (playerNPC.getYHeadRot() + ((random.nextDouble() - 0.5) * 25)));
                    } else if (nextInt == 20) {
                        Location l = playerNPC.getBukkitEntity().getLocation();
                        l.setDirection(target.getLocation().toVector()
                            .subtract(playerNPC.getBukkitEntity().getLocation().toVector()));
                        playerNPC.setYHeadRot(l.getYaw());
                    }
                });
                npc.setOnDamage((playerNPC, event) -> {
                    splash(npc.getBukkitEntity().getLocation());
                    event.setDamage(0);
                    playerNPC.discard();
                });
                npc.setOnDeath((playerNPC, event) -> {
                    splash(npc.getBukkitEntity().getLocation());
                    npc.getBukkitEntity().getInventory().clear();
                    event.setKeepInventory(true);
                    event.setKeepLevel(true);
                    event.deathMessage(null);
                    event.setShouldDropExperience(false);
                });
                WitchcraftAPI.scheduler.schedule(() -> {
                    if (!npc.getBukkitEntity().isDead()) {
                        splash(npc.getBukkitEntity().getLocation());
                        npc.getBukkitEntity().remove();
                    }
                }, 12, TimeUnit.SECONDS);
            }
        }
    }
    
    @Override
    public boolean canCast(LivingEntity caster) {
        final Location location = caster.getEyeLocation();
        this.target = WitchcraftAPI.minecraft.getTargetEntity(location, 16, 0.96);
        if (!(target instanceof LivingEntity)) return false;
        if (!(caster instanceof HumanEntity)) return false;
        final int count = 8;
        this.blocks = AbstractTeleportSpell.getValidTeleportSpaces(target.getLocation(), 5.6);
        this.blocks.removeIf(block -> !WitchcraftAPI.minecraft.hasLineOfSight(block.getLocation(), target.getLocation()));
        return blocks.size() > count;
    }
    
    private void splash(Location location) {
        for (int i = 0; i < 10; i++) {
            builder.location(location)
                .spawn();
        }
    }
}
