import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.paukov.combinatorics3.Generator;

public class Automaton {

    /*
     * Symbol inner class
     */
    class Symbol {

        private String name;

        Symbol(final String name) {
            this.name = name;
            symbols.put(name, this);
        }

        @Override
        public String toString() {
            return "Sym[" + name + "]";
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

    }

    /*
     * Pair inner class
     */
    class Pair {
        Symbol symbol;
        State state;

        Pair(final Symbol symbol, final State state) {
            this.symbol = symbol;
            this.state = state;
        }

        State getState() {
            return this.state;
        }

        String getStateName() {
            return this.state.name;
        }

        Symbol getSymbol() {
            return this.symbol;
        }

        String getSymbolName() {
            return this.symbol.name;
        }

        @Override
        public String toString() {
            return "<" + symbol.name + "->" + state.name + ">";
        }
    }

    /*
     * State inner class
     */
    class State implements Comparable<State> {

        private final String name;
        List<Pair> transition;
        private Boolean accept = false;

        State(final String name) {
            this.name = name;
            this.transition = new ArrayList<>();
        }

        public void setTransition(final List<String> inputMap) {

            for (int i = 0; i < inputMap.size(); i += 2) {

                final Symbol tempSym = getSymbol(inputMap.get(i));
                final State tempState = getState(inputMap.get(i + 1));

                this.transition.add(0, new Pair(tempSym, tempState));
            }
        }

        public Stream<Pair> getTransitionStream() {
            return this.transition.stream();
        }

        @Override
        public String toString() {
            // return "S[" + name + ":" + accept + ":" + transition +"]";
            return "S[" + name + "]";
        }

        public String getName() {
            return name;
        }

        public Boolean isAccept() {
            return accept;
        }

        public void setAccept(final Boolean accept) {
            this.accept = accept;
        }

        public int compareTo(State o) {
            return shortFirst.compare(this.getName(), o.getName());
        }

        @Override
        public boolean equals(Object obj) {
            return (obj != null)
                    && this.getName().equals(((State) obj).getName());
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

    }

    /*
     * Constants
     */
    public static final String EPSILON = "ℇ";
    public static final String EMPTY = "∅";
    private static final String START = "start";
    private static final String UNION = "∪";
    private static final String STAR = "*";
    /*
     * Fields
     */
    // Automaton name
    private String name;
    // Map of states
    private Map<String, State> states;
    // Map of symbols
    private Map<String, Symbol> symbols;
    // Name of start state
    private State startState;
    // Name of accept states
    private Set<State> acceptStates;
    // Print legend
    private boolean withLegend = true;

    // Comparator for treemap sorting
    private static final Comparator<String> shortFirst = Comparator
            .comparing(String::length)
            .thenComparing(Comparator.naturalOrder());

    /*
     * Constructor
     */
    public Automaton(final String name) {

        this.name = name;
        this.states = new TreeMap<>(shortFirst);
        this.symbols = new TreeMap<>();
        this.symbols.put(EPSILON, new Symbol(EPSILON));
        this.acceptStates = new TreeSet<>();
    }
    /*
     * getters and setters
     */

    // Symbols
    public void addSymbol(final String symbol) {
        symbols.computeIfAbsent(symbol, Symbol::new);
    }

    public void addSymbol(final Collection<String> symbols) {
        symbols.forEach(this::addSymbol);
    }

    public Symbol getSymbol(final String name) {
        // Error checking
        if (symbols.get(name) == null) {
            System.err.println(
                    "WARNING: Symbol " + name + " not found, creating...");
            addSymbol(name);
        }
        return symbols.get(name);
    }

    public Stream<Symbol> getSymbolsStream() {
        return this.symbols.values().stream();
    }

    // States
    public void addState(final String state) {
        states.computeIfAbsent(state, State::new);
    }

    public void addState(final List<String> states) {
        states.forEach(this::addState);
    }

    public void addState(final State state) {
        this.addState(state.name);
    }

    public State getState(final String name) {
        final State found = states.get(name);
        // Error checking
        if (found == null)
            throw new RuntimeException("State " + name + " not found");
        return found;
    }

    public Stream<State> getStatesStream() {
        return this.states.values().stream();
    }

    public List<State> getStatesList() {
        return getStatesStream().collect(Collectors.toList());
    }

