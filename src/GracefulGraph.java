/**
 * Created by Miron on 21/03/2018.
 */
public class GracefulGraph {

    private static short n;
    private NodeList vertices;
    private short[] edges;
    private int[][] adjacentMatrix;

    GracefulGraph(short[] edges, short n) {
        this.edges = edges;
        GracefulGraph.n = n;
        this.adjacentMatrix = adjacent_matrix(edges);
        this.vertices = new NodeList(this.adjacentMatrix);
    }

    GracefulGraph(int[][] matrix, short n) {
        this.adjacentMatrix = matrix;
        this.vertices = new NodeList(matrix);
        GracefulGraph.n = n;
    }

    private static int[][] adjacent_matrix(short[] edges) {
        int[][] adj = new int[n + 1][n + 1];

        int abi;
        for (int i = 0; i < n; i++) {
            abi = edges[i];
            adj[abi][abi + i + 1] = 1;
            adj[abi + i + 1][abi] = 1;
        }
        return adj;
    }

    private static String show_adjacentMatrix(int[][] adj) {//väljastab külgnevusmaatriksi
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= n ; i++) {
            for (int j = 0; j <= n; j++)
                sb.append(adj[i][j]).append(" ");
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return show_adjacentMatrix(this.adjacentMatrix);
    }

    @Override
    public boolean equals(Object obj) {
        GracefulGraph compareTo = (GracefulGraph) obj;
        return Isomorphism.areIsomorphic(this.vertices, compareTo.vertices);
    }

    @Override public int hashCode() {
        return 0;
    }



}
