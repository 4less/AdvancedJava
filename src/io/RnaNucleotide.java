package io;

/**
 * Created by Joachim on 11/11/2015.
 */
public class RnaNucleotide extends Nucleotide {
    public RnaNucleotide(char c) throws Exception{
        super(c);
        setSigma(sigma());
        if(!isValid())
            throw new Exception("No valid nucleotide. Check on Sigma for valid nucleotides.");
    }

    public static byte[] sigma() {
        byte[] sigma = {'-','A','C','G','M','N','R','U','Y','a','c','g','u'};
        return sigma;
    }

    public void complement() {
        super.complement();
        switch(getNucleotide()) {
            case 'U':
                this.setNucleotide('A');
                break;
            case 'u':
                this.setNucleotide('a');
                break;
            case 'A':
                this.setNucleotide('U');
                break;
            case 'a':
                this.setNucleotide('u');
                break;
        }
    }
}