    public List<String> getStateNamesList() {
        return getStatesStream().map(State::getName)
                .collect(Collectors.toList());
    }

    // Acceptance
    public void setAccept(final String nameState) {
        this.setAccept(getState(nameState));
    }

    public void setAccept(final State state) {
        state.accept = true;
        this.acceptStates.add(state);
    }

    public void setAccept(final List<String> nameStates) {
        nameStates.forEach(this::setAccept);
    }

    public void setAcceptStates(final Collection<State> states) {
        states.forEach(this::setAccept);
    }

    public boolean isAccept(final State state) {
        return acceptStates.contains(state);
    }

    public boolean isAccept(final List<State> states) {
        return states.stream().anyMatch(this::isAccept);
    }

    public Stream<State> getAcceptStatesStream() {
        return this.states.values().stream().filter(State::isAccept);
    }

    public void clearAccept(State state) {
        state.accept = false;
    }

    public void clearAccept() {
        this.getAcceptStatesStream().forEach(this::clearAccept);
    }

    // Start state
    public void setStart(final State startState) {
        this.startState = startState;
    }

    public void setStart(final String startState) {
        this.setStart(this.getState(startState));
    }

    public State getStartState() {
        return this.startState;
    }

    // Legend related
    public boolean isWithLegend() {
        return withLegend;
    }

    public void setWithLegend(boolean withLegend) {
        this.withLegend = withLegend;
    }

    @Override
    public String toString() {
        return "Automaton [name=" + name + ", states=" + states + ", symbols="
                + symbols + "]";
    }

    /*
     * operations
     */
    // Get list of next states
    List<State> transition(final Symbol symbol, final State state) {

        return state.transition.stream()
                .filter(p -> p.symbol == symbol)
                .map(p -> p.state)
                .distinct()
                .collect(Collectors.toList());
    }

    // Get list of next states from a list of states
    List<State> transition(final Symbol symbol, final List<State> states) {

        return states.stream()
                .flatMap(s -> transition(symbol, s).stream())
                .sorted()
                .distinct()
                .collect(Collectors.toList());
    }

    // Apply epsilon-closure to a list of states
    List<State> epsilonClosure(final List<State> states) {

        final Stream<State> closured = states.stream()
                .flatMap(s -> transition(getSymbol(EPSILON), s).stream());
        return Stream.concat(closured, states.stream())
                .sorted()
                .distinct()
                .collect(Collectors.toList());
    }

    /*
     * Get a power set of the existing states
     * 
     * Returns a map with the name of the superstate and the associated
     * states
     */
    Map<String, List<State>> statesPowerSet() {

        // Generate power set
        final List<List<String>> powerSet = Generator
                .subset(this.getStateNamesList())
                .simple()
                .stream()
                .collect(Collectors.toList());

        // Create the new states
        final Map<String, List<State>> result = new TreeMap<>(shortFirst);
        for (final List<String> listEntry : powerSet)
            if (listEntry.isEmpty())
                result.put(EMPTY, List.of(new State(EMPTY)));
            else
                result.put(
                        // The key will be a superstate name
                        listEntry.stream()
                                .sorted(shortFirst)
                                .collect(Collectors.joining("")),
                        // The value will be a list of all composing states
                        listEntry.stream()
                                .map(this::getState)
                                .collect(Collectors.toList()));
        return result;
    }

