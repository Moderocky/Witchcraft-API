package com.moderocky.mask.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

class ArgLiteralPlural extends ArgLiteral {
    
    public final Set<String> aliases = new HashSet<>();
    private final Pattern pattern;
    
    public ArgLiteralPlural(String... aliases) {
        super(aliases[0].toLowerCase());
        for (String alias : aliases) this.aliases.add(alias.toLowerCase());
        pattern = Pattern.compile("^(" + String.join("|", this.aliases) + ")$");
    }
    
    public ArgLiteralPlural(Collection<String> aliases) {
        super(aliases.iterator().next().toLowerCase());
        for (String alias : aliases) this.aliases.add(alias.toLowerCase());
        pattern = Pattern.compile("^(" + String.join("|", this.aliases) + ")$");
    }
    
    public ArgLiteralPlural(String name, String... aliases) {
        super(name);
        this.aliases.add(name.toLowerCase());
        for (String alias : aliases) this.aliases.add(alias.toLowerCase());
        pattern = Pattern.compile("^(" + String.join("|", this.aliases) + ")$");
    }
    
    @NotNull
    @Override
    public Void serialise(String string) {
        return null;
    }
    
    @Override
    public boolean matches(String string) {
        return aliases.contains(string.toLowerCase());
    }
    
    @Override
    public @NotNull String getName() {
        return aliases.iterator().next();
    }
    
    @Override
    public @Nullable List<String> getCompletions() {
        return new ArrayList<>(aliases);
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
        return pattern;
    }
}
