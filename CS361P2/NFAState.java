package fa.nfa;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.LinkedHashSet;

import java.util.Set;

import fa.State;
import fa.dfa.DFAState;

public class NFAState extends State{
    private HashMap<Character,LinkedHashSet<NFAState>> delta;
    private boolean isFinal;


    

/**
 *Default constructor
 */ 
public NFAState(String name)
{
	initDefault(name);
	isFinal = false;
}


/**
 * Overloaded constructor that sets the state type
 * @param name the state name
 * @param isFinal the type of state: true  fina=l
 */
 public NFAState(String name,boolean isFinal) 
 {
     initDefault(name);
     this.isFinal = isFinal;
 }


private void initDefault(String name ){
	this.name = name;
	delta = new HashMap<Character, LinkedHashSet<NFAState>>();
}

/**
 * Accessor for the state type
 * @return true if final and false otherwise
 */
public boolean isFinal(){
	return isFinal;
}


/**
 * Add the transition from <code> this </code> object
 * @param onSymb the alphabet symbol
 * @param toState to DFA state
 */
public void addTransition(char onSymb, NFAState toState){
	LinkedHashSet<NFAState> transitions = new LinkedHashSet<NFAState>();
	//key = symbol, value = states to get to
	if(delta.containsKey(onSymb))
	{
		transitions.addAll(delta.get(onSymb));
	}
	transitions.add(toState);
	delta.put(onSymb, transitions);
}

/**
 * Retrieves the state that <code>this</code> transitions to
 * on the given symbol
 * @param symb - the alphabet symbol
 * @return the new state 
 */
public Set<NFAState> getTo(char symb){
	LinkedHashSet<NFAState> returned = new LinkedHashSet<NFAState>();
	returned = delta.get(symb);
//	if(ret == null){
//		 System.err.println("ERROR: NFAState.getTo(char symb) returns null on " + symb + " from " + name);
//		 System.exit(2);
//		}
	return returned;
	
}
}