    // Generate a NFA from a DFA
    public Automaton nfa2dfa() {

        // Set name
        final Automaton dfa = new Automaton(this.name + "→DFA");

        // Set symbols
        dfa.symbols = this.symbols;

        // Get the powerset
        final Map<String, List<State>> powerSet = this.statesPowerSet();

        // Add the states
        powerSet.keySet().stream().forEach(dfa::addState);

        /*
         * Get accept states traverse powerset and see if any outcome maps
         * to accept state, then add to new accept states
         * 
         * Note: not applying epsilon closure here
         */
        powerSet.entrySet()
                .stream()
                .filter(state -> state.getValue()
                        .stream()
                        // is any of the associated states in the previous
                        // accept states list?
                        .anyMatch(t -> this.acceptStates.contains(t)))
                .map(Map.Entry::getKey)
                .forEach(dfa::setAccept);

        // New transition function
        for (final Map.Entry<String, List<State>> entry : powerSet.entrySet()) {

            final List<State> statesList = entry.getValue()
                    .stream()
                    .collect(Collectors.toList());

            for (final Symbol symbol : symbols.values()) {
                final String dest = epsilonClosure(
                        transition(symbol, statesList)).stream()
                                .map(s -> s.name)
                                .sorted()
                                .collect(Collectors.joining(""));
                if (!symbol.name.equals(EPSILON)) {
                    if (dest.length() > 0)
                        dfa.getState(entry.getKey())
                                .setTransition(
                                        Arrays.asList(symbol.name, dest));
                    else
                        dfa.getState(entry.getKey())
                                .setTransition(
                                        Arrays.asList(symbol.name, EMPTY));
                }
            }
        }

        // Calculating new start
        dfa.setStart(epsilonClosure(List.of(this.startState)).stream()
                .map(s -> s.name)
                .collect(Collectors.joining("")));

        // DFA done!!!
        return dfa;
    }

    // All the outs from all states
    public List<State> allIncoming() {

        return this.states.values()
                .stream()
                .flatMap(s -> s.transition.stream()
                        .map(Automaton.Pair::getState)
                        .filter(trst -> !trst.name.equals(s.name)))
                .collect(Collectors.toList());
    }

    // Generate a simplified automaton
    public Automaton simplified() {

        // Set name
        final Automaton simplified = new Automaton(this.name + "→S");

        // Set symbols
        simplified.addSymbol(this.symbols.keySet());

        // Surviving states
        final List<State> surviving = allIncoming();

        // Add the states as they are, with the transition function already
        // inside
        surviving.stream().forEach(simplified::addState);

        /*
         * Get accept states traverse powerset and see if any outcome maps
         * to accept state, then add
         * 
         * Note: not applying epsilon-closure here
         */
        simplified.setAcceptStates(this.acceptStates.stream()
                .filter(surviving::contains)
                .collect(Collectors.toSet()));

        // Same start
        simplified.setStart(this.startState);

        // Simplified done!!
        return simplified;
    }

    /*
     * transforming the automaton
     * 
     */

    // Helper lambdas
    Comparator<Pair> byState = Comparator.comparing(Pair::getStateName)
            .thenComparing(Comparator.comparing(Pair::getSymbolName));
    Comparator<Pair> bySymbol = Comparator.comparing(Pair::getSymbolName)
            .thenComparing(Comparator.comparing(Pair::getStateName));

    // Factory
    public Automaton cloneExceptState(State ignore) {

        Automaton clone = new Automaton(this.name + "+");
        // Helpers
        Predicate<String> notIgnoreString = sn -> ignore == null
                || !sn.equals(ignore.getName());
        Predicate<State> notIgnoreState = st -> ignore == null
                || !st.getName().equals(ignore.getName());
        Predicate<Pair> notIgnorePair = pa -> ignore == null
                || !pa.state.getName().equals(ignore.getName());
        // cloning symbols
        this.getSymbolsStream().map(Symbol::getName).forEach(clone::addSymbol);
        // cloning states
        this.getStatesStream()
                .map(State::getName)
                .filter(notIgnoreString)
                .forEach(clone::addState);
        // setting same accepting states
        this.getAcceptStatesStream()
                .map(State::getName)
                .filter(notIgnoreString)
                .forEach(clone::setAccept);
        // adding transitions
        // Collector combiner
        BinaryOperator<List<Pair>> combine = (l1, l2) -> {
            l1.addAll(l2);
            return l2;
        };
        clone.getStatesStream()
                .filter(notIgnoreState)
                .forEach(s -> this.getState(s.getName())
                        .getTransitionStream()
                        .filter(notIgnorePair)
                        .collect(Collector.<Pair, List<Pair>>of(
                                () -> s.transition, List::add, combine,
                                Collector.Characteristics.IDENTITY_FINISH)));
        // Start state
        if (this.getStartState().equals(ignore))
            throw new RuntimeException("Cannot remove the start state!!");
        else
            clone.setStart(this.getStartState().getName());
        // Legend status
        clone.setWithLegend(this.isWithLegend());
        return clone;
    }

