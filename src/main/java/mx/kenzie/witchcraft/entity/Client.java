package mx.kenzie.witchcraft.entity;

import com.destroystokyo.paper.ParticleBuilder;
import mx.kenzie.witchcraft.entity.client.AbstractClientArmorStand;
import mx.kenzie.witchcraft.entity.client.AbstractClientEntity;
import mx.kenzie.witchcraft.entity.client.IClientsideEntity;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Client {
    
    ParticleCreator particles(Particle particle);
    
    ParticleCreator particles(ParticleBuilder particle);
    
    void spawnEntity(IClientsideEntity entity, Player target);
    
    void sendPacket(Player player, Object packet);
    
    void sendBlockBreak(int entity, Block block, int stage, Player... receivers);
    
    int spawnClientsideEntity(Location location, String type, Player[] target);
    
    int spawnClientsideEntity(int id, Location location, String type, Player[] target);
    
    void destroyClientsideEntity(int id, Player... target);
    
    IClientsideEntity create(EntityType type);
    
    AbstractClientEntity create(UUID uuid, EntityType type);
    
    AbstractClientArmorStand createArmorStand();
    
    void remove(IClientsideEntity entity);
}
