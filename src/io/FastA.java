package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.io.FileReader;

/**
 * Created by Joachim on 16/10/2015.
 *
 * Class FastA yielding a Vector of Header s and a Vector of their corresponding Sequence s
 * length is the amount of stored sequenes.
 */

public class FastA {
    private Vector<Header> headers = new Vector<Header>();
    private Vector<Sequence> sequences = new Vector<Sequence>();
    private int length = 0;

    public void addElement(Header header, Sequence sequence) {
        if(header.check(sequence)) {
            this.headers.addElement(header);
            this.sequences.addElement(sequence);
            this.length++;
        }
    }

    /**
     * add Element to sequence and also store corresponding header
     * @param sequence
     */
    public void addElement(Sequence sequence) {
        this.sequences.addElement(sequence);
        this.headers.addElement(sequence.getHeader());
        this.length++;
    }

    /**
     * add Element to header and also store corresponding sequence
     * @param header
     */
    public void addElement(Header header) {
        this.headers.addElement(header);
        this.sequences.addElement(header.getSequence());
        this.length++;
    }

    /**
     * add element to sequence and header and check if the correspond
     * @param header
     * @param sequence
     */
    public void add(Header header, Sequence sequence) {
        if(header.check(sequence)) {
            this.headers.add(header);
            this.sequences.add(sequence);
        }
    }

    /**
     * Overwrite Header and Sequence information at position index
     * @param index
     * @param header
     * @param sequence
     * @return
     */
    public boolean set(int index, Header header, Sequence sequence) {
        if (index < this.length && header.check(sequence)) {
            this.headers.set(index, header);
            this.sequences.set(index, sequence);
            return true;
        } else
            return false;
    }

    /**
     * Empty headers sequences and set length back to 0
     */
    public void clear() {
        this.headers.clear();
        this.sequences.clear();
        this.length = 0;
    }

    /**
     * check if FastA object is empty
     * @return
     */
    public boolean isEmpty() {
        return length == 0 && headers.isEmpty() || sequences.isEmpty();
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
     * @return Header object
     */
    public Header getHeader(int index) {
        if(index < this.length) {
            return this.headers.get(index);
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