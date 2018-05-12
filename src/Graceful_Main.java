import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class Graceful_Main {
    private static final short n = 7;//servade arv, tippe 1 võrra rohkem !!!!!
    private static final short rek = 2; //rek - rekursiooni tase, kus tuleks jagada funktsioon generateGraphsSet lõimedeks
    private static final short algorithmDivisionLevel = 1; //tase, millest iga arvuti hakkab eraldi tööle. vajaMinevArvutiteArv = algorithmDivisionLevel!
    private static short computerNR;
    private static AtomicInteger counter = new AtomicInteger(0);
    private static volatile Set<GracefulGraph> unique;
    private static List<List<Short>> options;
    private static ExecutorService threadPoolExecutor;


    //Lõim töötab eraldi meetodis
    private static Set<GracefulGraph> generateGraphsSet(int len, short[] edges, Set<GracefulGraph> threadGraphs) {
        if (len == 0) {
            threadGraphs.add(new GracefulGraph(edges,n));
            counter.incrementAndGet();
            return threadGraphs;
        }
        else {
            for (short i = 0; i <= n - len; i++) {
                short[] sheppardArray = new short[edges.length + 1];
                sheppardArray[0] = i; //fikseerime serva pikkusega "len"
                System.arraycopy(edges, 0, sheppardArray, 1, edges.length);
                generateGraphsSet((len - 1), sheppardArray, threadGraphs);
            }
            return threadGraphs;
        }
    }

    private static void generateGraphsSet(int len, short[] servad) {
        // LÕIMEDE VAHEL JAGAMINE
        if(len == n - rek) {
            for (int i = 0; i <= n - len; i++) {
                final int index = i;
                Runnable loim = () -> {
                    short[] uus = new short[servad.length + 1];
                    uus[0] = (short) index; //fikseerime serva pikkusega "len"
                    System.arraycopy(servad, 0, uus, 1, servad.length);
                    Set<GracefulGraph> threadiKoikGraafid = generateGraphsSet((short) (len - 1), uus, new HashSet<>());
                    unique.addAll(threadiKoikGraafid);
                };
                threadPoolExecutor.execute(loim);
            }
        }

        //Programmi jagamine osadeks
        else if(len <= n - algorithmDivisionLevel){
            for (short i = 0; i <= n - len; i++) {
                short[] uus = new short[servad.length + 1];
                uus[0] = i; //fikseerime serva pikkusega "len"
                System.arraycopy(servad, 0, uus, 1, servad.length);
                generateGraphsSet((len - 1), uus);
            }
        }

        else { //iga arvuti saab computerNR, mis määrab igale arvutile tema vastava haru puus.
            short[] uus = new short[servad.length + 1];
            uus[0] = options.get(computerNR).get(n-len);
            System.arraycopy(servad, 0, uus, 1, servad.length);
            generateGraphsSet((short) (len - 1), uus);
        }
    }

    public static void main(String[] args) {
        threadPoolExecutor = Executors.newFixedThreadPool(rek+1);
        computerNR = Short.parseShort(args[0]);
        options = genereeriTeeValikud();


        unique = Collections.synchronizedSet(new HashSet<>());

        long start = System.currentTimeMillis();
        short[] jada = new short[0];
        generateGraphsSet(n, jada);

        threadPoolExecutor.shutdown();

        try {
            threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }

        System.out.println("n=" + (n + 1) + " tipu puhul on erinevaid graafe: " + unique.size());

        long stop = System.currentTimeMillis();
        System.out.println("Aega kulus " + (stop - start) / 1000.0 + " sekundit");
        System.out.println("Läbi vaadati graafe: " + counter.toString());

        //writeToFile();


        for (GracefulGraph graaf : unique) {
            System.out.println(graaf);
        }
    }

    private static List<List<Short>> genereeriTeeValikud() {
        List<List<Short>> allOptions = new ArrayList<>();
        List<Short> temporary = new ArrayList();
        temporary.add((short) 0);
        allOptions.add(temporary);
        for (int i = 1; i < (int) algorithmDivisionLevel; i++) {
            List<List<Short>> b = new ArrayList<>();
            for (short j = 0; j <= i; j++) {
                for (List<Short> el : allOptions) {
                    List<Short> temp = new ArrayList<>(el);
                    temp.add(j);
                    b.add(temp);
                }
            }
            allOptions = b;
        }
        return allOptions;
    }

    public static void writeToFile() {
        try {
            String failinimi = "tulemus" + (n+1) + "_" + computerNR + ".txt";
            PrintWriter valjund = new PrintWriter(new FileWriter(failinimi), true);
            for (GracefulGraph graaf : unique) {
                valjund.println(graaf);
            }
            valjund.close();
        } catch (IOException e) {
            System.out.println("S/V viga: " + e);
        }
    }


}


