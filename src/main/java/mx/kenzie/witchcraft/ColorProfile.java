package mx.kenzie.witchcraft;

import net.kyori.adventure.text.format.TextColor;

public record ColorProfile(TextColor highlight,
                           TextColor lowlight,
                           TextColor neutral,
                           TextColor shadow,
                           TextColor pop) {
}
