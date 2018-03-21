//package graph;
import java.util.LinkedList;


public class NodeList {

	private LinkedList<Node> nodes = new LinkedList<Node>();

	public NodeList() {}

	public NodeList(int[][] adjacencyMatrix) {
		for(int i = 0; i < adjacencyMatrix.length; i++){
			addNode();
		}

		for(int i = 0; i < adjacencyMatrix.length; i++){
			Node node = getNode(i);
			for(int j = i+1; j < adjacencyMatrix.length; j++){
				if(adjacencyMatrix[i][j] == 1){
					node.addNeighbourNode(getNode(j));
				}
			}
		}

	}

	public int getNrOfNodes() {
		return nodes.size();
	}

	public void addNode() {
			Node n = new Node();
			nodes.add(n);
	}

	public void addNode(Node n) {
		nodes.add(n);
	}

	public void removeNode(Node n) {
		if(n != null) {
			//Remove from neighbours' lists
			for(Node neighbourNode : n.getNeighbourNodes()){
				neighbourNode.removeNeighbourNode(n);
			}
			nodes.remove(n);
		}
	}

	public Node getNode(int i) {
		return nodes.get(i);
	}

	public LinkedList<Node> getNodes() {
		return nodes;
	}

}
