package io;
import java.util.Arrays;
import java.util.Vector;

/**
 *
 */
public abstract class Nucleotide  {
    private char nucleotide;
    private byte[] sigma;

    /**
     * Constructor for class Nucleotide
     * @param c char
     * @throws Exception if c is not element alphabet SIGMA
     */
    public Nucleotide(char c) throws Exception {
        this.nucleotide = c;
    }

    protected void setSigma(byte[] sigma) {
        this.sigma = sigma;
    }

    public void complement() {
        switch (this.nucleotide) {
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

    protected void setNucleotide(char c) {
        this.nucleotide = c;
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
        return 0 <= Arrays.binarySearch(this.sigma, (byte) this.nucleotide);
    }


    /**
     * Getter for Nucleotide
     * @return char nucleotide
     */
    public char getNucleotide() {
        return nucleotide;
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
        return (Arrays.binarySearch(gc, (byte) this.nucleotide) >= 0);
    }
}
