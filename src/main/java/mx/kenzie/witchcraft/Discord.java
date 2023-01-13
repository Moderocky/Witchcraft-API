package mx.kenzie.witchcraft;

import mx.kenzie.eris.api.entity.Channel;
import mx.kenzie.eris.api.entity.User;
import mx.kenzie.witchcraft.data.Coven;
import mx.kenzie.witchcraft.data.PlayerData;

import java.util.UUID;

public interface Discord {
    void linkAccount(UUID uuid, User user);
    
    void sync(PlayerData data);
    
    void sendMessage(Coven coven, String message);
    
    Channel getChannel(Coven coven);
    
    void delete(Coven coven);
    
    void sync(Coven coven);
    
    void sync(Coven coven, PlayerData data);
    
    void sync(Coven coven, Channel channel);
}
