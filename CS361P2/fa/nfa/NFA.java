package fa.nfa;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import fa.dfa.DFA;
import fa.dfa.DFAState;


public class NFA implements NFAInterface{
    private NFAState startState;    //creating start state.
    private Set<NFAState> states; //alphabet container
    private Set<Character> alphabet;
    
    public NFA() {
    	states = new LinkedHashSet<NFAState>();
        alphabet = new LinkedHashSet<Character>();
    }
	@Override
	public void addStartState(String name) {
		// TODO Auto-generated method stub
		NFAState s = checkIfExists(name);
		if(s == null){
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
		if( s == null){
			s = new NFAState(name);
			addState(s);
		} else {
			System.out.println("WARNING: A state with name " + name + " already exists in the DFA");
		}
	}
        //overloaded addState
    public void addState(NFAState s) {
        states.add(s);  
    }

	@Override
	public void addFinalState(String name) {
		// TODO Auto-generated method stub
		NFAState s = checkIfExists(name);
		if( s == null){
			s = new NFAState(name, true);
			addState(s);
		} else {
			System.out.println("WARNING: A state with name " + name + " already exists in the DFA");
		}
	}

	@Override
	public void addTransition(String fromState, char onSymb, String toState) {
		// TODO Auto-generated method stub
		
	}

	private NFAState checkIfExists(String name){
		NFAState ret = null;
		for(NFAState s : states){
			if(s.getName().equals(name)){
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
		for(NFAState s : states){
			if(s.isFinal()){
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
		return null;
	}
	
	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		// TODO Auto-generated method stub
		return from.getTo(onSymb);
	}

	@Override
	public Set<NFAState> eClosure(NFAState s) {
		// TODO Auto-generated method stub
		Set<NFAState> visited =  new LinkedHashSet<NFAState>();
		
		eClosureHelper(s, visited);
		return visited;
		
		
	}
	private void eClosureHelper(NFAState s, Set<NFAState> visited)
	{
		visited.add(s);
		//While we can move to a different state from an 'e'
		while(s.getTo('e') != null)
		{
		//for every NFASTate in s.getTo('e')
			Iterator<NFAState> iterator = s.getTo('e').iterator();
			eClosureHelper(iterator.next(), visited);
		}
		
}
}