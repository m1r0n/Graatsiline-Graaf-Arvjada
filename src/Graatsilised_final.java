import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Miron on 22/03/2018.
 */
public class Graatsilised_final{

    private static final short tippudeArv = 6;
    private static final short servadeArv = tippudeArv - 1;
    private static volatile Set<GraatsilineGraaf> graafid;

    public static void main(String[] args) {
        graafid = Collections.synchronizedSet(new HashSet<>());

        //failide nimekiri
        File dir = new File("C:/Users/Miron/Desktop/Graatsiline-Graaf-Arvjada");
        File[] foundFiles = dir.listFiles((dir1, name) -> name.startsWith("isomorfsus"));

        for (File file : foundFiles) {
            System.out.println(file.getName());
        }

        int  corePoolSize  =    5;
        int  maxPoolSize   =   10;
        long keepAliveTime = 5000;

        ExecutorService threadPoolExecutor =
                new ThreadPoolExecutor(
                        corePoolSize,
                        maxPoolSize,
                        keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>()
                );


        /*
        try(BufferedReader br = new BufferedReader(new FileReader("isomorfsus.txt"))) {
            String line = br.readLine();

            int[][] graafiMaatriks = new int[tippudeArv][];
            int rida = 0;
            while (line != null) {
                if (line.length() != 0) {
                    rida++;
                    int[] array = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
                    graafiMaatriks[rida-1] = array;

                    if (rida == tippudeArv) {
                        graafid.add(new GraatsilineGraaf(graafiMaatriks, servadeArv));
                        graafiMaatriks = new int[tippudeArv][];
                        rida = 0;
                    }
                }
                line = br.readLine();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        */
    }

}