    public Automaton cloneExcept(String ignore) {
        return ignore.isBlank() ? this.cloneExceptState(null)
                : this.cloneExceptState(getState(ignore));
    }

    public Automaton cloner() {
        return this.cloneExceptState(null);
    }

    public void parallelUnion() {
        BinaryOperator<List<Pair>> combine = (l1, l2) -> {
            l1.addAll(l2);
            return l2;
        };
        Consumer<State> consumingState = s -> {
            // Join the transitions to same state
            Map<State, String> newTrans = s.getTransitionStream()
                    .collect(Collectors.groupingBy(Pair::getState,
                            Collectors.mapping(Pair::getSymbolName,
                                    Collectors.joining(Automaton.UNION))));
            // Update the transition function
            s.transition.clear();
            newTrans.entrySet()
                    .stream()
                    .map(e -> new Pair(getSymbol(e.getValue()), e.getKey()))
                    .collect(Collector.<Pair, List<Pair>>of(() -> s.transition,
                            List::add, combine,
                            Collector.Characteristics.IDENTITY_FINISH));
        };
        this.getStatesStream().forEach(consumingState);
    }

    public void arrangeForRE() {

        // New start
        if (hasArrowsIn(this.getStartState())) {
            this.addState("S");
            this.getState("S")
                    .setTransition(List.of(Automaton.EPSILON,
                            this.startState.getName()));
            this.setStart("S");
        }
        // New accept
        if (this.acceptStates.size() != 1
                || hasArrowsOut(acceptStates.iterator().next())
                || acceptStates.contains(this.getStartState())) {
            this.addState("a");
            this.getAcceptStatesStream()
                    .forEach(s -> s
                            .setTransition(List.of(Automaton.EPSILON, "a")));
            this.clearAccept();
            this.setAccept("a");
        }
        // Make union of equivalent paths
        this.parallelUnion();
    }

    // Return all states poiting in
    public List<Pair> arrowsIn(final State state) {

        final Predicate<Pair> isGoingToState = pair -> pair.state.equals(state);
        final Function<State, Stream<Pair>> extract = s -> s.transition.stream()
                .filter(isGoingToState)
                .map(tp -> new Pair(tp.symbol, s));
        final Predicate<Pair> notComingFromState = pair -> !pair.state
                .equals(state);
        return this.getStatesStream()
                .flatMap(extract)
                .filter(notComingFromState)
                .sorted(byState)
                .collect(Collectors.toList());
    }

    // Return states looping back
    public List<Pair> arrowsLoop(final State state) {

        final Predicate<Pair> isGoingToState = pair -> pair.state.equals(state);
        final Function<State, Stream<Pair>> extract = s -> s.transition.stream()
                .filter(isGoingToState)
                .map(tp -> new Pair(tp.symbol, s));
        final Predicate<Pair> comingFromItself = pair -> pair.state
                .equals(state);
        return this.getStatesStream()
                .flatMap(extract)
                .filter(comingFromItself)
                .sorted(byState)
                .collect(Collectors.toList());
    }

    // Does it have arrows pointing in? (or looping back)
    public boolean hasArrowsIn(final State state) {
        return !arrowsIn(state).isEmpty() || !arrowsLoop(state).isEmpty();
    }

    // Does it have arrows pointing out? (or looping back)
    public boolean hasArrowsOut(final State state) {
        return !arrowsOut(state).isEmpty() || !arrowsLoop(state).isEmpty();
    }

    // Return states this state is pointing to
    public List<Pair> arrowsOut(final State state) {

        final Predicate<Pair> notComingFromState = pair -> !pair.state
                .equals(state);
        return state.transition.stream()
                .filter(notComingFromState)
                .sorted(byState)
                .collect(Collectors.toList());
    }

    // Adding strings by removing epsilon and adding brackets
    private String symConcat(String sym1, String sym2) {

        Predicate<String> requiresBrackets = s -> {
            String[] bits = s.split("[()]");
            String subs = bits.length > 1
                    ? bits[0].concat(bits[bits.length - 1])
                    : bits[0];
            return subs.contains(Automaton.UNION) && !(s.startsWith("(")
                    && (s.endsWith(")") || s.endsWith(")*")));
        };
        if (sym1.equals(Automaton.EPSILON))
            return sym2;
        else if (sym2.equals(Automaton.EPSILON))
            return sym1;
        else {
            if (requiresBrackets.test(sym1))
                sym1 = "(" + sym1 + ")";
            if (requiresBrackets.test(sym2))
                sym2 = "(" + sym2 + ")";
            return sym1.concat(sym2);
        }
    }

