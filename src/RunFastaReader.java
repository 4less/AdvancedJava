/**
 * Created by Joachim on 21/10/2015.
 *
 * Has to Variables. FastaReader and Commandline.
 */
public class RunFastaReader {
    private FastaReader fastaReader;
    private CommandLine commandLine;

    /**
     * runs the command line and FastaREader
     * @param args
     * @throws Exception
     */
    public void run(String[] args) throws Exception {
        this.commandLine = new CommandLine();
        this.fastaReader = this.commandLine.run(args);
        fastaReader.print(commandLine.getHeaderLength(), commandLine.getSequenceLength(), commandLine.getLines());
    }

    public static void main(String[] args) throws Exception {
        RunFastaReader runFastaReader = new RunFastaReader();
        runFastaReader.run(args);
    }
}
