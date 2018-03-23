/**
 * Pair type meant only for positive integers.
 */
public class IntPair {

	private final int EMPTY_PAIR = -1;

	private int firstInt;
	private int secondInt;

	private boolean isEmpty;

	//For empty pairs
	public IntPair(){
		isEmpty = true;
	}

	//For nonempty pairs
	public IntPair(int firstInt, int secondInt){
		this.firstInt = firstInt;
		this.secondInt = secondInt;
		isEmpty = false;
	}

	public int getFirstInt() {
		if(isEmpty){
			return EMPTY_PAIR;
		}
		return firstInt;
	}

	public int getSecondInt() {
		if(isEmpty){
			return EMPTY_PAIR;
		}
		return secondInt;
	}

	public boolean isEmpty(){
		return isEmpty;
	}
}
