import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class Graatsilised_UUS3 {
    private static final short n = 5;//servade arv, tippe 1 võrra rohkem !!!!!
    private static final short rek = 2; //rek - rekursiooni tase, millest tuleks jagada funktsioon genereeriGraaf lõimedeks (REK peaks olema kaasa antud System variablina)
    private static final short arvutiJagamine = 4;
    private static short arvutiNR;


    private static volatile Set<GraatsilineGraaf> unikaalsed;

    private static Set<GraatsilineGraaf> genereeriGraaf(int pikkus, short[] servad, Set<GraatsilineGraaf> threadiGraafid) {
        if (pikkus == 0) {
            threadiGraafid.add(new GraatsilineGraaf(servad,n));
            return threadiGraafid;
        }
        else {
            for (short i = 0; i <= n - pikkus; i++) {
                short[] uus = new short[servad.length + 1];
                uus[0] = i; //fikseerime serva pikkusega "pikkus"
                System.arraycopy(servad, 0, uus, 1, servad.length);
                genereeriGraaf((pikkus - 1), uus, threadiGraafid);
            }
            return threadiGraafid;
        }
    }

    private static void genereeriGraaf(int pikkus, short[] servad) {
        // LÕIMEDE VAHEL JAGAMINE
        if(pikkus == n - rek) {
            for (int i = 0; i < n - pikkus; i++) {
                final int index = i;
                new Thread(() -> {
                    short[] uus = new short[servad.length + 1];
                    uus[0] = (short) index; //fikseerime serva pikkusega "pikkus"
                    System.arraycopy(servad, 0, uus, 1, servad.length);
                    Set<GraatsilineGraaf> threadiKoikGraafid = genereeriGraaf((short) (pikkus - 1), uus, new HashSet<>());
                    unikaalsed.addAll(threadiKoikGraafid);
                }).start();
            }
        }
        //iga arvuti saab arvutiNR, mis määrab igale arvutile tema vastava haru puus.
        else if (pikkus == arvutiJagamine) {
            short[] uus = new short[servad.length + 1];
            uus[0] = (short)(arvutiNR % arvutiJagamine); //iga arvuti saab endale ühe kindla haru. Kasutan arvutiJagamine jagamisega jääki. Tekivad arvud 1,2,3,...,0,1,2,3,...,0 jne.
            // ülejäänud kopeerime
            System.arraycopy(servad, 0, uus, 1, servad.length);
            genereeriGraaf((short) (pikkus - 1), uus);
        }
        else {
            for (short i = 0; i <= n - pikkus; i++) {
                short[] uus = new short[servad.length + 1];
                uus[0] = i; //fikseerime serva pikkusega "pikkus"
                System.arraycopy(servad, 0, uus, 1, servad.length);
                genereeriGraaf((pikkus - 1), uus);
            }
        }
    }

    public static void main(String[] args) {
        arvutiNR = Short.parseShort(args[0]);

        unikaalsed = Collections.synchronizedSet(new HashSet<>());

        long start = System.currentTimeMillis();
        short[] jada = new short[0];
        genereeriGraaf(n, jada);

        while (Thread.activeCount() > 2) ; //Ootame, kuni kõik lõimed on oma töö lõpetanud
        System.out.println("n=" + (n + 1) + " tipu puhul on erinevaid graafe: " + unikaalsed.size());

        long stop = System.currentTimeMillis();
        System.out.println("Aega kulus " + (stop - start) / 1000.0 + " sekundit");

        for (GraatsilineGraaf graaf : unikaalsed) {
            System.out.println(graaf);
        }


        /*
        try {
            PrintWriter valjund = new PrintWriter(new FileWriter("isomorfsus.txt"), true);
            for (GraatsilineGraaf graaf : unikaalsed) {
                valjund.println(graaf);
            }
            valjund.close();
        } catch (IOException e) {
            System.out.println("S/V viga: " + e);
        }
        */
    }
}


