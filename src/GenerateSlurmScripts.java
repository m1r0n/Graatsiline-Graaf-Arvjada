import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by Miron on 30/03/2018.
 */
public class GenerateSlurmScripts {

    private static int numberOfFiles = 24;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        for (int i = 0; i < numberOfFiles; i++) {
            String fileName = "bash_sricpt_11_" + i + ".sh";
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");

            String s = "#!/bin/bash\n" + "\n" + "#SBATCH -p main\n" +
                    "\n" + "#SBATCH -J graph_" + i +"\n" +
                    "\n" + "#SBATCH -N 1\n" + "#SBATCH --ntasks 1\n" +
                    "\n" + "#SBATCH --ntasks-per-node=1\n" +
                    "\n" + "#SBATCH --cpus-per-task 4\n" + "#SBATCH --mem 10GB\n" +
                    "\n" + "#SBATCH -t 05:00:00\n" + "#SBATCH -D /gpfs/hpchome/miron/thesis/graphs11/\n" +
                    "\n" + "#SBATCH --output=/gpfs/hpchome/miron/thesis/graphs11/tulemus11_" + i +".out\n" +
                    "\n" + "#SBATCH --mail-type=END\n" + "#SBATCH --mail-user=miron.storozhev96@gmail.com\n" +
                    "\n" + "module load java-1.8.0_40\n" +
                    "module load jdk-1.8.0_25\n" +
                    "\n" + "cd Graatsiline-Graaf-Arvjada/src/\n" +
                    "\n" + "java Graatsilised_UUS3 " + i;
            writer.print(s);
            writer.close();
        }


    }


}
