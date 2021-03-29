package fa.nfa;

import java.util.HashMap;
import java.util.Set;

import fa.State;
import fa.dfa.DFAState;

public class NFAState extends State{
    private HashMap<Character,Set<NFAState>> delta;
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
	delta = new HashMap<Character, Set<NFAState>>();
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
	Set<NFAState> ret = delta.get(onSymb);
	ret.add(toState);
	delta.put(onSymb, ret);
}

/**
 * Retrieves the state that <code>this</code> transitions to
 * on the given symbol
 * @param symb - the alphabet symbol
 * @return the new state 
 */
public Set<NFAState> getTo(char symb){
	Set<NFAState> ret = delta.get(symb);
//	if(ret == null){
//		 System.err.println("ERROR: NFAState.getTo(char symb) returns null on " + symb + " from " + name);
//		 System.exit(2);
//		}
	return ret;
	
}
}
