
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
        nfa1.getState("q3").setTransition(List.of("a", "q2", "b", "q1"));
        nfa1.setAccept(List.of("q2", "q3")); // Accept states
        nfa1.setStart("q1"); // Start
        nfa1.render(); // Show

        Automaton nfa2 = nfa1.cloner();
        nfa2.arrangeForRE();
        nfa2.render();

        Automaton nfa3 = nfa2.nextStepRE();
        nfa3.render();

        Automaton nfa4 = nfa3.nextStepRE();
        nfa4.render();

        Automaton nfa5 = nfa4.nextStepRE();
        nfa5.render();
    }

    static void NFA167() {

        Automaton nfa1 = new Automaton("NFA1.67Sipser");
        nfa1.setWithLegend(false);

        nfa1.addSymbol(List.of("a", "b")); // Symbols
        nfa1.addState(List.of("q1", "q2")); // States
        // Transitions
        nfa1.getState("q1").setTransition(List.of("a", "q1", "b", "q2"));
        nfa1.getState("q2").setTransition(List.of("b", "q2", "a", "q2"));
        nfa1.setAccept(List.of("q2")); // Accept states
        nfa1.setStart("q1"); // Start
        nfa1.render(); // Show

        Automaton nfa2 = nfa1.cloner();
        nfa2.arrangeForRE();
        nfa2.render();

        Automaton nfa3 = nfa2.nextStepRE();
        nfa3.render();

        Automaton nfa4 = nfa3.nextStepRE();
        nfa4.render();

    }

    static void AssignQ3() {

        Automaton nfa1 = new Automaton("AssignQ3");
        nfa1.setWithLegend(false);

        nfa1.addSymbol(List.of("a", "b")); // Symbols
        nfa1.addState(List.of("s", "q1", "q2", "q3", "q4", "r1")); // States
        // Transitions
        nfa1.getState("s").setTransition(List.of("a", "r1", "b", "q1"));
        nfa1.getState("q1").setTransition(List.of("b", "q1", "a", "q2"));
        nfa1.getState("q2").setTransition(List.of("a", "r1", "b", "q3"));
        nfa1.getState("q3").setTransition(List.of("a", "q4", "b", "q1"));
        nfa1.getState("q4").setTransition(List.of("a", "q4", "b", "q4"));
        nfa1.getState("r1").setTransition(List.of("a", "r1", "b", "q1"));
        nfa1.setAccept(List.of("q4")); // Accept states
        nfa1.setStart("s"); // Start
        nfa1.render(); // Show

        Automaton nfa2 = nfa1.cloner();
        nfa2.arrangeForRE();
        nfa2.render();

        Automaton nfa3 = nfa2.nextStepRE();
        nfa3.render();

        Automaton nfa4 = nfa3.nextStepRE();
        nfa4.render();

        Automaton nfa5 = nfa4.nextStepRE();
        nfa5.render();

        Automaton nfa6 = nfa5.nextStepRE();
        nfa6.render();

        Automaton nfa7 = nfa6.nextStepRE();
        nfa7.render();
    }

    static void AssignQ6() {

        Automaton nfa1 = new Automaton("AssignQ6");
        nfa1.setWithLegend(false);

        nfa1.addSymbol(List.of("a", "b")); // Symbols
        nfa1.addState(List.of("s", "q1", "q2", "q3", "q4")); // States
        // Transitions
        nfa1.getState("s").setTransition(List.of("a", "q1", "b", "q1"));
        nfa1.getState("q1").setTransition(List.of("a", "q2", "b", "q2"));
        nfa1.getState("q2").setTransition(List.of("a", "q3", "b", "q3"));
        nfa1.getState("q3").setTransition(List.of("a", "q4", "b", "q4"));
        nfa1.getState("q4").setTransition(List.of("a", "q4", "b", "q4"));
        nfa1.setAccept(List.of("s", "q1", "q2", "q3")); // Accept states
        nfa1.setStart("s"); // Start
        nfa1.render(); // Show

        nfa1.makeRE();
        nfa1.makeAllREs();

    }

    static void Mel() {

        Automaton nfa1 = new Automaton("Mel");
        nfa1.setWithLegend(false);

        nfa1.addSymbol(List.of("0", "1")); // Symbols
        nfa1.addState(List.of("s1", "s2", "s3")); // States
        // Transitions
        nfa1.getState("s1").setTransition(List.of("0", "s3"));
        nfa1.getState("s2")
                .setTransition(
                        List.of("0", "s3", "1", "s1", Automaton.EPSILON, "s1"));
        nfa1.getState("s3").setTransition(List.of("1", "s2"));
        nfa1.setAccept(List.of("s3")); // Accept states
        nfa1.setStart("s1"); // Start

        nfa1.makeRE();
        nfa1.makeAllREs();
    }

    static void Sri() {

        Automaton nfa1 = new Automaton("Sri");
        nfa1.setWithLegend(false);

        nfa1.addSymbol(List.of("a", "b")); // Symbols
        nfa1.addState(List.of("q1", "q2", "q3")); // States
        // Transitions
        nfa1.getState("q1")
                .setTransition(List.of("a", "q1", "b", "q1", "a", "q2"));
        nfa1.getState("q2").setTransition(List.of("b", "q3"));
        nfa1.getState("q3").setTransition(List.of());
        nfa1.setAccept(List.of("q3")); // Accept states
        nfa1.setStart("q1"); // Start

        nfa1.makeRE();
        nfa1.makeAllREs();
    }

    static void Ex1() {

        Automaton nfa1 = new Automaton("Example1");
        nfa1.setWithLegend(false);

        nfa1.addSymbol(List.of("0", "1")); // Symbols
        nfa1.addState(List.of("q1", "q2", "q3")); // States
        // Transitions
        nfa1.getState("q1")
                .setTransition(List.of("0", "q1", "1", "q1", "1", "q2"));
        nfa1.getState("q2").setTransition(List.of("0", "q3"));
        nfa1.getState("q3").setTransition(List.of());
        nfa1.setAccept(List.of("q3")); // Accept states
        nfa1.setStart("q1"); // Start

        nfa1.makeRE();
        nfa1.makeAllREs(); // (0∪1)*10
    }

    static void Ex2() {

        Automaton nfa1 = new Automaton("Example1");
        nfa1.setWithLegend(false);

        nfa1.addSymbol(List.of("x")); // Symbols
        nfa1.addState(List.of("q0", "q1")); // States
        // Transitions
        nfa1.getState("q0").setTransition(List.of("x", "q1"));
        nfa1.getState("q1").setTransition(List.of("x", "q0"));
        nfa1.setAccept(List.of("q0")); // Accept states
        nfa1.setStart("q0"); // Start

        nfa1.makeRE();
        nfa1.makeAllREs(); // (xx)*
    }

    static void Odd0() {

        Automaton nfa1 = new Automaton("Odd0");
        nfa1.setWithLegend(false);

        nfa1.addSymbol(List.of("x")); // Symbols
        nfa1.addState(List.of("e0e1", "o0e1", "e0o1", "o0o1")); // States
        // Transitions
        nfa1.getState("e0e1").setTransition(List.of("0", "o0e1", "1", "e0o1"));
        nfa1.getState("o0e1").setTransition(List.of("0", "e0e1", "1", "o0o1"));
        nfa1.getState("e0o1").setTransition(List.of("0", "o0o1", "1", "e0e1"));
        nfa1.getState("o0o1").setTransition(List.of("0", "e0o1", "1", "o0e1"));
        nfa1.setAccept(List.of("o0e1", "o0o1")); // Accept states
        nfa1.setStart("e0e1"); // Start

        nfa1.render();
        nfa1.makeRE(List.of("o0e1", "e0o1", "o0o1", "e0e1"));
    }

    static void Odd0OrEven1() {

        Automaton nfa1 = new Automaton("Odd0");
        nfa1.setWithLegend(false);

        nfa1.addSymbol(List.of("x")); // Symbols
        nfa1.addState(List.of("e0e1", "o0e1", "e0o1", "o0o1")); // States
        // Transitions
        nfa1.getState("e0e1").setTransition(List.of("0", "o0e1", "1", "e0o1"));
        nfa1.getState("o0e1").setTransition(List.of("0", "e0e1", "1", "o0o1"));
        nfa1.getState("e0o1").setTransition(List.of("0", "o0o1", "1", "e0e1"));
        nfa1.getState("o0o1").setTransition(List.of("0", "e0o1", "1", "o0e1"));
        nfa1.setAccept(List.of("o0e1", "o0o1", "e0e1")); // Accept states
        nfa1.setStart("e0e1"); // Start

        nfa1.makeRE();
        nfa1.makeAllREs();
    }

    static void Ps2Q31() {

        Automaton nfa1 = new Automaton("Ps2Q31");
        nfa1.setWithLegend(false);

        nfa1.addState(List.of("q1", "q2")); // States
        // Transitions
        nfa1.getState("q1")
                .setTransition(List.of("a", "q1", "a", "q2", "b", "q2"));
        nfa1.getState("q2").setTransition(List.of("b", "q1"));
        nfa1.setAccept(List.of("q1")); // Accept states
        nfa1.setStart("q1"); // Start

        nfa1.render();
        nfa1.makeRE(List.of("q2", "q1"));
    }

    static void Ps2Q32() {

        Automaton nfa1 = new Automaton("Ps2Q32");
        nfa1.setWithLegend(false);

        nfa1.addState(List.of("q1", "q2", "q3")); // States
        // Transitions
        nfa1.getState("q1")
                .setTransition(List.of(Automaton.EPSILON, "q2", "a", "q3"));
        nfa1.getState("q2").setTransition(List.of("a", "q1"));
        nfa1.getState("q3")
                .setTransition(List.of("a", "q2", "b", "q2", "b", "q3"));
        nfa1.setAccept(List.of("q2")); // Accept states
        nfa1.setStart("q1"); // Start

        nfa1.render();
        nfa1.makeAllREs();
        nfa1.makeRE(List.of("q2", "q3", "q1"));
    }

    public static void main(String... args) {
        Ps2Q32();
    }

}