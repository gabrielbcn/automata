import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.paukov.combinatorics3.Generator;

public class Automaton {

    /*
     * Symbol inner class
     */
    class Symbol {

        String name;

        Symbol(String name) {
            this.name = name;
            symbols.put(name, this);
        }

        @Override
        public String toString() {
            return "Sym[" + name + "]";
        }

    }

    /*
     * Pair inner class
     */
    class Pair {
        Symbol symbol;
        State state;

        Pair(Symbol symbol, State state) {
            this.symbol = symbol;
            this.state = state;
        }

        State getState() {
            return this.state;
        }

        @Override
        public String toString() {
            return "<" + symbol.name + "->" + state.name + ">";
        }
    }

    /*
     * State inner class
     */
    class State {
        String name;
        List<Pair> transition;
        Boolean accept = false;

        State(String name) {
            this.name = name;
            this.transition = new ArrayList<>();
        }

        public void setTransition(List<String> inputMap) {

            for (int i = 0; i < inputMap.size(); i += 2) {

                Symbol tempSym = getSymbol(inputMap.get(i));
                State tempState = getState(inputMap.get(i + 1));

                this.transition.add(new Pair(tempSym, tempState));
            }
        }

        @Override
        public String toString() {
            return "State[" + name + ":" + accept + ":" + transition + "]";
        }

        public String getName() {
            return name;
        }

    }

    /*
     * Constants
     */
    public static final String EPSILON = "ℇ";
    public static final String EMPTY = "∅";
    private static final String START = "start";
    // To sort TreeMap
    private static final Comparator<String> shortFirst = Comparator
            .comparing(String::length)
            .thenComparing(String::compareTo);
    /*
     * Fields
     */
    // Automaton name
    String name;
    // Map of states
    Map<String, State> states;
    // Map of symbols
    Map<String, Symbol> symbols;
    // Name of start state
    String nameStartState;
    // Name of accept states
    Set<String> nameAcceptStates;

    /*
     * Constructor
     */
    public Automaton(String name) {

        this.name = name;

        states = new TreeMap<>(shortFirst);
        states.put(START, new State(START));

        symbols = new TreeMap<>();
        symbols.put(EPSILON, new Symbol(EPSILON));

        nameAcceptStates = new TreeSet<>(shortFirst);
    }

    /*
     * getters and setters
     */
    public void addSymbol(String symbol) {
        symbols.put(symbol, new Symbol(symbol));
    }

    public void addSymbol(List<String> symbols) {
        for (String s : symbols)
            addSymbol(s);
    }

    public Symbol getSymbol(String name) {
        Symbol found = symbols.get(name);
        // Error checking
        if (found == null)
            throw new RuntimeException("Symbol " + name + " not found");
        return found;
    }

    public void addState(String state) {
        states.put(state, new State(state));
    }

    public void addState(List<String> states) {
        for (String s : states)
            addState(s);
    }

    public void addState(State state) {
        states.put(state.name, state);
    }

    public State getState(String name) {
        State found = states.get(name);
        // Error checking
        if (found == null)
            throw new RuntimeException("State " + name + " not found");
        return found;
    }

    public void setAccept(String nameState) {
        State toChange = states.get(nameState);
        toChange.accept = true;
        nameAcceptStates.add(nameState);
    }

    public void setAccept(State state) {
        state.accept = true;
        nameAcceptStates.add(state.name);
    }

    public void setAcceptStateNames(List<String> nameStates) {
        nameStates.forEach(this::setAccept);
    }

    public void setAcceptStates(List<State> states) {
        states.forEach(this::setAccept);
    }

