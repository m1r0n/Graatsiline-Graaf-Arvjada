import sun.util.locale.provider.FallbackLocaleProviderAdapter;

/**
 * Created by Miron on 21/03/2018.
 */
public class GraatsilineGraaf{

    private static short n;
    private NodeList tipud;

    GraatsilineGraaf(short[] servad, short n) {
        GraatsilineGraaf.n = n;
        this.tipud = new NodeList(kulgnevus_maatriks(servad));
    }

    private static int[][] kulgnevus_maatriks(short[] servad) {
        int[][] adj = new int[n + 1][n + 1];

        int abi;
        for (int i = 0; i < n; i++) {
            abi = servad[i];
            adj[abi][abi + i + 1] = 1;
            adj[abi + i + 1][abi] = 1;
        }
        return adj;
    }

    @Override
    public boolean equals(Object obj) {
        GraatsilineGraaf compareTo = (GraatsilineGraaf) obj;
        return Isomorphism.areIsomorphic(this.tipud, compareTo.tipud);
    }

    @Override public int hashCode() {
        return 0;
    }



}
