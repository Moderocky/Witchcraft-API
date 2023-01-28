package com.moderocky.mask.template;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@FunctionalInterface
public interface Listener<T extends Event> extends org.bukkit.event.Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    void event(T event);

}