    public void setStart(String startState) {
        getState(START).setTransition(Arrays.asList(EPSILON, startState));
        this.nameStartState = startState;
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
    List<State> transition(Symbol symbol, State state) {
        return state.transition.stream()
                .filter(p -> p.symbol == symbol)
                .map(p -> p.state)
                .collect(Collectors.toList());
    }

    // Get list of next states from a list of states
    List<State> transition(Symbol symbol, List<State> states) {
        return states.stream()
                .flatMap(s -> transition(symbol, s).stream())
                .sorted(Comparator.comparing(State::getName))
                .distinct()
                .collect(Collectors.toList());
    }

    // Apply epsilon-closure to a list of states
    List<State> epsilonClosure(List<State> states) {
        Stream<State> closured = states.stream()
                .flatMap(s -> transition(getSymbol(EPSILON), s).stream());
        return Stream.concat(closured, states.stream())
                .sorted((s1, s2) -> s1.name.compareTo(s2.name))
                .distinct()
                .collect(Collectors.toList());
    }

    // Get a power set of the existing states
    Map<String, List<State>> statesPowerSet() {

        // Clean fake state start
        List<String> statesToCombine = new ArrayList<>();
        for (Map.Entry<String, State> s : this.states.entrySet())
            if (!s.getKey().equals(START))
                statesToCombine.add(s.getValue().name);

        // Generate power set
        List<List<String>> powerSet = Generator.subset(statesToCombine)
                .simple()
                .stream()
                .collect(Collectors.toList());

        // Create the new states
        Map<String, List<State>> result = new TreeMap<>();
        for (List<String> set : powerSet)
            if (set.isEmpty())
                result.put(EMPTY, Arrays.asList(new State(EMPTY)));
            else
                result.put(set.stream().collect(Collectors.joining("")),
                        set.stream()
                                .map(this::getState)
                                .collect(Collectors.toList()));
        return result;
    }

    // Generate a NFA from a DFA
    public Automaton nfa2dfa() {

        // Set name
        Automaton dfa = new Automaton(this.name + "→DFA");

        // Set symbols
        dfa.symbols = this.symbols;

        // Get the powerset and add the states
        Map<String, List<State>> powerSet = this.statesPowerSet();
        powerSet.keySet().stream().forEach(dfa::addState);

        /*
         * Get accept states traverse powerset, apply epsilon closure to
         * list of associated states and see if any outcome maps to accept
         * state, then add
         */
        powerSet.entrySet()
                .stream()
                .filter(state -> state.getValue()
                        .stream()
                        .anyMatch(t -> this.nameAcceptStates.contains(t.name)))
                .map(Map.Entry::getKey)
                .forEach(dfa::setAccept);

        // New transition function
        for (Map.Entry<String, List<State>> entry : powerSet.entrySet()) {

            List<State> statesList = entry.getValue()
                    .stream()
                    .collect(Collectors.toList());

            for (Symbol symbol : symbols.values()) {
                String dest = epsilonClosure(transition(symbol, statesList))
                        .stream()
                        .map(s -> s.name)
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
        dfa.setStart(epsilonClosure(
                Arrays.asList(this.getState(this.nameStartState))).stream()
                        .map(s -> s.name)
                        .collect(Collectors.joining("")));

        // DFA done!!!
        return dfa;
    }

    // All the outs from a state
    List<State> allIncoming() {

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
        Automaton simplified = new Automaton(this.name + "→S");

        // Set symbols
        simplified.symbols = this.symbols;

        // Surviving states
        List<State> surviving = allIncoming();
        List<String> survivingNames = surviving.stream()
                .map(Automaton.State::getName)
                .collect(Collectors.toList());

        // Add the states as they are, with the transition function already
        // inside
        surviving.stream().forEach(simplified::addState);

        /*
         * Get accept states traverse powerset, apply epsilon closure to
         * list of associated states and see if any outcome maps to accept
         * state, then add
         */
        simplified.nameAcceptStates = this.nameAcceptStates.stream()
                .filter(survivingNames::contains)
                .collect(Collectors.toSet());

        // Same start
        simplified.setStart(this.nameStartState);

        // Simplified done!!
        return simplified;
    }

    /*
     * presentation block
     */

    // Proper formatting of a list of states
    private String presentStates(List<State> states) {
        // Transform to a list of names and call presentation
        return presentStatesFromNames(
                states.stream().map(s -> s.name).collect(Collectors.toList()));
    }

    // Proper formatting of a set of states from names
    private String presentStatesFromNames(List<String> stateNames) {
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
    private String toSub(String input) {
        return input.replaceAll("([0-9]+)",
                "<sub><font point-size=\"9\">$1</font></sub>");
    }

    /*
     * format graphs for graphviz
     */
    // Graph the nodes inventory and description
    private String graphStates2Nodes() {
        StringBuilder result = new StringBuilder();
        result.append("\nrankdir=LR\n\n");
        for (State s : states.values()) {
            result.append("node ");
            if (s.name.equals(START))
                result.append("[shape = point width=0]");
            else if (Boolean.TRUE.equals(s.accept))
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
    private String graphStateTransitions2Links(String originState,
            List<Pair> transitionsFromState) {
        // Error checking
        if (transitionsFromState == null || transitionsFromState.isEmpty()
                || transitionsFromState.get(0).symbol == null)
            throw new RuntimeException(
                    "Node links printout called on null, empty or null element list");
        // Group the transitions from origin state to same destination
        // state together
        Map<State, List<Pair>> set = transitionsFromState.stream()
                .collect(Collectors.groupingBy(Pair::getState));
        // Group the symbols together and separate with commas
        StringBuilder result = new StringBuilder();
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
        StringBuilder result = new StringBuilder("\n");
        for (State s : states.values()) {
            // Special case for fake START state
            if (s.name.equals(START))
                result.append(s.name + " -> " + s.transition.get(0).state.name
                        + "\n");
            else
                result.append(
                        graphStateTransitions2Links(s.name, s.transition));
        }
        return result.toString();
    }

    // Format table row
    private String tr(String input) {
        return "<TR>" + input + "</TR>\n";
    }

    // Format table cell
    private String td(String input) {
        return "<TD>" + input + "</TD>";
    }

    private String td(String input, int colspan) {
        return "<TD COLSPAN=\"" + colspan + "\">" + input + "</TD>";
    }

    // Format bold
    private String b(String input) {
        return "<B>" + input + "</B>";
    }

    // Format italics
    private String i(String input) {
        return "<I>" + input + "</I>";
    }

    // Format table
    private String tbl(String input) {
        return "<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">\n"
                + input + "</TABLE>\n";
    }

    // Formats the full legend as HTML table
    private String table() {
        StringBuilder result = new StringBuilder();
        int colspan = this.symbols.size() + 1; // number of columns because
                                               // of symbols

        // Name
        result.append(tr(td(b(this.name.toUpperCase()), colspan)));

        // States
        // List of states without START
        List<State> cleanStates = this.states.values()
                .stream()
                .filter(state -> !state.name.equals(START))
                .collect(Collectors.toCollection(ArrayList<State>::new));
        result.append(tr(td(i(b("States")), colspan)));
        result.append(tr(td(presentStates(cleanStates), colspan)));

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
        result.append(tr(td(i(b("Start state: ")) + toSub(this.nameStartState),
                colspan)));

        // Accept States
        result.append(tr(td(i(b("Accept states: "))
                + presentStates(this.nameAcceptStates.stream()
                        .map(this::getState)
                        .collect(Collectors.toList())),
                colspan)));

        return tbl(result.toString());
    }

    // Sets a subgraph with the legend
    private String graphLegend() {
        StringBuilder result = new StringBuilder();
        result.append("\n { \nrank = sink; \n");
        result.append("Legend [shape=none, margin=0, label=<\n");
        result.append(table());
        result.append("\n>];\n}\n");
        return result.toString();
    }

    // Renders an automaton
    public void render() {
        GraphViz gv = new GraphViz();
        gv.addln(gv.startGraph());
        gv.add(this.graphStates2Nodes());
        gv.add(this.graphTransitions2Links());
        gv.add(this.graphLegend());
        gv.addln(gv.endGraph());
        String type = "svg";
        File out = new File(this.name + "." + type);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
    }

}