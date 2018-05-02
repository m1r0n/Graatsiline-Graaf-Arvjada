import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

/**
 * Created by Miron on 22/03/2018.
 */
public class Graatsilised_final{

    private static final short tippudeArv = 10;
    private static final short servadeArv = tippudeArv - 1;
    private static volatile Set<GraatsilineGraaf> graafid;
    private static final int nrOfThreads = 4;
    private static final String failideAsukoht = "/gpfs/hpchome/miron/thesis/graphs13/";
    private static ConcurrentLinkedQueue<Set<GraatsilineGraaf>> globalQueue;
    private static final Object lock = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        graafid = Collections.synchronizedSet(new HashSet<>());
        globalQueue = new ConcurrentLinkedQueue();

        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(nrOfThreads);

        //failide nimekiri
        File dir = new File(failideAsukoht);
        String algabRegexiga = "tulemus" + tippudeArv + "_";
        File[] foundFiles = dir.listFiles((dir1, name) -> name.startsWith(algabRegexiga));


        //Failide sisselugemine
        for (File file : foundFiles) {
            Runnable readFromFile = () -> {

                Set<GraatsilineGraaf> threadiGraafid = Collections.synchronizedSet(new HashSet<>());
                String fileFullName = failideAsukoht + "/" + file.getName();

                try(BufferedReader br = new BufferedReader(new FileReader(fileFullName))) {
                    String line = br.readLine();

                    int[][] graafiMaatriks = new int[tippudeArv][];
                    int rida = 0;
                    while (line != null) {
                        if (line.length() != 0) {
                            rida++;
                            int[] array = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
                            graafiMaatriks[rida-1] = array;

                            if (rida == tippudeArv) {
                                threadiGraafid.add(new GraatsilineGraaf(graafiMaatriks, servadeArv));
                                graafiMaatriks = new int[tippudeArv][];
                                rida = 0;
                            }
                        }
                        line = br.readLine();
                    }
                    globalQueue.add(threadiGraafid);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            };
            threadPoolExecutor.execute(readFromFile);
        }

        while (globalQueue.size() < foundFiles.length); //Wait until all threads done
        long stop = System.currentTimeMillis();
        System.out.println("Faili lugemine " + (stop - start) / 1000.0 + " sekundit");


/*
        threadPoolExecutor.shutdown();
        try {
            threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }

        Set<GraatsilineGraaf> finalSet = new HashSet<>();
        for (Set<GraatsilineGraaf> graafid : globalQueue) {
            finalSet.addAll(graafid);
        }
	    System.out.println(finalSet.size());
        stop = System.currentTimeMillis();
        System.out.println("Kokku " + (stop - start) / 1000.0 + " sekundit");
*/



        List<Future<?>> futures = new ArrayList<>();
        while (true) {
            if (globalQueue.size() <= 1) {
                boolean allDone = true;
                for(Future<?> future : futures){
                    allDone &= future.isDone(); // check if future is done
                }
                if (allDone) {
                    break;
                }
            }
            else if (globalQueue.size() > 1){
                Runnable runnableTask = () -> {
                    Set<GraatsilineGraaf> set1 = null;
                    Set<GraatsilineGraaf> set2 = null;
                    synchronized (lock) {
                        if (globalQueue.size() > 1) {
                            set1 = globalQueue.poll();
                            set2 = globalQueue.poll();
                        }
                    }
                    if (set1 != null && set2 != null) {
                        set1.addAll(set2);
                        globalQueue.add(set1);
                    }
                };
                Future<?> f = threadPoolExecutor.submit(runnableTask);
                futures.add(f);
            }
        }

        threadPoolExecutor.shutdown();
        try {
            threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("n=" + tippudeArv + " puhul on graatsilisi graafe: " + globalQueue.peek().size());
        stop = System.currentTimeMillis();
        System.out.println("Kokku " + (stop - start) / 1000.0 + " sekundit");


    }

}
