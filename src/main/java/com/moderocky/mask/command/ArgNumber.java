package com.moderocky.mask.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Moderocky
 * @version 1.0.0
 */
public class ArgNumber implements Argument<Double> {
    
    private static final Pattern pattern = Pattern.compile("^(-?[0-9]+|-?[0-9]+\\.[0-9]*[1-9])$");
    private String label = "number";
    private boolean required = true;
    private Double def = null;
    
    @Override
    public @NotNull Double serialise(String string) {
        try {
            return Double.valueOf(string);
        } catch (Throwable throwable) {
            return def;
        }
    }
    
    @Override
    public boolean matches(String string) {
        if (string.isEmpty()) return false;
        try {
            double d = Double.parseDouble(string);
            return d == d; // Not always true.
        } catch (Throwable throwable) {
            return false;
        }
    }
    
    @Override
    public @NotNull String getName() {
        return label;
    }
    
    @Override
    public @Nullable List<String> getCompletions() {
        return null;
    }
    
    @Override
    public boolean isPlural() {
        return false;
    }
    
    @Override
    public boolean isRequired() {
        return required;
    }
    
    @Override
    public ArgNumber setRequired(boolean boo) {
        required = boo;
        return this;
    }
    
    @Override
    public ArgNumber setLabel(@NotNull String label) {
        this.label = label;
        return this;
    }
    
    @Override
    public Pattern getPattern() {
        return pattern;
    }
    
    public ArgNumber setDefault(Double def) {
        this.def = def;
        return this;
    }
    
}
