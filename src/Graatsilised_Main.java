import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class Graatsilised_Main {
    private static final short n = 3;//servade arv, tippe 1 võrra rohkem !!!!!
    private static final short rek = 2; //rek - rekursiooni tase, kus tuleks jagada funktsioon genereeriGraaf lõimedeks
    private static final short algoritmiTase = 1; //tase, millest iga arvuti hakkab eraldi tööle. vajaMinevArvutiteArv = algoritmiTase!
    private static short arvutiNR;
    private static AtomicInteger counter = new AtomicInteger(0);
    private static volatile Set<GraatsilineGraaf> unikaalsed;
    private static List<List<Short>> options;
    private static ExecutorService threadPoolExecutor;


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
                Runnable loim = () -> {
                    short[] uus = new short[servad.length + 1];
                    uus[0] = (short) index; //fikseerime serva pikkusega "pikkus"
                    System.arraycopy(servad, 0, uus, 1, servad.length);
                    Set<GraatsilineGraaf> threadiKoikGraafid = genereeriGraaf((short) (pikkus - 1), uus, new HashSet<>());
                    unikaalsed.addAll(threadiKoikGraafid);
                };
                threadPoolExecutor.execute(loim);
            }
        }

        //Programmi jagamine osadeks
        else if(pikkus <= n - algoritmiTase){
            for (short i = 0; i <= n - pikkus; i++) {
                short[] uus = new short[servad.length + 1];
                uus[0] = i; //fikseerime serva pikkusega "pikkus"
                System.arraycopy(servad, 0, uus, 1, servad.length);
                genereeriGraaf((pikkus - 1), uus);
            }
        }

        else { //iga arvuti saab arvutiNR, mis määrab igale arvutile tema vastava haru puus.
            short[] uus = new short[servad.length + 1];
            uus[0] = options.get(arvutiNR).get(n-pikkus);
            System.arraycopy(servad, 0, uus, 1, servad.length);
            genereeriGraaf((short) (pikkus - 1), uus);
        }
    }

    public static void main(String[] args) {
        threadPoolExecutor = Executors.newFixedThreadPool(rek+1);
        arvutiNR = Short.parseShort(args[0]);
        options = genereeriTeeValikud();


        unikaalsed = Collections.synchronizedSet(new HashSet<>());

        long start = System.currentTimeMillis();
        short[] jada = new short[0];
        genereeriGraaf(n, jada);

        //while (Thread.activeCount() > 2) ; //Ootame, kuni kõik lõimed on oma töö lõpetanud


        threadPoolExecutor.shutdown();

        try {
            threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }

        //System.out.println("n=" + (n + 1) + " tipu puhul on erinevaid graafe: " + unikaalsed.size());

        long stop = System.currentTimeMillis();
        //System.out.println("Aega kulus " + (stop - start) / 1000.0 + " sekundit");
        //System.out.println("Läbi vaadati graafe: " + counter.toString());

        //kirjutaFaili();


        for (GraatsilineGraaf graaf : unikaalsed) {
            System.out.println(graaf);
        }
    }

    private static List<List<Short>> genereeriTeeValikud() {
        List<List<Short>> valikud = new ArrayList<>();
        List<Short> temporary = new ArrayList();
        temporary.add((short) 0);
        valikud.add(temporary);
        for (int i = 1; i < (int) algoritmiTase; i++) {
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

    public static void kirjutaFaili() {
        try {
            String failinimi = "tulemus" + (n+1) + "_" + arvutiNR + ".txt";
            PrintWriter valjund = new PrintWriter(new FileWriter(failinimi), true);
            for (GraatsilineGraaf graaf : unikaalsed) {
                valjund.println(graaf);
            }
            valjund.close();
        } catch (IOException e) {
            System.out.println("S/V viga: " + e);
        }
    }


}


