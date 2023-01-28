package com.moderocky.mask.command;

import com.moderocky.mask.api.StringReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Commander<S> {

    final List<String> patterns = new ArrayList<>();
    final Map<String, @Nullable String> patternDescriptions = new HashMap<>();
    final Set<String> aliases = new HashSet<>();
    final ArgumentTree tree = new ArgumentTree();
    private BiFunction<S, Throwable, Boolean> error = null;
    private String namespace;
    private volatile String input;

    {
        this.compile();
    }

    final void compile() {
        patterns.clear();
        patternDescriptions.clear();
        aliases.clear();
        tree.clear();
        this.create();
        final String regex = "^[^\\[<\\n]+((?> [<\\[]\\S+[\\]>])+)$";
        Pattern pattern = Pattern.compile(regex);
        {
            List<String> list = new ArrayList<>(this.getPossibleArguments());
            List<String> catcher = new ArrayList<>(list);
            catcher.removeIf(s -> !s.contains("<") && !s.contains("["));
            catcher.removeIf(s -> !s.matches(regex));
            for (String string : new ArrayList<>(catcher)) {
                Matcher matcher = pattern.matcher(string);
                if (!matcher.find()) continue;
                String group = matcher.group(1).trim();
                String test = string.replace(group, "").trim();
                boolean matches = false;
                for (String str : new ArrayList<>(list)) {
                    if (str.trim().equalsIgnoreCase(test)) {
                        matches = true;
                        list.remove(str);
                    }
                }
                if (matches) {
                    list.remove(string);
                    list.add(group.contains("<") ? (test + " [" + group + "]") : (test + " " + group));
                }
            }
            this.patterns.addAll(list);
            Collections.sort(patterns);
        }
        {
            Map<String, String> map = new HashMap<>(getPatternDesc());
            Map<String, String> catcher = new HashMap<>(map);
            catcher.keySet().removeIf(s -> !s.contains("<") && !s.contains("["));
            catcher.keySet().removeIf(s -> !s.matches(regex));
            for (String string : new ArrayList<>(catcher.keySet())) {
                Matcher matcher = pattern.matcher(string);
                if (!matcher.find()) continue;
                String group = matcher.group(1).trim();
                String test = string.replace(group, "").trim();
                boolean matches = false;
                for (String str : new ArrayList<>(map.keySet())) {
                    if (str.trim().equalsIgnoreCase(test)) {
                        matches = true;
                        map.remove(str);
                    }
                }
                if (matches) {
                    String desc = map.remove(string);
                    map.put(group.contains("<") ? (test + " [" + group + "]") : (test + " " + group), desc);
                }
            }
            this.patternDescriptions.putAll(map);
        }
    }

    protected abstract CommandImpl create();

    public List<String> getPossibleArguments(String... inputs) {
        if (inputs.length == 0) {
            final List<String> strings = new ArrayList<>(tree.size());
            for (ArgumentEntry entry : tree.keySet()) strings.add(entry.args);
            return strings;
        }
        String input = String.join(" ", inputs);
        List<ArgumentEntry> list = new ArrayList<>(tree.keySet());
        list.removeIf(entry -> !entry.matches(input));
        final List<String> strings = new ArrayList<>(tree.size());
        for (ArgumentEntry entry : list) strings.add(entry.args);
        return strings;
    }

    private @NotNull Map<@NotNull String, @Nullable String> getPatternDesc() {
        Map<@NotNull String, @Nullable String> map = new HashMap<>();
        for (ArgumentEntry entry : tree.keySet()) map.put(entry.args, entry.description);
        return map;
    }

    public synchronized boolean execute(S sender, String... inputs) {
        try {
            input = String.join(" ", inputs);
            List<Map.Entry<ArgumentEntry, CommandAction<S>>> list = new LinkedList<>(tree.entrySet());
            Map<Map.Entry<ArgumentEntry, CommandAction<S>>, ArgumentEntry.Result> map = new LinkedHashMap<>();
            for (Map.Entry<ArgumentEntry, CommandAction<S>> entry : list) {
                ArgumentEntry.Result result = entry.getKey().matchesEntry(input);
                if (result == ArgumentEntry.Result.FALSE) continue;
                map.put(entry, result);
            }
            if (!map.isEmpty()) {
                ArgumentEntry entry;
                CommandAction<S> action;
                Map<Map.Entry<ArgumentEntry, CommandAction<S>>, ArgumentEntry.Result> perfect = new HashMap<>(map);
                perfect.entrySet().removeIf(e -> e.getValue() == ArgumentEntry.Result.TRAILING);
                if (perfect.isEmpty()) {
                    final Map.Entry<Map.Entry<ArgumentEntry, CommandAction<S>>, ArgumentEntry.Result> thing = map.entrySet()
                        .iterator().next();
                    entry = thing.getKey().getKey();
                    action = thing.getKey().getValue();
                } else {
                    final Map.Entry<Map.Entry<ArgumentEntry, CommandAction<S>>, ArgumentEntry.Result> thing = perfect.entrySet()
                        .iterator().next();
                    entry = thing.getKey().getKey();
                    action = thing.getKey().getValue();
                }
                if (action instanceof CommandSingleAction) action.accept(sender);
                else action.accept(sender, entry.compileEntry(input));
            } else {
                getDefault().accept(sender);
            }
            return true;
        } catch (Throwable throwable) {
            if (error == null)
                System.out.println("Command Error in '" + getCommand() + "' - " + throwable.getMessage());
            else return error.apply(sender, throwable);
            return false;
        }
    }

    public abstract CommandSingleAction<S> getDefault();

    public @NotNull String getCommand() {
        return namespace;
    }

    public List<String> getTabCompletions(String input) {
        String[] inputs = input.split(" ");
        return getTabCompletions(inputs);
    }

    public List<String> getTabCompletions(String... inputs) {
        List<String> list = new ArrayList<>();
        for (ArgumentEntry arguments : tree.keySet()) {
            for (String completion : arguments.getCompletions(inputs.length, inputs)) {
                if (list.contains(completion)) continue;
                list.add(completion);
            }
        }
        return list;
    }

    public @NotNull Collection<String> getPatterns() {
        return new ArrayList<>(patterns);
    }

    public @NotNull Map<@NotNull String, @Nullable String> getPatternDescriptions() {
        return new HashMap<>(patternDescriptions);
    }

    public String getInput() {
        return input;
    }

    protected CommandImpl command(String namespace, String... aliases) {
        this.namespace = namespace;
        this.aliases.addAll(List.of(aliases));
        return new CommandImpl();
    }

    public Description desc(@Nullable String string) {
        return string != null ? new Description(string) : null;
    }

    public SubArg arg(String arg, CommandSingleAction<S> action) {
        return new SubArg(new ArgLiteral(arg), action);
    }

    public SubArg arg(String arg, Description description, CommandSingleAction<S> action) {
        SubArg subArg = new SubArg(new ArgLiteral(arg), action);
        subArg.description = description;
        return subArg;
    }

    @SafeVarargs
    public final SubArg arg(String arg, SubArg... args) {
        return new SubArg(new ArgLiteral(arg), null, args);
    }

    @SafeVarargs
    public final SubArg arg(String @NotNull [] arg, SubArg... args) {
        return new SubArg(new ArgLiteralPlural(arg), null, args);
    }

    @SafeVarargs
    public final SubArg arg(String @NotNull [] arg, CommandSingleAction<S> action, SubArg... args) {
        return new SubArg(new ArgLiteralPlural(arg), action, args);
    }

    @SafeVarargs
    public final SubArg arg(String @NotNull [] arg, Description description, CommandSingleAction<S> action, SubArg... args) {
        SubArg subArg = new SubArg(new ArgLiteralPlural(arg), action, args);
        subArg.description = description;
        return subArg;
    }

    @SafeVarargs
    public final SubArg arg(String arg, CommandSingleAction<S> action, SubArg... args) {
        return new SubArg(new ArgLiteral(arg), action, args);
    }

    @SafeVarargs
    public final SubArg arg(String arg, Description description, CommandSingleAction<S> action, SubArg... args) {
        SubArg subArg = new SubArg(new ArgLiteral(arg), action, args);
        subArg.description = description;
        return subArg;
    }

    public SubArg arg(CommandSingleAction<S> action, String... arg) {
        return new SubArg(new ArgLiteralPlural(arg), action);
    }

    public SubArg arg(CommandSingleAction<S> action, Description description, String... arg) {
        SubArg subArg = new SubArg(new ArgLiteralPlural(arg), action);
        subArg.description = description;
        return subArg;
    }

    public SubArg arg(CommandBiAction<S> action, @NotNull Argument<?>... arguments) {
        return arg(null, action, arguments);
    }

    public SubArg arg(Description description, CommandBiAction<S> action, @NotNull Argument<?>... arguments) {
        if (arguments.length == 0) throw new IllegalArgumentException("No arguments were provided!");
        SubArg top = null;
        SubArg arg = null;
        for (Argument<?> argument : arguments) {
            SubArg current = new SubArg(argument, (CommandAction<S>) null);
            if (top == null) top = current;
            if (arg != null) {
                arg.children.add(current);
            }
            arg = current;
        }
        arg.action = action;
        arg.description = description;
        return top;
    }

    public @NotNull List<String> getAliases() {
        return new ArrayList<>(aliases);
    }

    interface CommandAction<S> {
        default void execute(S sender, Object[] object) {
            if (isSingle()) accept(sender);
            else accept(sender, object);
        }

        boolean isSingle();

        void accept(S sender);

        void accept(S sender, Object[] object);

    }

    @FunctionalInterface
    public interface CommandSingleAction<S> extends CommandAction<S> {
        default boolean isSingle() {
            return true;
        }

        void accept(S sender);

        @Override
        default void accept(S sender, Object[] object) {
            accept(sender);
        }
    }

    @FunctionalInterface
    public interface CommandBiAction<S> extends CommandAction<S> {
        default boolean isSingle() {
            return false;
        }

        @Override
        default void accept(S sender) {
            accept(sender, new Object[0]);
        }

        void accept(S sender, Object[] object);
    }

    private static class Description {
        protected final @Nullable String string;

        public Description(@Nullable String string) {
            this.string = string;
        }
    }

    static class ArgumentEntry extends ArrayList<Argument<?>> {

        final Pattern pattern;
        final String args;
        String description;

        {
            StringBuilder builder = new StringBuilder();
            List<String> strings = new ArrayList<>();
            for (Argument<?> argument : this) {
                builder.append(argument.getPattern().toString().replace("^", "").replace("$", "")).append(" ");
                if (argument.isLiteral()) {
                    strings.add(argument.getName());
                } else {
                    strings.add((argument.isRequired() ? "<" : "[") + (argument.isPlural() ? "*" : "") + argument.getName() + (argument.isFinal() ? "..." : "") + (argument.isRequired() ? ">" : "]"));
                }
            }
            pattern = Pattern.compile("^" + builder.toString().trim());
            args = String.join(" ", strings);
        }

        public ArgumentEntry(Argument<?>... arguments) {
            super(List.of(arguments));
        }

        public ArgumentEntry(Collection<Argument<?>> arguments) {
            super(arguments);
        }

        Pattern getPattern() {
            return pattern;
        }

        boolean matches(String input) {
            Matcher matcher = pattern.matcher(input.trim());
            return matcher.matches();
        }

        public Result matchesEntry(String input) {
            StringReader reader = new StringReader(input);
            for (Argument<?> argument : this) {
                final String segment;
                if (argument.isFinal()) {
                    segment = reader.readRest();
                } else if (argument.acceptSpaces()) {
                    segment = reader.readUntilMatchesAfter(argument.getPattern(), ' ');
                } else {
                    segment = reader.readUntil(' ');
                }
                if (argument.isRequired()) {
                    if (!argument.getPattern().matcher(segment).matches()) return Result.FALSE;
                    if (!argument.matches(segment)) return Result.FALSE;
                }
                reader.skip();
            }
            return reader.readRest().trim().isEmpty() ? Result.TRUE : Result.TRAILING;
        }

        public Object[] compileEntry(String input) {
            List<Object> objects = new ArrayList<>();
            StringReader reader = new StringReader(input);
            for (Argument<?> argument : this) {
                final String segment;
                if (argument.isFinal()) {
                    segment = reader.readRest();
                } else if (argument.acceptSpaces()) {
                    segment = reader.readUntilMatchesAfter(argument.getPattern(), ' ');
                } else {
                    segment = reader.readUntil(' ');
                }
                if (!argument.isLiteral())
                    objects.add(argument.matches(segment) ? argument.serialise(segment) : null);
                reader.skip();
            }
            return objects.toArray(new Object[0]);
        }

        Collection<String> getCompletions(int position, String[] inputs) {
            List<String> list = new ArrayList<>();
            if (position < 2) {
                List<String> strings = get(0).getCompletions();
                if (strings != null)
                    list.addAll(strings);
            } else if (size() > position - 1) check:{
                if (!inputMatches(inputs)) break check;
                List<String> strings = get(position - 1).getCompletions();
                if (strings != null)
                    list.addAll(strings);
            }
            return list;
        }

        boolean inputMatches(String[] inputs) {
            StringBuilder builder = new StringBuilder();
            int i = 1;
            for (Argument<?> argument : this) {
                i++;
                if (i == inputs.length) break;
                if (argument instanceof ArgLiteralPlural) {
                    builder
                        .append("(")
                        .append(String.join("|", ((ArgLiteralPlural) argument).aliases))
                        .append(")")
                        .append(" ");
                } else if (argument instanceof ArgLiteral) {
                    builder
                        .append(argument.getName())
                        .append(" ");
                } else if (argument.isRequired()) {
                    if (argument.isFinal())
                        builder
                            .append("(.+)")
                            .append(" ");
                    else
                        builder
                            .append("(\\S+)")
                            .append(" ");
                } else {
                    if (argument.isFinal())
                        builder
                            .append("(.*)")
                            .append(" ");
                    else
                        builder
                            .append("(\\S*)")
                            .append(" ?");
                }
            }
            Pattern pattern = Pattern.compile("^" + builder.toString().toLowerCase().trim() + ".*");
            return pattern.matcher(String.join(" ", inputs).toLowerCase().trim()).matches();
        }

        public enum Result {
            FALSE,
            TRAILING,
            TRUE
        }

    }

    public class CommandImpl {

        private CommandImpl() {
        }

        public CommandImpl arg(String arg, CommandSingleAction<S> action) {
            return arg(arg, null, action);
        }

        public CommandImpl arg(String arg, Description description, CommandSingleAction<S> action) {
            final ArgumentEntry entry = new ArgumentEntry(new ArgLiteral(arg));
            if (description != null) entry.description = description.string;
            tree.put(entry, action);
            return this;
        }

        public CommandImpl arg(CommandSingleAction<S> action, String arg) {
            return arg(action, arg, (Description) null);
        }

        public CommandImpl arg(CommandSingleAction<S> action, String arg, Description description) {
            ArgumentEntry entry = new ArgumentEntry(new ArgLiteral(arg));
            if (description != null) entry.description = description.string;
            tree.put(entry, action);
            return this;
        }

        public CommandImpl arg(CommandSingleAction<S> action, String... arg) {
            ArgumentEntry entry = new ArgumentEntry(new ArgLiteralPlural(arg));
            tree.put(entry, action);
            return this;
        }

        public CommandImpl arg(CommandSingleAction<S> action, Description description, String... arg) {
            ArgumentEntry entry = new ArgumentEntry(new ArgLiteralPlural(arg));
            if (description != null) entry.description = description.string;
            tree.put(entry, action);
            return this;
        }

        @SafeVarargs
        public final CommandImpl arg(String @NotNull [] arg, SubArg... subArguments) {
            final List<Argument<?>> list = new ArrayList<>(List.of(new ArgLiteralPlural(arg)));
            for (SubArg subArg : subArguments) {
                subArg.compile(list);
            }
            return this;
        }

        @SafeVarargs
        public final CommandImpl arg(String arg, SubArg... subArguments) {
            final List<Argument<?>> list = new ArrayList<>(List.of(new ArgLiteral(arg)));
            for (SubArg subArg : subArguments) {
                subArg.compile(list);
            }
            return this;
        }

        @SafeVarargs
        public final CommandImpl arg(String arg, CommandSingleAction<S> action, SubArg... subArguments) {
            return arg(arg, null, action, subArguments);
        }

        @SafeVarargs
        public final CommandImpl arg(String arg, Description description, CommandSingleAction<S> action, SubArg... subArguments) {
            ArgumentEntry entry = new ArgumentEntry(new ArgLiteral(arg));
            if (description != null) entry.description = description.string;
            tree.put(entry, action);
            final List<Argument<?>> list = new ArrayList<>(List.of(new ArgLiteral(arg)));
            for (SubArg subArg : subArguments) {
                subArg.compile(list);
            }
            return this;
        }

        @SafeVarargs
        public final CommandImpl arg(CommandSingleAction<S> action, String arg, SubArg... subArguments) {
            ArgumentEntry entry = new ArgumentEntry(new ArgLiteral(arg));
            tree.put(entry, action);
            List<Argument<?>> list = new ArrayList<>(List.of(new ArgLiteral(arg)));
            for (SubArg subArg : subArguments) {
                subArg.compile(list);
            }
            return this;
        }

        @SafeVarargs
        public final CommandImpl arg(CommandSingleAction<S> action, String arg, Description description, SubArg... subArguments) {
            return arg(arg, description, action, subArguments);
        }

        @SafeVarargs
        public final CommandImpl arg(CommandSingleAction<S> action, String @NotNull [] arg, SubArg... subArguments) {
            return arg(action, arg, null, subArguments);
        }

        @SafeVarargs
        public final CommandImpl arg(CommandSingleAction<S> action, String @NotNull [] arg, Description description, SubArg... subArguments) {
            ArgumentEntry entry = new ArgumentEntry(new ArgLiteralPlural(arg));
            if (description != null) entry.description = description.string;
            tree.put(entry, action);
            final List<Argument<?>> list = new ArrayList<>(List.of(new ArgLiteralPlural(arg)));
            for (SubArg subArg : subArguments) {
                subArg.compile(list);
            }
            return this;
        }

        public final CommandImpl arg(CommandBiAction<S> action, @NotNull Argument<?>... arguments) {
            return arg(null, action, arguments);
        }

        public final CommandImpl arg(Description description, CommandBiAction<S> action, @NotNull Argument<?>... arguments) {
            if (arguments.length == 0) {
                return this;
            }
            SubArg top = null;
            SubArg arg = null;
            for (Argument<?> argument : arguments) {
                SubArg current = new SubArg(argument, (CommandAction<S>) null);
                if (top == null) top = current;
                if (arg != null) {
                    arg.children.add(current);
                }
                arg = current;
            }
            arg.action = action;
            if (description != null) arg.description = description;
            top.compile(new ArrayList<>());
            return this;
        }

        public CommandImpl arg(Argument<?> argument, CommandBiAction<S> action) {
            return arg(null, argument, action);
        }

        public CommandImpl arg(Description description, Argument<?> argument, CommandBiAction<S> action) {
            SubArg top = new SubArg(argument, action);
            top.description = description;
            top.compile(new ArrayList<>());
            return this;
        }

        public final CommandImpl onException(@Nullable BiFunction<S, Throwable, Boolean> errorFunction) {
            Commander.this.error = errorFunction;
            return this;
        }

    }

    public class SubArg {
        public final Argument<?> argument;
        public final List<SubArg> children = new LinkedList<>();
        public CommandAction<S> action;
        public Description description = null;

        public SubArg(Argument<?> argument, CommandAction<S> action) {
            this.argument = argument;
            this.action = action;
        }

        public SubArg(Argument<?> argument, Description description, CommandAction<S> action) {
            this.argument = argument;
            this.action = action;
            this.description = description;
        }

        @SafeVarargs
        public SubArg(Argument<?> argument, CommandAction<S> action, SubArg... children) {
            this.argument = argument;
            this.action = action;
            Collections.addAll(this.children, children);
        }

        @SafeVarargs
        public SubArg(Argument<?> argument, Description description, CommandAction<S> action, SubArg... children) {
            this.argument = argument;
            this.action = action;
            this.description = description;
            Collections.addAll(this.children, children);
        }

        @SafeVarargs
        public SubArg(Argument<?> argument, SubArg... children) {
            this.argument = argument;
            this.action = null;
            Collections.addAll(this.children, children);
        }

        public void compile(List<Argument<?>> list) {
            list.add(argument);
            if (action != null) {
                ArgumentEntry entry = new ArgumentEntry(list);
                if (description != null) entry.description = description.string;
                tree.put(entry, action);
            }
            if (!children.isEmpty()) {
                for (SubArg child : children) {
                    child.compile(new ArrayList<>(list));
                }
            }
        }
    }

    class ArgumentTree extends LinkedHashMap<ArgumentEntry, CommandAction<S>> {
    }

}
