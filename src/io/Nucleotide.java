package io;
import java.util.Arrays;
import java.util.Vector;

/**
 *
 */
public class Nucleotide {
    private char nucleotide;

    private byte[] SIGMA = {'-','A','C','G','T','U','a','c','g','t','u'};


    /**
     * Constructor for class Nucleotide
     * @param c char
     * @throws Exception if c is not element alphabet SIGMA
     */
    public Nucleotide(char c) throws Exception {
        this.nucleotide = c;
        if(!isValid())
            throw new Exception("No valid nucleotide. Check on Sigma for valid nucleotides.");
    }

    public void complement() {
        switch (this.nucleotide) {
            case 'A':   this.nucleotide = 'T';
                        break;
            case 'a':   this.nucleotide = 't';
                        break;
            case 'T':   this.nucleotide = 'A';
                        break;
            case 't':   this.nucleotide = 'a';
                        break;
            case 'G':   this.nucleotide = 'C';
                        break;
            case 'g':   this.nucleotide = 'c';
                        break;
            case 'C':   this.nucleotide = 'G';
                        break;
            case 'c':   this.nucleotide = 'g';
                        break;
        }
    }

    public void toUpperCase() {
        this.nucleotide = Character.toUpperCase(this.nucleotide);
    }

    public void toLowerCase() {
        this.nucleotide = Character.toLowerCase(this.nucleotide);
    }

    /**
     * Is nucleotide an element of  the set io/Nucleotide.java:7SIGMA
     * @return true, false
     */
    public boolean isValid() {
        return 0 <= Arrays.binarySearch(this.SIGMA, (byte) this.nucleotide);
    }


    public void toRNA() {
        switch (nucleotide) {
            case 'T':   this.nucleotide = 'U';
                        break;
            case 't':   this.nucleotide = 'u';
                        break;
        }
    }

    /**
     * Getter for Nucleotide
     * @return char nucleotide
     */
    public char getNucleotide() {
        return nucleotide;
    }

    /**
     * Getter for SIGMA
     * @return the regex SIGMA (String) representing an alphabet
     */
    public byte[] getSIGMA() {
        return SIGMA;
    }


    /**
     * Returns char nucleotide as a String
     * @return String
     */
    public String toString() {
        return String.valueOf(this.nucleotide);
    }

    /**
     * Am I Guanine or Cytosine?
     * @return
     */
    public boolean isGC() {
        byte[] gc = {'C','G','g','c'};
        if(Arrays.binarySearch(gc, (byte) this.nucleotide) >= 0)
            return true;
        return false;
    }
}
