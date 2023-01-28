package mx.kenzie.witchcraft;

import mx.kenzie.witchcraft.data.item.ItemArchetype;
import mx.kenzie.witchcraft.texture.Texture;
import org.bukkit.entity.Player;

public interface TextureManager {

    static TextureManager getInstance() {
        return WitchcraftAPI.textures;
    }

    Texture get(Player player, ItemArchetype... clothes);

}
