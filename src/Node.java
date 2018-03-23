import java.util.ArrayList;
import java.util.List;

public class Node {

	private List<Node> neighbourNodes = new ArrayList<Node>();

	/*public int getDegree(){
        int degree = 0;

		for(int i = 0; i < panel.getEdges().getSize(); i++) {
			if(panel.getEdges().getEdge(i).isConnectedTo(this)) {
				degree++;
			}
		}

		return degree;
	}*/

	public List<Node> getNeighbourNodes() {
		return neighbourNodes;
	}

	public void removeNeighbourNode(Node node){
		neighbourNodes.remove(node);
	}

	public void addNeighbourNode(Node node){
		neighbourNodes.add(node);
		node.getNeighbourNodes().add(this);
	}

}
