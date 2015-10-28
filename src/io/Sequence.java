package io;

import java.util.Vector;

/**
 * Created by Joachim on 20/10/2015.
 *
 * Class Sequence representing a sequence of Nucleotides.
 * @param header    yields the header for the sequence
 * @param sequence  Vector<Nucleotide> yielding a sequence of Nucleotides
 * @param length    int length of sequence
 */
public class Sequence {
    private Header header;
    private Vector<Nucleotide> sequence;
    private int length;

    /**
     * Constructor without any arguments. Initiate new Sequence and Header object
     */
    public Sequence() {
        this.header = new Header(">", this);
        this.sequence = new Vector<Nucleotide>();
        this.length = 0;
    }

    /**
     * initiate Sequence with header by given String and empty sequence
     * @param header
     */
    public Sequence(String header) {
        this.header = new Header(header, this);
        this.sequence = new Vector<Nucleotide>();
        this.length = 0;
    }

    /**
     * Constructor for Sequence
     * @param sequence  String sequence to construct Sequence object
     * @param header    String header to construct Header object
     * @throws Exception
     */
    public Sequence(String sequence, String header) throws Exception {
        this.header = new Header(header, this);
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
            for (int i = begin; i < end; i++) {
                result += sequence.get(i);
            }
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
     * @return
     */
    public boolean isEmpty() {
        return this.sequence == null || this.sequence.isEmpty();
    }

    /**
     * Return corresponding Header object
     * @return
     */
    public Header getHeader() {
        return header;
    }

    /**
     * get Length of sequence
     * @return
     */
    public int getLength() {
        return length;
    }
}
