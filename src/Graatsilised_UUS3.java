import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Graatsilised_UUS3 {
    private static final short n = 10;//servade arv, tippe 1 võrra rohkem !!!!!
    private static final short rek = 3; //rek - rekursiooni tase, millest tuleks jagada funktsioon genereeriGraaf lõimedeks
    private static short arvutiNR;
    private static AtomicInteger counter = new AtomicInteger(0);
    private static volatile Set<GraatsilineGraaf> unikaalsed;
    private static List<List<Short>> options;

    //Lõim töötab eraldi meetodis
    private static Set<GraatsilineGraaf> genereeriGraaf(int pikkus, short[] servad, Set<GraatsilineGraaf> threadiGraafid) {
        if (pikkus == 0) {
            threadiGraafid.add(new GraatsilineGraaf(servad,n));
            counter.incrementAndGet();
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
            for (int i = 0; i <= n - pikkus; i++) {
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
        else {
            short[] uus = new short[servad.length + 1];
            uus[0] = options.get(arvutiNR).get(n-pikkus);
            System.arraycopy(servad, 0, uus, 1, servad.length);
            genereeriGraaf((short) (pikkus - 1), uus);
        }
    }

    public static void main(String[] args) {
        arvutiNR = Short.parseShort(args[0]);
        options = genereeriTeeValikud();


        unikaalsed = Collections.synchronizedSet(new HashSet<>());

        //long start = System.currentTimeMillis();
        short[] jada = new short[0];
        genereeriGraaf(n, jada);

        while (Thread.activeCount() > 2) ; //Ootame, kuni kõik lõimed on oma töö lõpetanud

        //System.out.println("n=" + (n + 1) + " tipu puhul on erinevaid graafe: " + unikaalsed.size());

        //long stop = System.currentTimeMillis();
        //System.out.println("Aega kulus " + (stop - start) / 1000.0 + " sekundit");
        //System.out.println("TOTAL:" + counter.toString());



        for (GraatsilineGraaf graaf : unikaalsed) {
            System.out.println(graaf);
        }


    }

    private static List<List<Short>> genereeriTeeValikud() {
        int valikuteTase = rek + 1;
        List<List<Short>> valikud = new ArrayList<>();
        List<Short> temporary = new ArrayList();
        temporary.add((short) 0);
        valikud.add(temporary);
        for (int i = 1; i < valikuteTase; i++) {
            List<List<Short>> b = new ArrayList<>();
            for (short j = 0; j <= i; j++) {
                for (List<Short> el : valikud) {
                    List<Short> temp = new ArrayList<>(el);
                    temp.add(j);
                    b.add(temp);
                }
            }
            valikud = b;
        }
        return valikud;
    }

    public void kirjutaFaili() {
        try {
            PrintWriter valjund = new PrintWriter(new FileWriter("isomorfsus.txt"), true);
            for (GraatsilineGraaf graaf : unikaalsed) {
                valjund.println(graaf);
            }
            valjund.close();
        } catch (IOException e) {
            System.out.println("S/V viga: " + e);
        }
    }


}


