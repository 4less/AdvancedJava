package io;
import java.util.Vector;

/**
 * Created by Joachim on 20/10/2015.
 * Class Nucleotide yields a char nucleotide out of a specified set SIGMA.
 * @param nucleotide char out of set SIGMA
 * @param SIGMA      String that has to be a regular expression representing an alphabet
 */
public class Nucleotide {
    private char nucleotide;
    private String SIGMA = "[ACGTU-]";

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

    /**
     * Is nucleotide an element of  the set SIGMA
     * @return true, false
     */
    public boolean isValid() {
        return String.valueOf(this.nucleotide).matches(SIGMA);
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
}
