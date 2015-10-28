package commandline;
import java.util.HashMap;
import java.util.Vector;


/**
 * Created by Joachim on 21/102015.
 */
public class CommandLine {
    private String description = "CommandLine Test";
    private HashMap<String, Option> map;
    private Vector<String> keyList;



    public CommandLine() {
        this.map = new HashMap<String, Option>();
        this.keyList = new Vector<String>();
    }



    public HashMap<String, Option> getMap() {
        return map;
    }

    public void addOption(String shortFlag, String longFlag, int args, boolean isRequired, Type type, String description, String regex) throws Exception {
        Option op = new Option(shortFlag, longFlag, args, isRequired, type, description, regex);
        this.map.put(op.getShortFlag(), op);
        this.map.put(op.getLongFlag(), op);
    }

    public void addOption(String shortFlag, String longFlag, int args, boolean isRequired, Type type, String description) throws Exception {
        Option op = new Option(shortFlag, longFlag, args, isRequired, type, description);
        this.map.put(op.getShortFlag(), op);
        this.map.put(op.getLongFlag(), op);
    }

    public boolean hasOption(String key) {
        return this.map.containsKey(key);
    }

    public boolean hasOption(char key) {
        return this.map.containsKey(key);
    }

    public Option getOption(String key) {
        return this.map.get(key);
    }

    public void parseArgs(String[] args) throws Exception {
        String key;
        Option option;
        for (int i = 0; i < args.length; i++) {
            if(args[i].matches("^-[^-].*")) {
                if(this.hasOption(args[i].substring(1))) {
                    key = args[i].substring(1);
                    option = map.get(key);
                    this.keyList.add(key);
                    if(option.hasArg()) {
                        for (int a = 0; a < option.getArgs(); a++) {
                            if(i+1 < args.length) {
                                i++;
                                option.setValue(args[i], a);
                            } else
                                throw new Exception("Missing arguments in the command line input for key: " + key);
                        }
                    } else if(option.getType()== Type.BOOLEAN)
                        option.setValue("True");
                } else if(args[i].length() > 2) {
                    char[] multiflag = args[i].toCharArray();
                    for (int j = 1; j < multiflag.length; j++) {
                        if (this.hasOption(args[j])) {
                            key = args[i];
                            option = this.map.get(key);
                            this.keyList.add(key);
                            if (!option.hasArg())
                                option.setValue("True");
                            else
                                throw new Exception("Single char flags with an input argument are not allowed to appear in a multi flag.");
                        }
                    }
                }
            } else if(args[i].matches("^--[^-].*")) {
                if(map.containsKey(args[i].substring(2))) {
                    key = args[i].substring(2);
                    option = this.map.get(key);
                    this.keyList.add(key);
                    if(!option.hasArg()) {
                        option.setValue("True");
                    } else for(int a = 0; a < option.getArgs(); a++) {
                        if(i+1 < args.length) {
                            i++;
                            option.setValue(args[i], a);
                        } else
                            throw new Exception("Missing arguments in the command line input for key: " + key);
                    }
                } else
                    throw new Exception(args[i] + " is an unknown argument.");
            } else {
                throw new Exception("Unknown argument error. Read the help thoroughly.");
            }
        }
    }

    public boolean isOption(String s) {
        return keyList.contains(s);
    }

    public static String formatString(int lineLength, String space, String line) {
        int seqLength = line.length();
        int lineCount = seqLength / lineLength;
        String formatString = "";
        for(int i = 0; i < lineCount; i++) {
            formatString += space;
            formatString += line.substring(i * lineLength, (i+1) * lineLength) + "\n";
        }
        formatString += space;
        formatString += line.substring(lineCount * lineLength, seqLength);
        return formatString;
    }

    public void printHelp() {
        System.out.println(this.description);
        for(String key : this.map.keySet()) {
            Option option = this.map.get(key);
            System.out.println("--" + option.getLongFlag() + ", " + "-" + option.getShortFlag() + ":");
            System.out.println(this.formatString(80, "    ", option.getDescription()));
        }
    }

    public static void main(String[] args) throws Exception {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption("i", "input", 1, true, Type.FILE, "Input Fasta file that is to be read. Must fulfill the fasta format conventions. Must have the file extension .fasta or .fa", "..*\\.(fasta|fa)");
        commandLine.addOption("o","output",1,false, Type.FILE, "Output Fasta file that is to be written to. Must fulfill the fasta format conventions. Must have the file extension .fasta or .fa", "..*\\.(fasta|fa)");
        commandLine.addOption("a","affine",2,false, Type.INTEGER, "If defined, affine gap penalty is used. The following two arguments define gapopen and gapextend cost.");
        commandLine.addOption("m","match",1,false,Type.INTEGER, "Defines the match score of the alignment.");
        commandLine.addOption("n","mismatch",1,false,Type.INTEGER,"Defines the mismatch score of the alignment.");
        commandLine.addOption("l","linear",1,false,Type.INTEGER,"Defines the linear gap cost of the alignment.");

        commandLine.parseArgs(args);

        if(commandLine.hasOption('i')) {
            System.out.println(commandLine.getOption("i").getFile());
        } else if(commandLine.hasOption('o')) {
            System.out.println(commandLine.getOption("o").getFile());
        } else if(commandLine.hasOption('a')) {
            System.out.println(commandLine.getOption("a").getValue(1));
            System.out.println(commandLine.getOption("a").getValue(2));
        } if(commandLine.hasOption('m')) {
            System.out.println(commandLine.getOption("m").getInt());
        } if(commandLine.hasOption('n')) {
            System.out.println(commandLine.getOption("n").getInt());
        } if(commandLine.hasOption('l')) {
            System.out.println(commandLine.getOption("l").getInt());
        }
        commandLine.printHelp();
    }
}
