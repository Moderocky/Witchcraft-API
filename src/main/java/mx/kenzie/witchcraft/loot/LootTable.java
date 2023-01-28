package mx.kenzie.witchcraft.loot;

import mx.kenzie.fern.Fern;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.data.item.ItemArchetype;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.InputStream;
import java.util.*;

public class LootTable {
    public static final Map<String, LootTable> MAP = new LinkedHashMap<>();
    protected transient final Random random;
    public String id;
    public Entry[] entries = new Entry[0];
    protected transient byte[] stack;

    public LootTable(Random random) {
        this.random = random;
    }

    public LootTable() {
        this.random = new Random();
    }

    public static void load(InputStream stream) {
        MAP.clear();
        final LootTable[] tables;
        try (final Fern fern = new Fern(stream)) {
            tables = fern.toArray(new LootTable[0]);
        }
        assert tables != null;
        for (LootTable table : tables) MAP.put(table.id, table);
    }

    public ItemStack get() {
        return this.get(random);
    }

    public ItemStack get(Random random) {
        if (entries.length == 0) return new ItemStack(Material.AIR);
        if (stack == null) {
            final List<Byte> list = new ArrayList<>(entries.length + 8);
            for (int index = 0; index < entries.length; index++) {
                final Entry entry = entries[index];
                for (int count = 0; count < entry.weight; count++) list.add((byte) index);
            }
            final Byte[] shorts = list.toArray(new Byte[0]);
            this.stack = new byte[shorts.length];
            for (int i = 0; i < stack.length; i++) stack[i] = shorts[i];
            list.clear();
        }
        final short index = stack[random.nextInt(0, stack.length)];
        final Entry entry = entries[index];
        return entry.get(random);
    }

    public ItemStack[] populate(ItemStack[] array, float integrity) {
        for (int i = 0; i < array.length; i++) {
            if (integrity == 1 || random.nextFloat() < integrity) array[i] = this.get(random);
            else array[i] = new ItemStack(Material.AIR);
        }
        return array;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entries.length);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof LootTable table)) return false;
        return Objects.equals(id, table.id) && this.entries.length == table.entries.length;
    }

    public static class Entry {
        public String id;
        public String dice;
        public int weight = 1;
        protected transient Dice _dice;

        public ItemStack get() {
            return this.get(null);
        }

        public ItemStack get(Random random) {
            final ItemArchetype archetype = WitchcraftAPI.resources.getArchetype(id);
            if (archetype == null) return new ItemStack(Material.AIR);
            final ItemStack stack = archetype.create();
            stack.setAmount(this.roll(random));
            return stack;
        }

        public int roll(Random random) {
            if (random == null) return this.roll();
            return this.dice().roll(random);
        }

        public int roll() {
            return this.dice().roll();
        }

        public Dice dice() {
            if (_dice != null) return _dice;
            if (dice == null || dice.isBlank() || dice.equalsIgnoreCase("1d1")) return _dice = Dice.ONE;
            return _dice = Dice.of(dice);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, dice);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Entry entry)) return false;
            return Objects.equals(id, entry.id) && Objects.equals(dice, entry.dice);
        }
    }

}
