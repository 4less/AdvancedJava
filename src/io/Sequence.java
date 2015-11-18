package io;

import javafx.beans.property.*;

import java.util.Vector;

public class Sequence  {
    private StringProperty header = new SimpleStringProperty(this,"header");
    private Vector<Nucleotide> sequence;
    private NucleotideType type;
    private DoubleProperty gcContent = new SimpleDoubleProperty(this, "gcContent");

    /**
     *
     * @param sequence
     * @param header
     * @throws Exception
     */
    public Sequence(String sequence, String header, NucleotideType type) throws Exception {
        this.header.set(header);
        this.sequence = new Vector<>();
        this.type = type;
        this.setSequence(sequence);
        this.setGcContent();
    }

    public Sequence(String sequence, String header) throws Exception {
        this.header.set(header);
        this.sequence = new Vector<>();
        this.type = NucleotideType.RNA;
        this.setSequence(sequence);
        this.setGcContent();
    }

    public Sequence toRNA() throws Exception {
        this.type = NucleotideType.RNA;
        for (int i = 0; i < sequence.size(); i++) {
            sequence.set(i, new RnaNucleotide(sequence.get(i).getNucleotide()));
        }
        return this;
    }

    public Sequence toDNA() throws Exception {
        this.type = NucleotideType.DNA;
        for (int i = 0; i < sequence.size(); i++) {
            sequence.set(i, new DnaNucleotide(sequence.get(i).getNucleotide()));
        }
        return this;
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
     * @param c char to add
     */
    public void addElement(char c) throws Exception {
        if (type == NucleotideType.DNA)
            this.sequence.addElement(new DnaNucleotide(c));
        if (type == NucleotideType.RNA)
            this.sequence.addElement(new RnaNucleotide(c));
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
            if(this.getLength() < end)
                end = this.getLength();
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
        return this.substring(0, this.getLength());
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
        return header.get();
    }

    /**
     * get Length of sequence
     * @return int length of sequence
     */
    public int getLength() {
        return sequence.size();
    }

    public void setSequence(String sequence) {
        this.sequence.clear();
        for (char c : sequence.toCharArray()) {
            try {
                if (type == NucleotideType.DNA)
                    this.sequence.add(new DnaNucleotide(c));
                else if (type == NucleotideType.RNA)
                    this.sequence.add(new RnaNucleotide(c));
            } catch (Exception e) {
                //System.out.println("failed");
            }
        }
    }

    public Sequence toUpperCase(int begin, int end) {
        if (begin <= end && begin < getLength() && end <= getLength()) {
            for (int i = begin; i < end; i++) {
                getSequence().get(i).toUpperCase();
            }
        }
        return this;
    }

    public Sequence toLowerCase(int begin, int end) {
        if (begin <= end && begin < getLength() && end <= getLength()) {
            for (int i = begin; i < end; i++) {
                getSequence().get(i).toLowerCase();
            }
        }
        return this;
    }


    public Sequence clear() {
        this.sequence.clear();
        return this;
    }

    public Sequence toLowerCase() {
        toLowerCase(0,getLength());
        return this;
    }

    public Sequence toUpperCase() {
        toUpperCase(0, getLength());
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

    public DoubleProperty getGcContentProperty() {
        if (gcContent == null)
            this.gcContent = new SimpleDoubleProperty();
        return gcContent;
    }

    public void setGcContent() {
        if(this.getLength() == 0)
            this.gcContent.set(0.0);
        int gcCount = 0;
        for (Nucleotide n : this.sequence)
            if (n.isGC()) gcCount++;
        this.gcContent.set((double)gcCount/(double)this.getLength());
    }

    public double gcContent() {
        if(this.getLength() == 0) return 0.0;
        int gcCount = 0;
        for (Nucleotide n : this.sequence)
            if (n.isGC()) gcCount++;
        return (double)gcCount/(double)this.getLength();
    }

    public DoubleProperty gcContentProperty() {
        return this.gcContent;
    }

    public void setNucleotideVector(Vector<Nucleotide> nVector) {
        this.sequence = nVector;
    }

    public String toString(int line) {
        return toString(this.toString(), line);
    }

    public static String toString(String s, int line) {
        StringBuilder result = new StringBuilder();
        if(!s.isEmpty()) {
            if (line == 0)
                return s;
            int wraps = s.length() / line;
            for (int i = 0; i < wraps; i++) {
                result.append(s.substring(i * line, i * line + line));
                result.append("\n");
            }
            result.append(s.substring(wraps * line, s.length()));
        }
        return result.toString();
    }

    public void convert(NucleotideType type) throws Exception {
        if (type != this.type) {
            if (getType() == NucleotideType.DNA) {
                setType(NucleotideType.RNA);
                for (int i = 0; i < getSequence().size(); i++) {
                    char c = getSequence().get(i).getNucleotide();
                    if (c == 'T') c = 'U';
                    else if (c == 't') c ='u';
                    getSequence().set(i, new RnaNucleotide(c));
                }
            } else if (getType() == NucleotideType.RNA) {
                setType(NucleotideType.DNA);
                for (int i = 0; i < getSequence().size(); i++) {
                    char c = getSequence().get(i).getNucleotide();
                    if (c == 'U') c = 'T';
                    else if (c == 'u') c ='t';
                    getSequence().set(i, new DnaNucleotide(c));
                }
            }
        }
    }

    public Vector<Nucleotide> getSequence() {
        return sequence;
    }

    public void setType(NucleotideType type) {
        this.type = type;
    }

    public NucleotideType getType() {
        return type;
    }

    public void setHeader(String header) {
        this.header.set(header);
    }
}
