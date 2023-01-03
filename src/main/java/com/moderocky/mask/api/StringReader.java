package com.moderocky.mask.api;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

public class StringReader implements Iterable<Character> {
    
    public final char[] chars;
    protected transient int position;
    
    StringReader(final MagicStringList list) {
        chars = list.toString().toCharArray();
    }
    
    public StringReader(final String string) {
        chars = string.toCharArray();
    }
    
    public StringReader(final char[] chars) {
        this.chars = chars;
    }
    
    public String readRest() {
        StringBuilder builder = new StringBuilder();
        while (canRead()) {
            builder.append(chars[position]);
            position++;
        }
        return builder.toString();
    }
    
    public boolean canRead() {
        return position < chars.length && position >= 0;
    }
    
    public String read(int length) {
        int end = position + length;
        StringBuilder builder = new StringBuilder();
        while (position < end) {
            if (position >= chars.length) break;
            builder.append(chars[position]);
            position++;
        }
        return builder.toString();
    }
    
    public String readUntil(char c) {
        StringBuilder builder = new StringBuilder();
        while (canRead()) {
            char test = chars[position];
            if (c == test) break;
            builder.append(test);
            position++;
        }
        return builder.toString();
    }
    
    public String readUntilEscape(char c) {
        StringBuilder builder = new StringBuilder();
        boolean ignore = false;
        while (canRead()) {
            char test = chars[position];
            if (ignore) ignore = false;
            else if (test == '\\') ignore = true;
            else if (c == test) break;
            builder.append(test);
            position++;
        }
        return builder.toString();
    }
    
    public String readUntilMatches(Function<String, Boolean> function) {
        StringBuilder builder = new StringBuilder();
        while (canRead()) {
            char test = chars[position];
            builder.append(test);
            position++;
            if (function.apply(builder.toString())) break;
        }
        return builder.toString();
    }
    
    public String readUntilMatches(Pattern pattern) {
        StringBuilder builder = new StringBuilder();
        while (canRead()) {
            char test = chars[position];
            builder.append(test);
            if (pattern.matcher(builder.toString()).matches()) break;
            position++;
        }
        return builder.toString();
    }
    
    public String readUntilMatchesAfter(Pattern pattern, char end) {
        StringBuilder builder = new StringBuilder();
        boolean canEnd = false;
        while (canRead()) {
            char test = chars[position];
            if (test == end) canEnd = true;
            if (canEnd && pattern.matcher(builder.toString()).matches()) break;
            builder.append(test);
            position++;
        }
        return builder.toString();
    }
    
    public boolean hasApproaching(int index) {
        return remaining().length > index;
    }
    
    public char[] remaining() {
        return Arrays.copyOfRange(chars, position, chars.length);
    }
    
    public char getApproaching(int index) {
        return remaining()[index];
    }
    
    public boolean hasNext() {
        return position < chars.length - 1;
    }
    
    public void skip() {
        if (canRead()) position++;
    }
    
    public void skip(int i) {
        position += i;
    }
    
    public void rotateBack(int i) {
        position -= i;
    }
    
    public int getPosition() {
        return position;
    }
    
    public void setPosition(int i) {
        position = i;
    }
    
    public int length() {
        return chars.length;
    }
    
    public char current() {
        if (canRead())
            return chars[position];
        throw new RuntimeException("Limit exceeded!");
    }
    
    public char previous() {
        if (position - 1 >= 0)
            return chars[position - 1];
        throw new RuntimeException("Limit exceeded!");
    }
    
    public char next() {
        if (position + 1 < chars.length)
            return chars[position + 1];
        throw new RuntimeException("Limit exceeded!");
    }
    
    public char rotate() {
        if (canRead()) {
            char c = chars[position];
            position++;
            return c;
        } else throw new RuntimeException("Limit exceeded!");
    }
    
    public void reset() {
        position = 0;
    }
    
    public int charCount(char c) {
        int i = 0;
        for (char ch : chars) {
            if (ch == c) i++;
        }
        return i;
    }
    
    @NotNull
    @Override
    public Iterator<Character> iterator() {
        return new Iterative();
    }
    
    @Override
    @SuppressWarnings("all")
    public StringReader clone() {
        StringReader reader = new StringReader(chars);
        reader.position = position;
        return reader;
    }
    
    @Override
    public String toString() {
        return new String(chars);
    }
    
    protected class Iterative implements Iterator<Character> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int size = chars.length;
        
        Iterative() {
        }
        
        public boolean hasNext() {
            return cursor != chars.length;
        }
        
        public Character next() {
            checkForComodification();
            int i = cursor;
            if (i >= chars.length)
                throw new NoSuchElementException();
            cursor = i + 1;
            return chars[lastRet = i];
        }
        
        public void remove() {
            throw new ConcurrentModificationException();
        }
        
        @Override
        public void forEachRemaining(Consumer<? super Character> consumer) {
            Objects.requireNonNull(consumer);
            final int size = chars.length;
            int i = cursor;
            if (i >= size) {
                return;
            }
            while (i != size) {
                consumer.accept(chars[i++]);
            }
            // update once at end of iteration to reduce heap write traffic
            cursor = i;
            lastRet = i - 1;
            checkForComodification();
        }
        
        final void checkForComodification() {
        }
    }
    
}
