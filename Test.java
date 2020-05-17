import java.util.Arrays;

public class Test {

    static void NFA1() {
        /*
         * NFA3 TO DFA
         */
        Automaton nfa = new Automaton("NFA1");
        // Symbols
        nfa.addSymbol(Arrays.asList("a", "b"));
        // States
        nfa.addState(Arrays.asList("q0", "q1"));
        // Transitions
        nfa.getState("q0")
                .setTransition(Arrays.asList("a", "q0", "a", "q1", "b", "q1"));
        nfa.getState("q1").setTransition(Arrays.asList("b", "q0"));
        // Accept states
        nfa.setAccept("q0");
        // Start
        nfa.setStart("q0");
        nfa.render(); // Show
        /*
         * creating the corresponding DFA
         */
        Automaton dfa = nfa.nfa2dfa();
        dfa.render(); // Show
    }

    static void NFA2() {
        /*
         * NFA3 TO DFA
         */
        Automaton nfa = new Automaton("NFA2");
        // Symbols
        nfa.addSymbol(Arrays.asList("a", "b"));
        // States
        nfa.addState(Arrays.asList("q0", "q1", "q2"));
        // Transitions
        nfa.getState("q0")
                .setTransition(
                        Arrays.asList("a", "q2", Automaton.EPSILON, "q1"));
        nfa.getState("q1").setTransition(Arrays.asList("a", "q0"));
        nfa.getState("q2")
                .setTransition(Arrays.asList("a", "q1", "b", "q1", "b", "q2"));
        // Accept states
        nfa.setAccept("q1");
        // Start
        nfa.setStart("q0");
        nfa.render(); // Show
        /*
         * creating the corresponding DFA
         */
        Automaton dfa = nfa.nfa2dfa();
        dfa.render(); // Show
        /*
         * Simplified DFA
         */
        Automaton dfa2 = dfa.simplified();
        dfa2.render(); // Show
        /*
         * Simplified DFA
         */
        Automaton dfa3 = dfa2.simplified();
        dfa3.render(); // Show
        /*
         * Simplified DFA
         */
        Automaton dfa4 = dfa3.simplified();
        dfa4.render(); // Show
    }

    static void NFA3() {
        /*
         * NFA3 TO DFA
         */
        Automaton nfa = new Automaton("NFA3");
        // Symbols
        nfa.addSymbol(Arrays.asList("0", "1"));
        // States
        nfa.addState(Arrays.asList("q0", "q1", "q2"));
        // Transitions
        nfa.getState("q0").setTransition(Arrays.asList("0", "q1", "1", "q1"));
        nfa.getState("q1")
                .setTransition(Arrays.asList("0", "q0", "0", "q2", "1", "q1",
                        Automaton.EPSILON, "q2"));
        nfa.getState("q2").setTransition(Arrays.asList("1", "q1"));
        // Accept states
        nfa.setAccept("q1");
        // Start
        nfa.setStart("q0");
        nfa.render(); // Show
        /*
         * creating the corresponding DFA
         */
        Automaton dfa = nfa.nfa2dfa();
        dfa.render(); // Show
        /*
         * Simplified DFA
         */
        Automaton dfa2 = dfa.simplified();
        dfa2.render(); // Show
        /*
         * Simplified DFA
         */
        Automaton dfa3 = dfa2.simplified();
        dfa3.render(); // Show
    }

    public static void main(String... args) {

        NFA3();
    }

}