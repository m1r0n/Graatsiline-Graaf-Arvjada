import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Holds matching state for VF2 graph isomorphism algorithm
 */
public class VF2_State {

	public static final int NULL_ELEM = -1;

	//mappings. Nr x on position n means that n-th node in nodeList1 is mapped to x-th node in nodeList2
	private int maping1[];
	private int maping2[];

	private final int nrOfNodes;

	private NodeList nodeList1;
	private NodeList nodeList2;

    public VF2_State(NodeList nodeList1, NodeList nodeList2) {
        this.nodeList1 = nodeList1;
        this.nodeList2 = nodeList2;

        nrOfNodes = nodeList1.getNrOfNodes();

        maping1 = new int[nrOfNodes];
        maping2 = new int[nrOfNodes];

        Arrays.fill(maping1, NULL_ELEM);
        Arrays.fill(maping2, NULL_ELEM);
    }

    public void addToMapping(IntPair pair){
    	maping1[pair.getFirstInt()] = pair.getSecondInt();
    	maping2[pair.getSecondInt()] = pair.getFirstInt();
    }

    //Calculate next possible mapping pairs
	List<IntPair> calculatePairs() {

		List<IntPair> pairs = new ArrayList<>();

		int firstElem = -1;

		//finds first element in first graph not yet mapped to anything
		for(int i = 0; i < maping1.length; i++){
			if(maping1[i] == NULL_ELEM){
				firstElem = i;
			}
		}

		if(firstElem == -1){
			return pairs;
		}

		for(int i = 0; i < maping2.length; i++){
			if(maping2[i] == NULL_ELEM){
				pairs.add(new IntPair(firstElem, i));
			}
		}

		return pairs;
	}

	//returns true if after adding the pair mapping will still be isomorphism
	boolean isFeasible(IntPair pair) {
		Node node1 = nodeList1.getNode(pair.getFirstInt());
		Node node2 = nodeList2.getNode(pair.getSecondInt());

		List<Node> neighbours1 = node1.getNeighbourNodes();
		List<Node> neighbours2 = node2.getNeighbourNodes();

		//checks degree matching
		if(neighbours1.size() != neighbours2.size()){
			return false;
		}

		for(Node node : neighbours1){
			int index = nodeList1.getNodes().indexOf(node);
			if(maping1[index] != NULL_ELEM){
				Node mappedNode = nodeList2.getNode(maping1[index]);
				if(!node2.getNeighbourNodes().contains(mappedNode)){
					return false;
				}
			}
		}

		for(Node node : neighbours2){
			int index = nodeList2.getNodes().indexOf(node);
			if(maping2[index] != NULL_ELEM){
				Node mappedNode = nodeList1.getNode(maping2[index]);
				if(!node1.getNeighbourNodes().contains(mappedNode)){
					return false;
				}
			}
		}

		return true;
	}

	public void RemoveFromMapping(IntPair pair) {
		maping1[pair.getFirstInt()] = NULL_ELEM;
		maping2[pair.getSecondInt()] = NULL_ELEM;
	}
}