    // Make the connections around a state
    public void makeRoutesAround(final State state, Automaton destination) {

        List<Pair> arrowsIn = this.arrowsIn(state);
        List<Pair> arrowsOut = this.arrowsOut(state);
        List<Pair> arrowsLoop = this.arrowsLoop(state);
        BiConsumer<Pair, Pair> biconsumer = (in, out) -> {
            String sym = in.getSymbolName();
            if (!arrowsLoop.isEmpty())
                if (arrowsLoop.get(0).getSymbolName().length() > 1)
                    sym = symConcat(sym, "(" + arrowsLoop.get(0).getSymbolName()
                            + ")" + Automaton.STAR);
                else
                    sym = symConcat(sym,
                            arrowsLoop.get(0).getSymbolName() + Automaton.STAR);
            sym = symConcat(sym, out.getSymbolName());
            destination.addSymbol(sym);
            destination.getState(in.getState().getName())
                    .setTransition(List.of(sym, out.getState().getName()));
        };
        arrowsIn.forEach(
                in -> arrowsOut.forEach(out -> biconsumer.accept(in, out)));
    }

    // Get the next step towards a RE
    public Automaton nextStepRE() {

        Predicate<State> notSorA = nSA -> !(nSA.equals(getStartState())
                || getAcceptStatesStream().collect(Collectors.toList())
                        .contains(nSA));

        State toRemove = this.getStatesStream()
                .filter(notSorA)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "No states left to remove!!"));

        Automaton nextStep = this.cloneExceptState(toRemove);
        this.makeRoutesAround(toRemove, nextStep);
        nextStep.parallelUnion();
        return nextStep;
    }

    /*
     * presentation block
     */

    // Proper formatting of a collection of states
    private String presentStates(final Collection<State> states) {
        // Transform to a list of names and call presentation
        return presentStatesFromNames(
                states.stream().map(s -> s.name).collect(Collectors.toList()));
    }

    // Proper formatting of a set of states from names
    private String presentStatesFromNames(final List<String> stateNames) {
        if (stateNames.isEmpty())
            return EMPTY;
        else if (stateNames.size() == 1)
            return toSub(stateNames.get(0));
        else
            return stateNames.stream()
                    .map(this::toSub)
                    .collect(Collectors.joining(", ", "{", "}"));
    }

    // Convert figures to subscript
    private String toSub(final String input) {
        return input.replaceAll("([0-9]+)",
                "<sub><font point-size=\"9\">$1</font></sub>");
    }

    /*
     * format graphs for graphviz
     */
    // Graph the nodes inventory and description
    private String graphStates2Nodes() {
        final StringBuilder result = new StringBuilder();
        result.append("\nrankdir=LR\n\n");
        result.append("node [shape = point width=0]" + Automaton.START + "\n");

        for (final State s : this.getStatesList()) {
            result.append("node ");
            if (Boolean.TRUE.equals(s.accept))
                result.append("[shape = doublecircle width=0.8 height=0.8 ]");
            else
                result.append("[shape = circle width=0.8 height=0.8 ]");

            result.append(s.name + "\n");
            result.append(s.name + "[label=<" + toSub(s.name)
                    + "> fixedsize=shape ]\n");
        }
        return result.toString();
    }

    // For a given state, return the transitions as graphviz links
    private String graphStateTransitions2Links(final String originState,
            final List<Pair> transitionsFromState) {
        // No transitions out
        if (transitionsFromState == null || transitionsFromState.isEmpty())
            return "";
        // Group the transitions from origin state to same destination
        // state together
        final Map<State, List<Pair>> set = transitionsFromState.stream()
                .collect(Collectors.groupingBy(Pair::getState));
        // Group the symbols together and separate with commas
        final StringBuilder result = new StringBuilder();
        set.keySet()
                .stream()
                .sorted((pair1, pair2) -> shortFirst.compare(pair1.name,
                        pair2.name))
                .forEach(pair -> result.append(originState + " -> " + pair.name
                        + " [ label = \""
                        + set.get(pair)
                                .stream()
                                .map(p -> p.symbol.name)
                                .collect(Collectors.joining(","))
                        + "\" ]\n"));
        return result.toString();
    }

