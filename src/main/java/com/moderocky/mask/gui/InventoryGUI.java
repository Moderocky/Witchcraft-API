package com.moderocky.mask.gui;

import org.bukkit.event.inventory.InventoryType;

public interface InventoryGUI extends GUI {

    int getSize();

    boolean isEditable();

    InventoryType getType();

}
