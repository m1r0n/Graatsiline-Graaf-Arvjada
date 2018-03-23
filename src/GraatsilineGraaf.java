import sun.util.locale.provider.FallbackLocaleProviderAdapter;

import java.util.Arrays;

/**
 * Created by Miron on 21/03/2018.
 */
public class GraatsilineGraaf{

    private static short n;
    private NodeList tipud;
    private short[] servad;
    private int[][] kulgnevusMaatriks;

    GraatsilineGraaf(short[] servad, short n) {
        this.servad = servad;
        GraatsilineGraaf.n = n;
        this.kulgnevusMaatriks = kulgnevus_maatriks(servad);
        this.tipud = new NodeList(this.kulgnevusMaatriks);
    }

    GraatsilineGraaf(int[][] maatriks, short n) {
        this.kulgnevusMaatriks = maatriks;
        this.tipud = new NodeList(maatriks);
        GraatsilineGraaf.n = n;
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

    private static String valjasta_kulgnevusmaatriks(int[][] adj) {//väljastab külgnevusmaatriksi
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
        return valjasta_kulgnevusmaatriks(this.kulgnevusMaatriks);
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