    // Graph the whole links block
    private String graphTransitions2Links() {

        final StringBuilder result = new StringBuilder("\n");
        result.append(Automaton.START + " -> " + this.startState.name + "\n");

        for (final State s : states.values()) {
            // Special case for fake START state
            // if (s.name.equals(START))
            // result.append(s.name + " -> " +
            // s.transition.get(0).state.name
            // + "\n");
            // else
            result.append(graphStateTransitions2Links(s.name, s.transition));
        }
        return result.toString();
    }

    // Format table row
    private String tr(final String input) {
        return "<TR>" + input + "</TR>\n";
    }

    // Format table cell
    private String td(final String input) {
        return "<TD>" + input + "</TD>";
    }

    private String td(final String input, final int colspan) {
        return "<TD COLSPAN=\"" + colspan + "\">" + input + "</TD>";
    }

    // Format bold
    private String b(final String input) {
        return "<B>" + input + "</B>";
    }

    // Format italics
    private String i(final String input) {
        return "<I>" + input + "</I>";
    }

    // Format table
    private String tbl(final String input) {
        return "<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">\n"
                + input + "</TABLE>\n";
    }

    // Formats the full legend as HTML table
    private String table() {
        final StringBuilder result = new StringBuilder();
        final int colspan = this.symbols.size() + 1; // number of columns
                                                     // because
        // of symbols

        // Name
        result.append(tr(td(b(this.name.toUpperCase()), colspan)));

        // States
        // List of states without START
        final List<State> cleanStates = this.states.values()
                .stream()
                .filter(state -> !state.name.equals(START))
                .collect(Collectors.toCollection(ArrayList<State>::new));

        result.append(tr(td(i(b("States")), colspan)));
        if (cleanStates.size() < 10)
            result.append(tr(td(presentStates(cleanStates), colspan)));
        else
            result.append(tr(td(states.size() + " states", colspan)));
        // Transition function
        result.append(tr(td(i(b("Transition function")), colspan)));
        // Symbols row
        result.append(tr(td("δ") + this.symbols.keySet()
                .stream()
                .map(this::b)
                .map(this::td)
                .collect(Collectors.joining())));
        /*
         * stream through states, for each state do the following:
         * 
         * 1. do a row start with the state name in td 2. stream the
         * symbols 3. get the transitions 4. format them & put in td 5.
         * collect them in a string 6. add the start of row with the rest
         * of row 7. put in tr 8. append to result
         */
        cleanStates.stream()
                .map(state -> tr(td(b(toSub(state.name))) + this.symbols
                        .values()
                        .stream()
                        .map(symbol -> td(
                                presentStates(transition(symbol, state))))
                        .collect(Collectors.joining(""))))
                .forEach(result::append);
        // Name of start state
        // String nameStartState;
        // Name of accept states
        // List<String> nameAcceptStates;
        // Start State
        result.append(tr(td(i(b("Start state: ")) + toSub(this.startState.name),
                colspan)));

        // Accept States
        if (this.acceptStates.size() < 10)
            result.append(tr(td(
                    i(b("Accept states: ")) + presentStates(this.acceptStates),
                    colspan)));
        else
            result.append(
                    tr(td(i(b("Accept states: ")) + this.acceptStates.size(),
                            colspan)));

        return tbl(result.toString());
    }

    // Sets a subgraph with the legend
    private String graphLegend() {
        final StringBuilder result = new StringBuilder();
        result.append("\n { \nrank = sink; \n");
        result.append("Legend [shape=none, margin=0, label=<\n");
        result.append(table());
        result.append("\n>];\n}\n");
        return result.toString();
    }

    // Renders an automaton
    public void render() {
        final GraphViz gv = new GraphViz();
        gv.addln(gv.startGraph());
        gv.add(this.graphStates2Nodes());
        gv.add(this.graphTransitions2Links());
        if (this.withLegend)
            gv.add(this.graphLegend());
        gv.addln(gv.endGraph());
        final String type = "svg";
        final File out = new File(this.name + "." + type);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
    }

}