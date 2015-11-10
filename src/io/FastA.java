package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.io.FileReader;

/**
 *
 */
public class FastA implements Iterable<Sequence> {
    private List<Sequence> sequences = new ArrayList<Sequence>();

    /**
     * add Element to sequence and also store corresponding header
     *
     * @param sequence
     */
    public void addElement(Sequence sequence) {
        this.sequences.add(sequence);
    }

    /**
     * add element to sequence and header
     *
     * @param sequence
     */
    public void add(Sequence sequence) {
        this.sequences.add(sequence);
    }

    /**
     * @param index
     * @param sequence
     * @return
     */
    public boolean set(int index, Sequence sequence) {
        if (index < this.getLength()) {
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
    }

    /**
     * check if FastA object is empty
     *
     * @return
     */
    public boolean isEmpty() {
        return getLength() == 0 || sequences.isEmpty();
    }

    /**
     * return the count of sequences
     *
     * @return
     */
    public int getLength() {
        return sequences.size();
    }

    /**
     * return the header at position index
     *
     * @param index
     * @return String header
     */
    public String getHeader(int index) {
        if (index < this.getLength()) {
            return getSequence(index).getHeader();
        }
        return null;
    }

    /**
     * return the sequence at position index
     *
     * @param index
     * @return Sequence object
     */
    public Sequence getSequence(int index) {
        if (index < this.getLength()) {
            return this.sequences.get(index);
        }
        return null;
    }

    @Override
    public Iterator<Sequence> iterator() {
        return this.sequences.iterator();
    }
}