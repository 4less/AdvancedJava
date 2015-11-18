package io;


/**
 * Created by Joachim on 05/11/2015.
 */
public class ClusterSequence {
    private String header;
    private Sequence sequence;
    private int length;
    private String similarity;

    public ClusterSequence(String header, String similarity) {
        this.header = header;
        this.similarity = similarity;
    }

    public ClusterSequence(String header, Sequence sequence, String similarity) {
        this.header = header;
        this.similarity = similarity;
        this.sequence = sequence;
        System.out.println(header);
        this.length = this.sequence.getLength();
        this.similarity = similarity;
    }

    public String getHeader() {
        return header;
    }

    public String getSimilarity() {
        return similarity;
    }
    public void print() {
        System.out.println(header + " " + similarity);
    }

    public int getLength() {
        return length;
    }

    public String getSequenceString() {
        return sequence.toString();
    }
}
