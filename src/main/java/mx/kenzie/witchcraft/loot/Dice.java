package mx.kenzie.witchcraft.loot;

import java.util.Objects;
import java.util.Random;

public record Dice(int rolls, int sides) {
    public static final Random RANDOM = new Random('k' + 'e' + 'n' + 'z' + 'i' + 'e');
    public static final Dice ONE = new Dice("1d1");
    public static final Dice SIX = new Dice("1d6");
    
    public Dice(String text) {
        this(text.split("d"));
    }
    
    private Dice(String[] parts) {
        this(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
    }
    
    public static Dice of(String counter) {
        final int index = counter.indexOf('d');
        return new Dice(
            Integer.parseInt(counter.substring(0, index).trim()),
            Integer.parseInt(counter.substring(index + 1).trim())
        );
    }
    
    public int roll() {
        return this.roll(RANDOM);
    }
    
    public int roll(Random random) {
        int total = 0;
        for (int i = 0; i < rolls; i++) total += random.nextInt(0, sides) + 1;
        return total;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dice dice)) return false;
        return rolls == dice.rolls && sides == dice.sides;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(rolls, sides);
    }
    
    @Override
    public String toString() {
        return rolls + "d" + sides;
    }
}
