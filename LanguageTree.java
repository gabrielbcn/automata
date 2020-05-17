import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LanguageTree {

    static ArrayList<String> accepted;
    static Automaton automaton;

    String name;
    List<Automaton.State> states;

    LanguageTree(String name, List<Automaton.State> states) {
        this.name = name;
        this.states = states;
        if (LanguageTree.automaton
                .isAccept(LanguageTree.automaton.epsilonClosure(this.states)))
            accepted.add(this.name);
    }

    static LanguageTree createFirst(Automaton automaton) {
        LanguageTree.automaton = automaton;
        accepted = new ArrayList<>();
        return new LanguageTree("",
                Arrays.asList(LanguageTree.automaton.getStartState()));
    }

    public List<Automaton.State> getStates() {
        return states;
    }

    public void setStates(List<Automaton.State> states) {
        this.states = states;
    }

    public List<LanguageTree> children(LanguageTree lt) {
        return LanguageTree.automaton.symbols.values()
                .stream()
                .filter(sy -> !sy.name.equals(Automaton.EPSILON))
                .map(symbol -> new LanguageTree(
                        lt.name.concat(symbol.getName()),
                        LanguageTree.automaton.transition(symbol,
                                LanguageTree.automaton
                                        .epsilonClosure(lt.states))))
                .collect(Collectors.toList());
    }

    public List<LanguageTree> children(List<LanguageTree> ltlist) {
        return ltlist.stream()
                .flatMap(lt -> children(lt).stream())
                .collect(Collectors.toList());
    }

    public static void reproduce(Automaton automaton, int max) {
        LanguageTree lt = createFirst(automaton);
        Stream.iterate(Arrays.asList(lt), lt::children)
                .takeWhile(ltlist -> ltlist.size() < max)
                .count();
        System.out.println(LanguageTree.accepted);
    }
}