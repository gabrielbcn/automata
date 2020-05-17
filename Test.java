import java.util.Arrays;

public class Test {

    public static void main(String... args) {

        /*
         * creating a NFA
         */
        Automaton nfa = new Automaton("NFA7");
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

    }

}