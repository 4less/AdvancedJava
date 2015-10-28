package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.io.FileReader;

/**
 *
 */
public class FastA {
    private Vector<Sequence> sequences = new Vector<Sequence>();
    private int length = 0;

    /**
     * add Element to sequence and also store corresponding header
     * @param sequence
     */
    public void addElement(Sequence sequence) {
        this.sequences.addElement(sequence);
        this.length++;
    }


    /**
     * add element to sequence and header
     * @param sequence
     */
    public void add(Sequence sequence) {
        this.sequences.add(sequence);
    }

    /**
     *
     * @param index
     * @param sequence
     * @return
     */
    public boolean set(int index, Sequence sequence) {
        if (index < this.length) {
            this.sequences.set(index, sequence);
            return true;
        } else
            return false;
    }

    /**
     * Empty headers sequences and set length back to 0
     */
    public void clear() {
        this.sequences.clear();
        this.length = 0;
    }

    /**
     * check if FastA object is empty
     * @return
     */
    public boolean isEmpty() {
        return length == 0 || sequences.isEmpty();
    }

    /**
     * return the count of sequences
     * @return
     */
    public int getLength() {
        return length;
    }

    /**
     * return the header at position index
     * @param index
     * @return String header
     */
    public String getHeader(int index) {
        if(index < this.length) {
            return getSequence(index).getHeader();
        }
        return null;
    }

    /**
     * return the sequence at position index
     * @param index
     * @return Sequence object
     */
    public Sequence getSequence(int index) {
        if(index < this.length) {
            return this.sequences.get(index);
        }
        return null;
    }
}