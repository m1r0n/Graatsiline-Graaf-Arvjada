import java.util.List;


/**
 *Contains general methods used for solution checking of many different exercises.
 */
public class Isomorphism {


	public static boolean areIsomorphic(int[][] adjMat1, int[][] adjMat2){

		return areIsomorphic(new NodeList(adjMat1), new NodeList(adjMat2));
	}

	//Graph isomorphism check by VF2 algorithm
	public static boolean areIsomorphic(NodeList nodeList1, NodeList nodeList2){
		/*//Some easy checks to rule out trivial nonisomorphisms
		if(nodeList1.getNrOfNodes() != nodeList2.getNrOfNodes()){
			return false;
		}
		if(edgeList1.getNrOfEdges() != edgeList2.getNrOfEdges()){
			return false;
		}
		if(nodeList1.getMaxDegree() != nodeList2.getMaxDegree()){
			return false;
		}
		if(nodeList1.getMinDegree() != nodeList2.getMinDegree()){
			return false;
		}*/

		VF2_State beginState = new VF2_State(nodeList1, nodeList2);

		VF2_State finalState = findMapping(beginState);
		if(finalState == null){
				return false;
		}
			return true;
	}

	/*returns state that contains mappings if graphs are isomorphic and otherwise null. Assumes that graphs have
	*same nr of nodes.
	*/
	private static VF2_State findMapping(VF2_State s){

		VF2_State state = null;

		//calculate all potential pairs and try to extend mapping by them
		List<IntPair> pairs = s.calculatePairs();
		if(pairs.isEmpty()){
			return s;
		}else{
			for(IntPair pair : pairs){
				  if(s.isFeasible(pair)){
					  s.addToMapping(pair);
					  state = findMapping(s);
					  if(state != null){
						  break;
					  }
					  s.RemoveFromMapping(pair);
				  }
			}
		}
		return state;
	}

}
