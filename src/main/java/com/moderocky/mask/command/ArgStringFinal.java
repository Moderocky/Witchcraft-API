package com.moderocky.mask.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

public class ArgStringFinal implements Argument<String> {
    
    private String label = "string";
    private boolean required = true;
    
    @Override
    @SuppressWarnings("all")
    public @NotNull String serialise(String string) {
        return string.toString();
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
    public ArgStringFinal setRequired(boolean boo) {
        required = boo;
        return this;
    }
    
    @Override
    public ArgStringFinal setLabel(@NotNull String label) {
        this.label = label;
        return this;
    }
    
    @Override
    public Pattern getPattern() {
        return Pattern.compile("^(.+)$");
    }
    
    @Override
    public boolean isFinal() {
        return true;
    }
}
