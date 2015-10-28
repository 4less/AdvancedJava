package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader ;
import java.util.Arrays;
import java.util.Vector;

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
        System.out.println(this.print(20,60,null,true,true,true));
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
    public String print(int lengthHeader, int lengthSequence, int[] lines, boolean header, boolean sequence, boolean numbers) {
        String result = "";
        if (!this.isEmpty()) {
            int begin = 0;
            int end = lengthSequence - 1;
            int max = 0;
            for(int i = 0; i < this.getLength(); i++)
                max = Math.max(max, this.getSequence(i).getLength());

            String spacer = "     ";

            do{
                if(numbers) {
                    String beginString = String.valueOf(begin + 1);
                    String endString = String.valueOf(end + 1);
                    int spaceBeginEnd = lengthSequence - beginString.length() - endString.length();
                    result += this.spaces(lengthHeader) + spacer + beginString + this.spaces(spaceBeginEnd) + endString + "\n";
                }
                if(lines != null) {
                    for(int i = 0; i < lines.length; i++) {
                        int line = lines[i]-1;
                        if(line < this.getLength()) {
                            if(header)
                                result += formatHeader(lengthHeader, this.getHeader(line).toString()) + spacer;
                            if(sequence)
                                result += this.getSequence(line).substring(begin, end + 1) + "\n";
                        }
                    }
                } else {
                    for (int i = 0; i < this.getLength(); i++) {
                        if(header)
                            result += formatHeader(lengthHeader, this.getHeader(i).toString()) + spacer;
                        if(sequence)
                            result += this.getSequence(i).substring(begin, end + 1) + "\n";
                    }
                }
                result += "\n";
                begin += lengthSequence;
                end += lengthSequence;
            } while(end < max+lengthSequence);
        }

        return result;
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

    public static int[] getLines(String pattern) {
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
}
