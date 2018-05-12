import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.*;
/**
 * Created by Miron on 22/03/2018.
 */
public class Graceful_final {

    private static final short nrOfVertices = 13;
    private static final short nrOfEdges = nrOfVertices - 1;
    private static volatile Set<GracefulGraph> graphs;
    private static final int nrOfThreads = 6;
    private static final String filesLocation = "/gpfs/hpchome/miron/thesis/graphs13/final";
    private static ConcurrentLinkedQueue<Set<GracefulGraph>> globalQueue;
    private static final Object lock = new Object();

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        graphs = Collections.synchronizedSet(new HashSet<>());
        globalQueue = new ConcurrentLinkedQueue();
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(nrOfThreads);

        //failide nimekiri
        File dir = new File(filesLocation);
        //String algabRegexiga = "tulemus" + nrOfVertices + "_";
        String algabRegexiga = "graatsilised_" + nrOfVertices + "_";
        File[] foundFiles = dir.listFiles((dir1, name) -> name.startsWith(algabRegexiga));


        //Failide sisselugemine
        for (File file : foundFiles) {
            Runnable readFromFile = () -> {

                Set<GracefulGraph> threadiGraafid = Collections.synchronizedSet(new HashSet<>());
                String fileFullName = filesLocation + "/" + file.getName();

                try(BufferedReader br = new BufferedReader(new FileReader(fileFullName))) {
                    String line = br.readLine();

                    int[][] graphMatrix = new int[nrOfVertices][];
                    int row = 0;
                    while (line != null) {
                        if (line.length() != 0) {
                            row++;
                            int[] array = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
                            graphMatrix[row-1] = array;

                            if (row == nrOfVertices) {
                                threadiGraafid.add(new GracefulGraph(graphMatrix, nrOfEdges));
                                graphMatrix = new int[nrOfVertices][];
                                row = 0;
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

        Set<GracefulGraph> finalSet = new HashSet<>();
        for (Set<GracefulGraph> graphs : globalQueue) {
            finalSet.addAll(graphs);
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
                    Set<GracefulGraph> set1 = null;
                    Set<GracefulGraph> set2 = null;
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

        System.out.println("n=" + nrOfVertices + " puhul on graatsilisi graafe: " + globalQueue.peek().size());
        stop = System.currentTimeMillis();
        System.out.println("Kokku " + (stop - start) / 1000.0 + " sekundit");
        for (GracefulGraph graaf : globalQueue.peek()) {
            System.out.println(graaf);
        }


    }

}
