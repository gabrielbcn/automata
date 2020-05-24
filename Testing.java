
import java.util.List;
import java.util.function.Consumer;

public class Testing {

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

    static void NFA168() {

        Automaton nfa1 = new Automaton("NFA1.68Sipser");
        nfa1.setWithLegend(false);

        nfa1.addSymbol(List.of("a", "b")); // Symbols
        nfa1.addState(List.of("q1", "q2", "q3")); // States
        // Transitions
        nfa1.getState("q1").setTransition(List.of("a", "q2", "b", "q3"));
        nfa1.getState("q2").setTransition(List.of("a", "q1", "b", "q2"));
        nfa1.getState("q3").setTransition(List.of("a", "q2", "b", "q3"));
        nfa1.setAccept(List.of("q2", "q3")); // Accept states
        nfa1.setStart("q1"); // Start
        nfa1.render(); // Show

        Automaton nfa2 = new Automaton("NFA1.68Sipser-2");
        nfa2.addSymbol(List.of("a", "b")); // Symbols
        nfa2.addState(List.of("s", "q1", "q2", "q3", "e")); // States
        // Transitions
        nfa2.getState("s").setTransition(List.of(Automaton.EPSILON, "q1"));
        nfa2.getState("q1").setTransition(List.of("a", "q2", "b", "q3"));
        nfa2.getState("q2")
                .setTransition(
                        List.of("a", "q1", "b", "q2", Automaton.EPSILON, "e"));
        nfa2.getState("q3")
                .setTransition(
                        List.of("b", "q1", "a", "q2", Automaton.EPSILON, "e"));
        nfa2.setAccept(List.of("e")); // Accept states
        nfa2.setStart("s"); // Start
        nfa2.render(); // Show

        Automaton nfa3 = new Automaton("NFA1.68Sipser-3");
        nfa3.addSymbol(List.of("a", "b")); // Symbols
        nfa3.addState(List.of("s", "q2", "q3", "e")); // States
        // Transitions
        nfa3.getState("s").setTransition(List.of("a", "q2", "b", "q3"));
        nfa3.getState("q2")
                .setTransition(List.of(Automaton.EPSILON, "e", "aa⋃b", "q2",
                        "ab", "q3"));
        nfa3.getState("q3")
                .setTransition(List.of("ba⋃a", "q2", "bb", "q3",
                        Automaton.EPSILON, "e"));
        nfa3.setAccept(List.of("e")); // Accept states
        nfa3.setStart("s"); // Start
        nfa3.render(); // Show

        Automaton nfa4 = new Automaton("NFA1.68Sipser-4");
        nfa4.addSymbol(List.of("a", "b")); // Symbols
        nfa4.addState(List.of("s", "q3", "e")); // States
        // Transitions
        nfa4.getState("s")
                .setTransition(List.of("a(aa⋃b)*ab⋃b", "q3", "a(aa⋃b)*", "e"));
        nfa4.getState("q3")
                .setTransition(List.of("(ba⋃a)(aa⋃b)*ab⋃bb", "q3",
                        "(ba⋃a)(aa⋃b)*⋃ℇ", "e"));
        nfa4.setAccept(List.of("e")); // Accept states
        nfa4.setStart("s"); // Start
        nfa4.render(); // Show

        Automaton nfa5 = new Automaton("NFA1.68Sipser-5");
        nfa5.addSymbol(List.of("a", "b")); // Symbols
        nfa5.addState(List.of("s", "e")); // States
        // Transitions
        nfa5.getState("s")
                .setTransition(List.of(
                        "(a(aa⋃b)*ab⋃b)((ba⋃a)(aa⋃b)*ab⋃bb)*((ba⋃a)(aa⋃b)*Uℇ)⋃a(aa⋃b)*",
                        "e"));
        nfa5.setAccept(List.of("e")); // Accept states
        nfa5.setStart("s"); // Start
        nfa5.render(); // Show
    }

    static void NFA168b() {

        Automaton nfa1 = new Automaton("NFA1.68Sipser");
        nfa1.setWithLegend(false);

        nfa1.addSymbol(List.of("a", "b")); // Symbols
        nfa1.addState(List.of("q1", "q2", "q3")); // States
        // Transitions
        nfa1.getState("q1").setTransition(List.of("a", "q2", "b", "q3"));
        nfa1.getState("q2").setTransition(List.of("a", "q1", "b", "q2"));
        nfa1.getState("q3")
                .setTransition(List.of("a", "q2", "b", "q2", "b", "q3"));
        nfa1.setAccept(List.of("q2", "q3")); // Accept states
        nfa1.setStart("q1"); // Start
        nfa1.render(); // Show

        System.err.println(nfa1.getStatesList());
        Consumer<Automaton.State> inLoopOut1 = state -> {
            System.err.println("State = " + state);
            System.err.println(nfa1.arrowsIn(state));
            System.err.println(nfa1.arrowsLoop(state));
            System.err.println(nfa1.arrowsOut(state));
        };
        nfa1.getStatesStream().forEach(inLoopOut1);

        Automaton nfa2 = nfa1.cloneExcept("");
        Consumer<Automaton.State> inLoopOut2 = state -> {
            System.err.println("State = " + state);
            System.err.println(nfa2.arrowsIn(state));
            System.err.println(nfa2.arrowsLoop(state));
            System.err.println(nfa2.arrowsOut(state));
        };
        System.err.println(nfa2.getStatesList());
        nfa2.getStatesStream().forEach(inLoopOut2);

        nfa2.render();

    }

    public static void main(String... args) {
        NFA168b();
    }

}