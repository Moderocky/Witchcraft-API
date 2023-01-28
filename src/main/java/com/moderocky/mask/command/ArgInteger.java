package com.moderocky.mask.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Moderocky
 * @version 1.0.0
 */
public class ArgInteger implements Argument<Integer> {
    private static final Pattern pattern = Pattern.compile("^(-?[0-9]{1,10})$");
    private String label = "int";
    private boolean required = true;
    private Integer def = null;

    @Override
    public @NotNull Integer serialise(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Throwable throwable) {
            return def;
        }
    }

    @Override
    public boolean matches(String string) {
        if (string.isEmpty()) return false;
        try {
            int i = Integer.parseInt(string);
            return i == i; // Not always true.
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
    public ArgInteger setRequired(boolean boo) {
        required = boo;
        return this;
    }

    @Override
    public ArgInteger setLabel(@NotNull String label) {
        this.label = label;
        return this;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    public ArgInteger setDefault(Integer def) {
        this.def = def;
        return this;
    }
}
