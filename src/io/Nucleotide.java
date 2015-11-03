package io;
import java.util.Vector;

/**
 *
 */
public class Nucleotide {
    private char nucleotide;
    private String SIGMA = "[-AaCcGgTtUu]";

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

    public Nucleotide(char c, String sigma) throws Exception {
        this.SIGMA = sigma;
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
     * Is nucleotide an element of  the set SIGMA
     * @return true, false
     */
    public boolean isValid() {
        return String.valueOf(this.nucleotide).matches(SIGMA);
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
    public String getSIGMA() {
        return SIGMA;
    }

    /**
     * Print SIGMA to the console.
     */
    public void printSigma() {
        System.out.print("Sigma = {");
        if(!SIGMA.isEmpty() && SIGMA.length() != 0) {
            String sigma = SIGMA.replaceAll("[\\[\\]]", "");
            for(int i = 0; i < sigma.length(); i++) {
                System.out.print(sigma.charAt(i));
                if(i < sigma.length()-1)
                    System.out.print(", ");
            }
        }
        System.out.println("}");
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
        if(String.valueOf(this.nucleotide).matches("[GgCc]"))
            return true;
        return false;
    }
}
