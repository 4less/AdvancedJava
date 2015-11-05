package io;

/**
 * Created by Joachim on 05/11/2015.
 */
public class ClusterSequence {
    private String header;
    private String similarity;

    public ClusterSequence(String header, String similarity) {
        this.header = header;
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
}
