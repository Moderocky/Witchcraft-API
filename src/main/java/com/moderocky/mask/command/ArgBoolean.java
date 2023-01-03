package com.moderocky.mask.command;

import com.moderocky.mask.api.MagicStringList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Moderocky
 * @version 1.0.0
 */
public class ArgBoolean implements Argument<Boolean> {
    
    private static final Pattern pattern = Pattern.compile("^(yes|y|no|n|true|false|1|0)$");
    private final MagicStringList ayes = new MagicStringList("yes", "1", "true", "y");
    private final MagicStringList noes = new MagicStringList("no", "0", "false", "n");
    private String label = "boolean";
    private boolean required = true;
    private Boolean def = null;
    
    @Override
    public @NotNull Boolean serialise(String string) {
        return ayes.containsIgnoreCase(string);
    }
    
    @Override
    public boolean matches(String string) {
        return ayes.containsIgnoreCase(string) || noes.containsIgnoreCase(string);
    }
    
    @Override
    public @NotNull String getName() {
        return label;
    }
    
    @Override
    public @Nullable List<String> getCompletions() {
        return new MagicStringList("true", "false");
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
    public ArgBoolean setRequired(boolean boo) {
        required = boo;
        return this;
    }
    
    @Override
    public ArgBoolean setLabel(@NotNull String label) {
        this.label = label;
        return this;
    }
    
    @Override
    public Pattern getPattern() {
        return pattern;
    }
    
    public ArgBoolean setDefault(Boolean def) {
        this.def = def;
        return this;
    }
}
