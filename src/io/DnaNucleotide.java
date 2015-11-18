package io;

/**
 * Created by Joachim on 11/11/2015.
 */
public class DnaNucleotide extends Nucleotide {
    public DnaNucleotide(char c) throws Exception {
        super(c);
        setSigma(sigma());
        if(!isValid())
            throw new Exception("No valid nucleotide. Check on Sigma for valid nucleotides.");
    }

    public static byte[] sigma() {
        byte[] sigma = {'-','A','C','G','M','N','R','T','Y','a','c','g','t'};
        return sigma;
    }

    public void complement() {
        super.complement();
        switch(getNucleotide()) {
            case 'A':
                this.setNucleotide('T');
                break;
            case 'a':
                this.setNucleotide('t');
                break;
            case 'T':
                this.setNucleotide('A');
                break;
            case 't':
                this.setNucleotide('a');
                break;
        }
    }
}
