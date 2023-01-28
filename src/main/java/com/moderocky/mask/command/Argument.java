package com.moderocky.mask.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @param <X> The return type post-serialisation.
 * @author Moderocky
 * @version 2.0.0
 * <p>
 * This class manages arguments for the .
 * The key aspects are to check if an input is valid, convert it to a workable type and to provide completions for guidance.
 */
public interface Argument<X> {

    /**
     * This should convert the player's input to a valid object.
     * This should never be null (?!) as {@link #matches(String)} will be called first.
     * Generally, throw an exception if something goes wrong.
     *
     * @param string The passed argument.
     * @return The serialised object
     */
    @NotNull X serialise(String string);

    /**
     * This is your matcher, to check if the player's input is valid. If it isn't, the action won't pass.
     * <p>
     * For example: a material-accepting argument checks if the string matches a Material enum and, if so, if the enum is a block.
     * In this case, "grass_block" would pass Material#GRASS_BLOCK but "blob" and "diamond_sword" wouldn't as they are not materials/blocks respectively.
     * <p>
     * This should be a catch-all. If there is any chance of it not being valid, this should FAIL.
     *
     * @param string The player's input argument.
     * @return Boo
     */
    boolean matches(String string);

    @SuppressWarnings("unchecked")
    default Class<X> getType() {
        return ((Class<X>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    /**
     * This is what your argument shows up as in a command.
     * <p>
     * Good examples: "block", "material", "entity", "player"
     * <p>
     * Bad examples: "string" (one exists already), "&lt;test&gt;" (the brackets are added later), "something" (vague and unhelpful), "blocks like stone" (should be one word), "verylongcommandargument" (too long).
     *
     * @return The argument name
     */
    @NotNull String getName();

    /**
     * A list of completions for this argument type.
     * This helps the player to understand what is required.
     *
     * @return A list of completions
     */
    @Nullable List<String> getCompletions();

    /**
     * Used by the system to determine if this argument accepts multiple inputs.
     * These should almost always be separated by commas.
     * <p>
     * The command handler will automatically handle plural suggestions using comma separation.
     * <p>
     * Typical plurals: "stone,grass_block,dirt", "stone_stairs[facing=north],oak_log,stone_slab[half=top]"
     *
     * @return Whether this accepts plurals, true or false
     */
    boolean isPlural();

    /**
     * Used by the system when testing the command.
     * This should ALWAYS default to true.
     *
     * @return Boo
     */
    boolean isRequired();

    /**
     * Ideally, this should set a field that will then be returned by {@link #isRequired()}.
     * This should ALWAYS default to true.
     * <p>
     * Note: You may want to change the {@link Argument} returned to be of your implementing class type.
     *
     * @param boo True/false.
     * @return The argument, allowing for method chaining
     */
    Argument<X> setRequired(boolean boo);

    /**
     * Ideally, this should set a field that will then be returned by {@link #getName()}.
     * <p>
     * Note: You may want to change the {@link Argument} returned to be of your implementing class type.
     *
     * @param name The label to be put [here];
     * @return The argument, allowing for method chaining
     */
    Argument<X> setLabel(@NotNull String name);

    default Pattern getPattern() {
        return Pattern.compile("^(\\S+)$");
    }

    default boolean acceptSpaces() {
        return false;
    }

    default boolean isLiteral() {
        return false;
    }

    default boolean isFinal() {
        return false;
    }

}
