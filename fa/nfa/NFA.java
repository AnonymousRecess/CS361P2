package fa.nfa;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import fa.dfa.DFA;
import fa.dfa.DFAState;

/**
 * <h1>NFA!</h1> The NFA class is responsible for implementing the NFAInterface
 * Methods associated with this class allow for adding and traversing State
 * objects with non-determinism
 * 
 * @author Jeff Kahn
 * @author Jackson Edwards
 * @version 1.0
 * @since 2021-04-06
 */

public class NFA implements NFAInterface {
	private NFAState startState; // keeps track of startState in NFA
	private Set<NFAState> states; // states in NFA
	private Set<Character> alphabet; // characters in the NFA language

	public NFA() {
		states = new LinkedHashSet<NFAState>();
		alphabet = new LinkedHashSet<Character>();
	}

	/**
	 * This method adds an NFAState to the states container using an overloaded
	 * method while updating the startState Prints a warning if the state already
	 * exists in the container of states
	 * 
	 * @param name: String representation of state to add to container
	 * @return void
	 */
	public void addStartState(String name) {
		NFAState s = checkIfExists(name);
		if (s == null) {
			s = new NFAState(name);
			addState(s);
		} else {
			System.out.println("WARNING: A state with name " + name + " already exists in the NFA");
		}
		startState = s;
	}

	/**
	 * This method adds an NFAState to the states container by calling the
	 * overloaded method of the same name Prints a warning if the state already
	 * exists in the container of states
	 * 
	 * @param name: String representation of state to add to container
	 */
	public void addState(String name) {
		NFAState s = checkIfExists(name);
		if (s == null) {
			s = new NFAState(name);
			addState(s);
		} else {
			System.out.println("WARNING: A state with name " + name + " already exists in the NFA");
		}
	}

	/**
	 * This method adds an NFAState to the states container
	 * @param s: NFAState to add to the states container
	 */
	public void addState(NFAState s) {
		states.add(s);
	}

	/**
	 * Creates state and uses overloaded method to add it to the Set of states
	 * Also marks the state as being a final state
	 * @param name: String representation of the state to be created and added
	 */
	public void addFinalState(String name) {
		NFAState s = checkIfExists(name);
		if (s == null) {
			s = new NFAState(name, true);
			addState(s);
		} else {
			if (s == startState) {
				s.setFinal();
			}
			System.out.println("WARNING: A state with name " + name + " already exists in the NFA");
		}
	}

	/**
	 * Adds the transition to the delta structure
	 * @param fromState: String representation of state to transition from
	 * @param onSymb: Character to transition with
	 * @param toState: String representation of state to transition to
	 */
	public void addTransition(String fromState, char onSymb, String toState) {
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

	/**
	 * Checks if the NFAState with name exists in the states container
	 * @param name of State to check states container for
	 * @return NFAState related to name - null if not in container
	 */
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

	/**
	 * Returns the Set containing the NFAStates in the State Machine
	 * @return Set of NFA states
	 */
	public Set<NFAState> getStates() {
		return states;
	}

	/**
	 * Returns the Set containing the final states of the NFA
	 * @return Set<NFAState> containing the final states of the NFA
	 */
	public Set<NFAState> getFinalStates() {
		Set<NFAState> ret = new LinkedHashSet<NFAState>();
		for (NFAState s : states) {
			if (s.isFinal()) {
				ret.add(s);
			}
		}
		return ret;
	}

	/**
	 * Method for returning the startState of the NFA
	 * @return NFAState that is the root of the NFA
	 */
	public NFAState getStartState() {
		return startState;
	}

	/**
	 * Method that returns the Set of characters in the NFA
	 * @return Set<Character> that contains the characters in the NFA
	 */
	public Set<Character> getABC() {
		return alphabet;
	}

	/**
	 * Generates and returns the equivalent DFA to the NFA
	 * @return DFA that is equivalent to the NFA
	 */
	public DFA getDFA() {
		DFA dfa = new DFA(); // creates a new DFA object

		LinkedHashMap<Set<NFAState>, String> stateFarm = new LinkedHashMap<>(); // hashmap to store all states 

		Set<NFAState> addedStates = eClosure(startState); // adds the eclosure of states from the root using DFS
		
		// Section of code for checking if the states contain a final state
		boolean finalState = false;
		for (NFAState statess : addedStates) {
			if (statess.isFinal()) {
				finalState = true;
			}

		}

		LinkedList<Set<NFAState>> queueStates = new LinkedList<>(); // queue for performing BFS

		stateFarm.put(addedStates, addedStates.toString());
		
		if (finalState == true) {
			dfa.addFinalState(stateFarm.get(addedStates)); // Marks the startState as final
		}
		dfa.addStartState(stateFarm.get(addedStates));

		queueStates.add(addedStates); // add startState for BFS

		while (!queueStates.isEmpty()) {

			addedStates = queueStates.remove();

			for (char c : alphabet) {
				LinkedHashSet<NFAState> build = new LinkedHashSet<>();

				for (NFAState state : addedStates) {
					if (state.getTo(c) != null) {
						for (NFAState states : state.getTo(c)) {
							build.addAll(eClosure(states)); // check for empty transitions
						}
					}
				}
				while (!stateFarm.containsKey(build)) {
					stateFarm.put(build, build.toString()); // place built state in container
					queueStates.add(build); // add built state to queue for BFS

					finalState = false;
					for (NFAState check : build) {
						if (check.isFinal())
							finalState = true; // check if state in built state is final
					}
					if (finalState) {
						dfa.addFinalState(stateFarm.get(build));
					}

					if (!finalState) {
						dfa.addState(stateFarm.get(build));
					}

				}

				dfa.addTransition(addedStates.toString(), c, stateFarm.get(build)); // add transition for built string
			}

		}
		return dfa; // returns the built dfa
	}

	/**
	 * Returns the set of states the State can reach on the symbol
	 * @param from: State to get transitions from
	 * @param onSymb: Symbol to get transitions with
	 * @return Set<NFAState> set of states transitioned to
	 */
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		return from.getTo(onSymb);
	}

	/**
	 * Method to return a set of states that can be reached on an empty transition using DFS
	 * @param s NFAState to transition from
	 * @return Set<NFAState> visited states from an empty transition 
	 */
	public Set<NFAState> eClosure(NFAState s) {
		Set<NFAState> visited = new LinkedHashSet<NFAState>(); // container to keep track of visited states

		eClosureHelper(s, visited); // helper method for recursion
		return visited;

	}
	
	/**
	 * builds a set of NFAStates visited on empty transitions with recursion
	 * @param s NFAState to traverse with empty transitions
	 * @param visited Set to contain NFAStates reached
	 */
	private void eClosureHelper(NFAState s, Set<NFAState> visited) {
		visited.add(s);
		// While we can move to a different state from an 'e'
		Set<NFAState> next = s.getTo('e');
		if (s.getTo('e') != null) {
			Iterator<NFAState> iterator = next.iterator();
			NFAState nextnext = iterator.next();
			if (!visited.contains(nextnext)) { // prevents infinite recursion
				eClosureHelper(nextnext, visited);
			}
		}

	}
}