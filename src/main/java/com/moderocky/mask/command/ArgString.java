package com.moderocky.mask.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Moderocky
 * @version 1.0.0
 */
public class ArgString implements Argument<String> {

    private static final Pattern pattern = Pattern.compile("^('.+'|\".+\"|[^ \"'\\t\\r\\s\\n]+)$");
    private String label = "string";
    private boolean required = true;

    @Override
    public @NotNull String serialise(String string) {
        if ((string.startsWith("\"") && string.endsWith("\"")) || (string.startsWith("'") && string.endsWith("'")))
            return string.substring(1, string.length() - 1);
        return string;
    }

    @Override
    public boolean matches(String string) {
        return true;
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
    public ArgString setRequired(boolean boo) {
        required = boo;
        return this;
    }

    @Override
    public ArgString setLabel(@NotNull String label) {
        this.label = label;
        return this;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public boolean acceptSpaces() {
        return true;
    }

}
