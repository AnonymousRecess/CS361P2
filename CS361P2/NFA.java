package fa.nfa;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import fa.dfa.DFA;
import fa.dfa.DFAState;

public class NFA implements NFAInterface {
	private NFAState startState; // creating start state.
	private Set<NFAState> states; // alphabet container
	private Set<Character> alphabet;

	public NFA() {
		states = new LinkedHashSet<NFAState>();
		alphabet = new LinkedHashSet<Character>();
	}

	@Override
	public void addStartState(String name) {
		// TODO Auto-generated method stub
		NFAState s = checkIfExists(name);
		if (s == null) {
			s = new NFAState(name);
			addState(s);
		} else {
			System.out.println("WARNING: A state with name " + name + " already exists in the DFA");
		}
		startState = s;
	}

	@Override
	public void addState(String name) {
		// TODO Auto-generated method stub
		NFAState s = checkIfExists(name);
		if (s == null) {
			s = new NFAState(name);
			addState(s);
		} else {
			System.out.println("WARNING: A state with name " + name + " already exists in the DFA");
		}
	}

	// overloaded addState
	public void addState(NFAState s) {
		states.add(s);
	}

	@Override
	public void addFinalState(String name) {
		// TODO Auto-generated method stub
		NFAState s = checkIfExists(name);
		if (s == null) {
			s = new NFAState(name, true);
			addState(s);
		} else {
			System.out.println("WARNING: A state with name " + name + " already exists in the DFA");
		}
	}

	@Override
	public void addTransition(String fromState, char onSymb, String toState) {
		// TODO Auto-generated method stub
		NFAState from = checkIfExists(fromState);
		NFAState to = checkIfExists(toState);
		if (from == null) {
			System.err.println("ERROR: No NFA state exists with name " + fromState);
			System.exit(2);
		} else if (to == null) {
			System.err.println("ERROR: No NFA state exists with name " + toState);
			System.exit(2);
		}
		from.addTransition(onSymb, to);

		if (!alphabet.contains(onSymb) && onSymb != 'e') {
			alphabet.add(onSymb);
		}
	}

	private NFAState checkIfExists(String name) {
		NFAState ret = null;
		for (NFAState s : states) {
			if (s.getName().equals(name)) {
				ret = s;
				break;
			}
		}
		return ret;
	}

	@Override
	public Set<NFAState> getStates() {
		// TODO Auto-generated method stub
		return states;
	}

	@Override
	public Set<NFAState> getFinalStates() {
		// TODO Auto-generated method stub
		Set<NFAState> ret = new LinkedHashSet<NFAState>();
		for (NFAState s : states) {
			if (s.isFinal()) {
				ret.add(s);
			}
		}
		return ret;
	}

	@Override
	public NFAState getStartState() {
		// TODO Auto-generated method stub
		return startState;
	}

	@Override
	public Set<Character> getABC() {
		// TODO Auto-generated method stub
		return alphabet;
	}

	@Override
	public DFA getDFA() {
		// TODO Auto-generated method stub
		DFA dfa = new DFA();

		LinkedHashMap<Set<NFAState>, String> stateFarm = new LinkedHashMap<>();

		Set<NFAState> addedStates = eClosure(startState);

		LinkedList<Set<NFAState>> notSureYet = new LinkedList<>();
		
		stateFarm.put(addedStates, addedStates.toString());

		dfa.addStartState(stateFarm.get(addedStates));

		notSureYet.add(addedStates);

		while (!notSureYet.isEmpty()) {
			
			addedStates = notSureYet.remove();
			
			for(char c:alphabet)
			{
				LinkedHashSet<NFAState> build = new LinkedHashSet<>();
				for(NFAState state: addedStates) {
					if(state.getTo(c) != null)
					{
						for(NFAState states: state.getTo(c))
						{
							build.addAll(eClosure(states));
						}
					}
				}
				while(!stateFarm.containsKey(build)) {
					stateFarm.put(build,build.toString());
					notSureYet.add(build);
					
					boolean finalState = false;
					for(NFAState check: build) {
						if(check.isFinal())
							finalState = true;
					}
					if(finalState) {
						dfa.addFinalState(stateFarm.get(build));
					}
					
					if(!finalState) {
						dfa.addState(stateFarm.get(build));
					}
					
				}
				
				dfa.addTransition(addedStates.toString(), c, stateFarm.get(build));
			}
				//if(notSureYet) is not in DFA, add to DFA and to addedStates?
				// also add notSureYet as the transitions from addedStates on char symb
			
		}
		return dfa;
	}

	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		// TODO Auto-generated method stub
		return from.getTo(onSymb);
	}

	@Override
	public Set<NFAState> eClosure(NFAState s) {
		// TODO Auto-generated method stub
		Set<NFAState> visited = new LinkedHashSet<NFAState>();

		eClosureHelper(s, visited);
		return visited;

	}

	private void eClosureHelper(NFAState s, Set<NFAState> visited) {
		visited.add(s);
		// While we can move to a different state from an 'e'
		Set<NFAState> next = s.getTo('e');
		if (s.getTo('e') != null) {
			Iterator<NFAState> iterator = next.iterator();
			NFAState nextnext = iterator.next();
			if (!visited.contains(nextnext))
			{
				eClosureHelper(nextnext, visited);
			}
		}

	}
}