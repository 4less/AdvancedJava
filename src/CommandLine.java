import java.io.*;
import java.util.Arrays;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Joachim on 20/10/2015.
 */
public class CommandLine {
    private FastaReader fReader;
    private String fasta;
    private String outputFile;
    private int[] lines = null;
    private int headerLength = 20;
    private int sequenceLength = 60;

    public int[] getLines() {
        return lines;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    /**
     * Resolves the pattern in the command line which specifies
     * which entries to print.
     * @param pattern
     * @return
     */
    public int[] getLines(String pattern) {
        String[] cache = pattern.split(",");
        Vector<Integer> inputLines = new Vector<Integer>();
        for(int i = 0; i < cache.length; i++) {
            String[] range = cache[i].split("-");
            if (range.length == 2) {
                int begin = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                for (; begin <= end; begin++)
                    inputLines.add(Integer.valueOf(begin));
            } else
                inputLines.add(Integer.parseInt(range[0]));

        }
        int j = 0;
        int[] result = new int[inputLines.size()];
        for (Integer value : inputLines) {
            result[j++] = value.intValue();
        }
        Arrays.sort(result);
        Vector<Integer> set = new Vector<Integer>();
        int cur = result[0];
        set.add(Integer.valueOf(cur));
        for (int i = 1; i < result.length; i++) {
            if(cur != result[i]) {
                cur = result[i];
                set.add(Integer.valueOf(cur));
            }
        }
        result = new int[set.size()];
        j = 0;
        for (Integer value : set) {
            result[j++] = value.intValue();
        }
        Arrays.sort(result);
        return result;
    }

    /**
     * Getter for Fasta
     * @return
     */
    public String getFasta() {
        return this.fasta;
    }

    /**
     * Getter for output file
     * @return
     */
    public String getOutputFile() {
        return outputFile;
    }

    /**
     * Checks if fasta isnt null
     * @return
     * @throws Exception
     */
    public boolean argumentsAreValid() throws Exception {
        if(this.fasta == null) {
            this.invalidInput();
            return false;
        }
        return true;
    }

    /**
     * Print help on the console
     */
    public void printHelp() throws IOException {
        System.out.println("test");
        BufferedReader bReader = new BufferedReader(new FileReader(new File("help.txt")));
        String line;
        while((line = bReader.readLine()) != null)
            System.out.println(line);
    }

    /**
     * Throws Exception for invalid Input
     * @throws Exception
     */
    private void invalidInput() throws Exception {
        throw new Exception("Invalid input. Type \'-?\' to get further information about the usage of this program.");
    }

    public void print() {
        System.out.println(this.fasta);
    }

    /**
     * Takes Commandline arguments and processes them
     * @param args
     * @throws Exception
     */
    private void processArgs(String[] args) throws Exception {
        if (args[0].matches("-h|-help|--h|--help")) {
            printHelp();
        } else {
            for(int i = 0; i < args.length; i++) {
                if(args[i].matches("-i|--i|-input|--input")) {
                    i++;
                    if(args[i].matches(".*\\.(fa|fasta)"))
                        this.fasta = args[i];
                    else invalidInput();
                } else if (args[i].matches("-o|--o")) {
                    i++;
                    if(args[i].matches(".*\\.(fasta|fa)")) {
                        this.outputFile = args[i];
                    } else
                        invalidInput();
                } else if (args[i].matches("-l|--l|-lines|--lines")) {
                    i++;
                    if(args[i].matches("(\\d\\d*|\\d\\d*-\\d\\d*)(,(\\d\\d*|\\d\\d*-\\d\\d*))*")) {
                        this.lines = this.getLines(args[i]);
                    }
                } else if (args[i].matches("-r|--r|-header|--header")) {
                    i++;
                    if(args[i].matches("\\d\\d*"))
                        this.headerLength = Integer.valueOf(args[i]);
                } else if (args[i].matches("-s|--s|-sequence|--sequence")) {
                    i++;
                    if(args[i].matches("\\d\\d*"))
                        this.sequenceLength = Integer.valueOf(args[i]);
                }
            }
        }
    }

    /**
     * runs the commandline and creates new FastaReader object
     * @param args
     * @return FastaReader
     * @throws Exception
     */
    public FastaReader run(String args[]) throws Exception {
        this.processArgs(args);
        this.argumentsAreValid();
        if (this.fasta != null)
           return new FastaReader(this.fasta);
        return null;
    }
}
