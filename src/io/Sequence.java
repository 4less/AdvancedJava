package io;

import java.util.Vector;

public class Sequence {
    private String header;
    private Vector<Nucleotide> sequence;
    private int length;

    /**
     *
     * @param sequence
     * @param header
     * @throws Exception
     */
    public Sequence(String sequence, String header) throws Exception {
        this.header = header;
        this.sequence = new Vector<Nucleotide>();
        int length = sequence.length();
        for(int i = 0; i < length; i++)
            this.sequence.add(new Nucleotide(sequence.charAt(i)));
        this.length = length;
    }

    /**
     * Add 1 Nucleotide to sequence
     * @param n Nucleotide to add
     */
    public void addElement(Nucleotide n) {
        this.sequence.addElement(n);
        this.length++;
    }

    /**
     * Get substring of String representation of sequence
     * @param begin     from index begin
     * @param end       to index end
     * @return String substring, empty String if begin greater than end
     */
    public String substring(int begin, int end) {
        if (begin <= end) {
            if(this.length < end)
                end = this.length;
            String result = "";
            for (int i = begin; i < end; i++)
                result = result + sequence.get(i);
            return result;
        } else
            return "";
    }


    /**
     * Returning String representation of sequence
     * @return
     */
    public String toString() {
        return this.substring(0, this.length);
    }

    /**
     * is this object empty?
     * @return boolean
     */
    public boolean isEmpty() {
        return this.sequence == null || this.sequence.isEmpty();
    }

    /**
     * Getter for header
     * @return String header
     */
    public String getHeader() {
        return header;
    }

    /**
     * get Length of sequence
     * @return int length of sequence
     */
    public int getLength() {
        return length;
    }
}
