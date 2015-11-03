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
        this.setSequence(sequence);
    }

    public Sequence reverse() {
        Vector<Nucleotide> reverse = new Vector<Nucleotide>();
        for (int i = this.sequence.size()-1; i >= 0; i--)
            reverse.add(this.sequence.get(i));
        this.sequence = reverse;
        return this;
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
        String result = "";
        if (begin < end) {
            if(this.length < end)
                end = this.length;
            for (int i = begin; i < end; i++)
                result = result + sequence.get(i);
            return result;
        } else
            return result;
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

    public void setSequence(String sequence) {
        int length = sequence.length();
        this.sequence.clear();
        for(int i = 0; i < length; i++)
            try {
                this.sequence.add(new Nucleotide(sequence.charAt(i)));
                length++;
            } catch (Exception e) {
            }
        this.length = this.sequence.size();
    }

    public Sequence toUpperCase() {
        this.sequence.forEach(nucleotide -> nucleotide.toUpperCase());
        return this;
    }

    public Sequence clear() {
        this.sequence.clear();
        return this;
    }

    public Sequence toLowerCase() {
        this.sequence.forEach(nucleotide -> nucleotide.toLowerCase());
        return this;
    }

    public Sequence complement() {
        this.sequence.forEach(nucleotide -> nucleotide.complement());
        return this;
    }

    public Sequence reverseComplementary() {
        this.complement();
        this.reverse();
        return this;
    }

    public double gcContent() {
        if(this.length == 0) return 0.0;
        int gcCount = 0;
        for (Nucleotide n : this.sequence)
            if (n.isGC()) gcCount++;
        return (double)gcCount/(double)this.length;
    }

    public Sequence toRNA() {
        this.sequence.forEach(nucleotide -> nucleotide.toRNA());
        return this;
    }

    public String toString(int line) {
        return toString(this.toString(), line);
    }

    public static String toString(String s, int line) {
        String result = "";
        if(!s.isEmpty()) {
            int wraps = s.length() / line;
            for (int i = 0; i < wraps; i++)
                result += s.substring(i * line, i * line + line) + "\n";
            result += s.substring(wraps * line, s.length());
        }
        return result;
    }

}
