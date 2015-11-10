package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader ;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Joachim on 20/10/2015.
 *
 * Class FastaReader extends class FastA and implements methods to read and print Fasta Files
 */
public class FastaReader extends FastA {
    HashMap<String, Sequence> sequenceMap;

    public FastaReader(BufferedReader fastaReader) throws Exception {
        this.read(fastaReader);
    }

    public void clearSequenceMap() {
        this.sequenceMap = null;
    }

    public void initSequenceMap() {
        sequenceMap = new HashMap<>();
        for (Sequence sequence : this)
            this.sequenceMap.put(truncateHeader(sequence.getHeader()), sequence);
    }

    public Sequence getSequence(String header) {
        return this.sequenceMap.get(header);
    }

    public static String truncateHeader(String header) {
        String result = header.substring(1,header.indexOf("."));
        return result;
    }

    /**
     * Constructor loading fasta file by String filePath
     * @param filePath path to fasta file
     * @throws Exception
     */
    public FastaReader(String filePath) throws Exception {
        this.read(filePath);
    }

    public FastaReader() {};

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
        StringBuffer sequence = new StringBuffer();


        while((line = fastaReader.readLine()) != null) {
            if (line.startsWith(">")) {
                if (header != null && sequence != null) {
                    this.addElement(new Sequence(sequence.toString(), header));
                    sequence = new StringBuffer();
                }
                header = line.trim();
            } else {
                sequence.append(line);
            }
        }
        if (header != null && sequence != null) {
            this.addElement(new Sequence(sequence.toString(), header));
        }
    }

    /**
     * Prints stored Sequences with default values of max length 20 for the header,
     * and max length of 60 for the sequence before breaking the line.
     */
    public void print() {
        int[] lines =  {1,2,3,4,5,6};
        System.out.println(this.print(20, 140, lines, true, true, true));
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
        // makes no sense to display numbers when sequences are disabled
        if(!sequence)
            numbers = false;
        StringBuilder result = new StringBuilder();
        //String result = "";
        if (!this.isEmpty()) {
            int begin = 0;
            int end = lengthSequence - 1;
            int max = 0;
            for(int i = 0; i < this.getLength(); i++)
                max = Math.max(max, this.getSequence(i).getLength());

            String spacer = "     ";

            int iteration = 0;

            do{
                System.out.println(iteration);
                if(numbers) {
                    String beginString = String.valueOf(begin + 1);
                    String endString = String.valueOf(end + 1);
                    int spaceBeginEnd = lengthSequence - beginString.length() - endString.length();
                    if(header)
                        result.append(this.spaces(lengthHeader) + spacer);
                    result.append(beginString + this.spaces(spaceBeginEnd) + endString + "\n");
                }
                if(lines != null) {
                    for(int i = 0; i < lines.length; i++) {
                        int line = lines[i]-1;
                        if(line < this.getLength()) {
                            if(header)
                                result.append(formatHeader(lengthHeader, this.getHeader(line)));
                            if(sequence) {
                                if(header)
                                    result.append(spacer);
                                result.append(this.getSequence(line).substring(begin, end + 1) + "\n");
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < this.getLength(); i++) {
                        if(header) {
                            result.append((formatHeader(lengthHeader, this.getHeader(i))));
                            if (!sequence)
                                result.append("\n");
                        }
                        if(sequence) {
                            if(header)
                                result.append(spacer);
                            result.append(this.getSequence(i).substring(begin, end + 1) + "\n");
                        }
                    }
                }
                if(!sequence)
                    break;
                result.append("\n");
                begin += lengthSequence;
                end += lengthSequence;
                iteration++;
            } while(end < max+lengthSequence);
        }

        return result.toString();
    }

    /**
     *
     * Format a given string that it breaks a line if it gets too long
     * @param lineLength
     * @param line
     * @return
     */
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
        System.out.println(formatHeader(lineLength, this.getHeader(index)));
        System.out.println(formatString(lineLength, this.getSequence(index).toString()));
    }

    /**
     * get Lines to
     * @param pattern
     * @return
     */
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
