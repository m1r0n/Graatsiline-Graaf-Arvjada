import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
//9.05.2016 kell21.09
//külgnevsumaatriks on int-tüüpi arvudega ja algul tehakse kõik maatriksdid valmis. Seejärel hakatakse neid paarikaupa kontrollima
//hoian leitud unikaalseid (senivaadeldutega mitteisomorfsed) massiivis "unikaalsed"
//uue graafi panen unikaalsetesse vaid siis, kui see on kõikidega sealolevatega mitteisomorfne
//9.05.2016: väljatrükk faili "tulemus.txt"
//võtan nüüd kõik graafid, milels n tippu ja n-1 serva ja leia, millised on mittegraatsilised

public class Graatsilised_UUS3 {
    private static final int n = 8;//servade arv, tippe 1 võrra rohkem !!!!!
    private static int[][] graafid;
    private static int abi = 0;
    private static int unikaalseid = 0;
    private static Ahel[] unikaalsed = new Ahel[15000];

    private static void rek(int pikkus, int[] servad) {

        if (pikkus == 0) {
            graafid[abi] = servad;
            abi++;
            return;
        }
        for (int i = 0; i <= n - pikkus; i++) {
            int[] uus = new int[servad.length + 1];
            uus[0] = (int) i; //fikseerime serva pikkusega "pikkus"
            // ülejäänud kopeerime
            System.arraycopy(servad, 0, uus, 1, servad.length);
            rek((pikkus - 1), uus);
        }
    }

    private static int[][] kulgnevus_maatriks(int[] servad) {
        int[][] adj = new int[n + 1][n + 1];

        int abi;
        for (int i = 0; i < n; i++) {
            abi = servad[i];
            adj[abi][abi + i + 1] = 1;
            adj[abi + i + 1][abi] = 1;
        }
        return adj;
    }


    public static void main(String[] args) {
        int margendeid = 1;
        for (int i = 1; i <= n; i++) //faktoriaal
            margendeid = margendeid * i;


        graafid = new int[margendeid][n + 1];


        int[] jada = new int[0];
        rek(n, jada);

        long start = System.currentTimeMillis();

        //1. etapp - tekitan ahela, elemente nii palju kui "margendeid"

        Ahel uus = new Ahel(0);
        uus.tipud = new NodeList(kulgnevus_maatriks(graafid[0]));
        unikaalsed[0] = uus;
        unikaalseid++;//niipalju on selles massiivis erinevaid graafe

        System.out.println();

        for (int i = 1; i < margendeid; i++) {
            uus = new Ahel(i);
            uus.tipud = new NodeList(kulgnevus_maatriks(graafid[i]));
            //salves on unikaalsed[0]..unikaalsed[unikaalseid-1]
            boolean kas_panna = true; //kas lisada uus unikaalsetesse?

            for (int j = 0; j < unikaalseid; j++)
                if (Isomorphism.areIsomorphic(uus.tipud, unikaalsed[j].tipud)) {
                    kas_panna = false;
                    break;
                }
            if (kas_panna) {
                unikaalsed[unikaalseid] = uus;
                unikaalseid++;
                //System.out.println(unikaalseid);
            }

        }

        System.out.println("n=" + (n + 1) + " tipu puhul on erinevaid graafe: " + unikaalseid);

        long stop = System.currentTimeMillis();
        System.out.println("Nüüd trükin faili isomorfuse järjendi:");
        System.out.println("Aega kulus " + (stop - start) / 1000.0 + " sekundit");

    }
}


