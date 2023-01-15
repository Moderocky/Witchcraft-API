package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.data.PlayerData;
import mx.kenzie.witchcraft.data.Position;
import mx.kenzie.witchcraft.data.world.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RealmWarpSpell extends TeleportSpell {
    
    public RealmWarpSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public void run(LivingEntity caster, int range, float scale, double amplitude) {
        if (!(caster instanceof Player player)) return;
        final List<World> worlds = Bukkit.getWorlds();
        final List<Position> list = new ArrayList<>(worlds.size());
        final PlayerData info = PlayerData.getData(player);
        for (World world : worlds) {
            if (info.isBanished(world)) continue;
            final WorldData data = WorldData.getData(world);
            if (data.restricted) continue;
            list.add(new Position.Static(world.getSpawnLocation(), data.name()));
        }
        this.createMenu(player, list);
    }
    
}
