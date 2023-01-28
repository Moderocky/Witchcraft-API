package com.moderocky.mask.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

class ArgLiteral implements Argument<Void> {

    private final String name;

    public ArgLiteral(String name) {
        this.name = name.trim();
    }

    @NotNull
    @Override
    public Void serialise(String string) {
        return null;
    }

    @Override
    public boolean matches(String string) {
        return name.equalsIgnoreCase(string.trim());
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable List<String> getCompletions() {
        return List.of(name);
    }

    @Override
    public boolean isPlural() {
        return false;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public Argument<Void> setRequired(boolean boo) {
        return this;
    }

    @Override
    public Argument<Void> setLabel(@NotNull String name) {
        return this;
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile("^(" + name + ")$");
    }

    @Override
    public boolean isLiteral() {
        return true;
    }
}
