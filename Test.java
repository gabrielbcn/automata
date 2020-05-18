import java.util.List;

public class Test {

    static void NFA1() {
        /*
         * NFA1 TO DFA
         */
        Automaton nfa = new Automaton("NFA1");
        // Symbols
        nfa.addSymbol(List.of("a", "b"));
        // States
        nfa.addState(List.of("q0", "q1"));
        // Transitions
        nfa.getState("q0")
                .setTransition(List.of("a", "q0", "a", "q1", "b", "q1"));
        nfa.getState("q1").setTransition(List.of("b", "q0"));
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

        LanguageTree.reproduce(nfa, 7);
        LanguageTree.reproduce(dfa, 7);
    }

    static void NFA2() {
        /*
         * NFA2 TO DFA
         */
        Automaton nfa = new Automaton("NFA2");
        // Symbols
        nfa.addSymbol(List.of("a", "b"));
        // States
        nfa.addState(List.of("q0", "q1", "q2"));
        // Transitions
        nfa.getState("q0")
                .setTransition(List.of("a", "q2", Automaton.EPSILON, "q1"));
        nfa.getState("q1").setTransition(List.of("a", "q0"));
        nfa.getState("q2")
                .setTransition(List.of("a", "q1", "b", "q1", "b", "q2"));
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

        LanguageTree.reproduce(nfa, 7);
        LanguageTree.reproduce(dfa, 7);
    }

    static void NFA3() {
        /*
         * NFA3 TO DFA
         */
        Automaton nfa = new Automaton("NFA3");
        // Symbols
        nfa.addSymbol(List.of("0", "1"));
        // States
        nfa.addState(List.of("q0", "q1", "q2"));
        // Transitions
        nfa.getState("q0").setTransition(List.of("0", "q1", "1", "q1"));
        nfa.getState("q1")
                .setTransition(List.of("0", "q0", "0", "q2", "1", "q1",
                        Automaton.EPSILON, "q2"));
        nfa.getState("q2").setTransition(List.of("1", "q1"));
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

        LanguageTree.reproduce(nfa, 7);
        LanguageTree.reproduce(dfa3, 7);
    }

    static void NFA4() {
        /*
         * NFA4 TO DFA
         */
        Automaton nfa = new Automaton("NFA4");
        // Symbols
        nfa.addSymbol(List.of("1"));
        // States
        nfa.addState(List.of("s", "q1", "q2", "r1", "r2", "r3"));
        // Transitions
        nfa.getState("s")
                .setTransition(List.of(Automaton.EPSILON, "q1",
                        Automaton.EPSILON, "r1"));
        nfa.getState("q1").setTransition(List.of("1", "q2"));
        nfa.getState("q2").setTransition(List.of("1", "q1"));
        nfa.getState("r1").setTransition(List.of("1", "r2"));
        nfa.getState("r2").setTransition(List.of("1", "r3"));
        nfa.getState("r3").setTransition(List.of("1", "r1"));
        // Accept states
        nfa.setAccept(List.of("q1", "r1"));
        // Start
        nfa.setStart("s");
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

        LanguageTree.reproduce(nfa, 15);
        LanguageTree.reproduce(dfa3, 15);
    }

    public static void main(String... args) {

        NFA1();

    }

}