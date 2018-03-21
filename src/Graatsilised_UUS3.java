import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Graatsilised_UUS3 {
    private static final short n = 8;//servade arv, tippe 1 võrra rohkem !!!!!
    private static int abi = 0;
    private static Set<GraatsilineGraaf> graafid;
    //private static GraatsilineGraaf[] graatsilisedGraafid;

    private static void rek(int pikkus, short[] servad) {

        if (pikkus == 0) {
            graafid.add(new GraatsilineGraaf(servad,n));
            //graatsilisedGraafid[abi] = new GraatsilineGraaf(servad, n);
            //graafid[abi] = servad;
            abi++;
            return;
        }
        for (short i = 0; i <= n - pikkus; i++) {
            short[] uus = new short[servad.length + 1];
            uus[0] = i; //fikseerime serva pikkusega "pikkus"
            // ülejäänud kopeerime
            System.arraycopy(servad, 0, uus, 1, servad.length);
            rek((pikkus - 1), uus);
        }
    }



    public static void main(String[] args) {
        int margendeid = 1;
        /*
        for (int i = 1; i <= n; i++) //faktoriaal
            margendeid = margendeid * i;

            */
        //graatsilisedGraafid = new GraatsilineGraaf[margendeid];

        graafid = new HashSet<>();

        long start = System.currentTimeMillis();
        short[] jada = new short[0];
        rek(n, jada);



        //Set<GraatsilineGraaf> unique = new HashSet<>(Arrays.asList(graatsilisedGraafid));

        System.out.println("n=" + (n + 1) + " tipu puhul on erinevaid graafe: " + graafid.size());

        long stop = System.currentTimeMillis();
        System.out.println("Nüüd trükin faili isomorfuse järjendi:");
        System.out.println("Aega kulus " + (stop - start) / 1000.0 + " sekundit");
    }
}


