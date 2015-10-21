import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader ;

/**
 * Created by Joachim on 20/10/2015.
 *
 * Class FastaReader extends class FastA and implements methods to read and print Fasta Files
 */
public class FastaReader extends FastA {
    public FastaReader(BufferedReader fastaReader) throws Exception {
        this.read(fastaReader);
    }

    /**
     * Constructor loading fasta file by String filePath
     * @param filePath path to fasta file
     * @throws Exception
     */
    public FastaReader(String filePath) throws Exception {
        this.read(filePath);
    }

    /**
     * Reads in FastA file by String filePath
     * @param filePath
     * @throws Exception
     */
    public void read(String filePath) throws Exception {
        read(new BufferedReader(new FileReader(new File(filePath))));
    }

    /**
     * Uses BufferedReader to load FastA file
     * @param fastaReader
     * @throws Exception
     */
    public void read(BufferedReader fastaReader) throws Exception {
        String line = null;
        String header = null;
        String sequence = null;

        while((line = fastaReader.readLine()) != null) {
            if (line.startsWith(">")) {
                if (header != null && sequence != null) {
                    this.addElement(new Sequence(sequence, header));
                    header = null;
                    sequence = null;
                }
                header = line.trim();
            } else {
                if (sequence == null)
                    sequence = line.trim();
                else
                    sequence += line.trim();
            }
        }
    }

    /**
     * Prints stored Sequences with default values of max length 20 for the header,
     * and max length of 60 for the sequence before breaking the line.
     */
    public void print() {
        this.print(20,60,null);
    }

    /**
     * creates a string consisting of count-spaces
     * @param count
     * @return
     */
    private String spaces(int count) {
        String result = "";
        for(int i = 0; i < count; i++) {
            result += " ";
        }
        return result;
    }

    /**
     * Print stored Sequences
     * @param lengthHeader maxlength header
     * @param lengthSequence count of chars before beginning a new line
     */
    public void print(int lengthHeader, int lengthSequence, int[] lines) {
        if (!this.isEmpty()) {
            int begin = 0;
            int end = lengthSequence - 1;
            int max = 0;
            for(int i = 0; i < this.getLength(); i++)
                max = Math.max(max, this.getSequence(i).getLength());

            String spacer = "     ";

            do{
                String beginString = String.valueOf(begin+1);
                String endString = String.valueOf(end+1);
                int spaceBeginEnd = lengthSequence-beginString.length()-endString.length();
                System.out.println(this.spaces(lengthHeader)+spacer+beginString+this.spaces(spaceBeginEnd)+endString);

                if(lines != null) {
                    for(int i = 0; i < lines.length; i++) {
                        int line = lines[i]-1;
                        if(line < this.getLength()) {
                            System.out.print(formatHeader(lengthHeader, this.getHeader(line).toString()) + spacer);
                            System.out.println(this.getSequence(line).substring(begin, end + 1));
                        }
                    }
                } else {
                    for (int i = 0; i < this.getLength(); i++) {
                        System.out.print(formatHeader(lengthHeader, this.getHeader(i).toString()) + spacer);
                        System.out.println(this.getSequence(i).substring(begin, end + 1));
                    }
                }
                System.out.println();
                begin += lengthSequence;
                end += lengthSequence;
            } while(end < max+lengthSequence);
        }
    }

    public String formatString(int lineLength, String line) {
        int seqLength = line.length();
        int lineCount = seqLength / lineLength;
        String formatString = "";
        for(int i = 0; i < lineCount; i++) {
            formatString += line.substring(i * lineLength, (i+1) * lineLength) + "\n";
        }
        formatString += line.substring(lineCount * lineLength, seqLength);
        return formatString.trim();
    }

    /**
     * format a String that it is not longer than length
     * if longer, add '...' at the end of the header to indicate
     * that its not the full header that is printed.
     * @param length
     * @param header
     * @return
     */
    public String formatHeader(int length, String header) {
        if(header.startsWith(">"))
            header = header.substring(1);
        if(header.length() > length)
            return header.substring(0,length-3) + "...";
        else
            return header+this.spaces(length-header.length());
    }

    /**
     * Print single header+sequence pair at index  with max line length linelength
     * @param index
     * @param lineLength
     */
    public void printEntry(int index, int lineLength) {
        System.out.println(formatHeader(lineLength, this.getHeader(index).toString()));
        System.out.println(formatString(lineLength, this.getSequence(index).toString()));
    }
}
