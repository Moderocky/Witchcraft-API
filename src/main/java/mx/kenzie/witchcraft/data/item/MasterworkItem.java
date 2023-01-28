package mx.kenzie.witchcraft.data.item;

import mx.kenzie.fern.Fern;

import java.io.OutputStream;

public class MasterworkItem extends Item implements ItemArchetype {

    @Override
    public boolean isProtected() {
        return false;
    }

    @Override
    public Rarity rarity() {
        return Rarity.MYTHICAL;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public void write(OutputStream stream) {
        Fern.trans(this, stream);
    }

}
