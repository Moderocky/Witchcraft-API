package com.moderocky.mask.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

public class MagicStringList extends ArrayList<String> {

    public MagicStringList() {
        super();
    }

    public MagicStringList(Collection<String> collection) {
        super(collection);
    }

    public MagicStringList(String... strings) {
        super(Arrays.asList(strings));
    }

    public static MagicStringList from(Collection<?> collection) {
        MagicStringList list = new MagicStringList();
        for (Object o : collection) {
            list.add(o.toString());
        }
        return list;
    }

    public static MagicStringList from(Object... objects) {
        MagicStringList list = new MagicStringList();
        for (Object o : objects) {
            list.add(o.toString());
        }
        return list;
    }

    public MagicStringList filter(Pattern pattern) {
        MagicStringList list = new MagicStringList();
        for (String string : this) {
            if (pattern.matcher(string).matches()) list.add(string);
        }
        return list;
    }

    public MagicStringList collectStrings(Function<String, String> function) {
        final MagicStringList list = new MagicStringList();
        for (String string : this) list.add(function.apply(string));
        return list;
    }

    public <U> MagicStringList collectStrings(BiFunction<String, U, String> biFunction, U argument) {
        final MagicStringList list = new MagicStringList();
        for (String string : this) list.add(biFunction.apply(string, argument));
        return list;
    }

    public void removeMatching(Pattern pattern) {
        this.removeIf(string -> pattern.matcher(string).matches());
    }

    public void removeNotMatching(Pattern pattern) {
        this.removeIf(string -> !pattern.matcher(string).matches());
    }

    public boolean containsIgnoreCase(String string) {
        for (String s : this) {
            if (s.equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public boolean anyContains(String string) {
        for (String s : this) {
            if (s.contains(string)) return true;
        }
        return false;
    }

    public MagicStringList from(int start) {
        return from(start, size());
    }

    public MagicStringList from(int start, int end) {
        if (start < 0) throw new IllegalArgumentException("Start index must be positive!");
        if (end > size()) throw new IllegalArgumentException("End index must not be greater than the list's size!");
        MagicStringList list = new MagicStringList();
        for (int i = start; i < end; i++) {
            list.add(get(i));
        }
        return list;
    }

    public double getMeanLength() {
        double sum = 0;
        for (String s : this) {
            sum += s.length();
        }
        return sum / size();
    }

    public String join(CharSequence delimiter) {
        return String.join(delimiter, this);
    }

    public String join(char delimiter) {
        return String.join(String.valueOf(delimiter), this);
    }

    public MagicStringList withoutEmpty() {
        MagicStringList list = new MagicStringList(this);
        list.removeIf(string -> string.trim().isEmpty());
        return list;
    }

    @Override
    public String[] toArray() {
        return super.toArray(new String[0]);
    }

    @Override
    public String toString() {
        return String.join(" ", this);
    }

}
