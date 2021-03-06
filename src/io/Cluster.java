package io;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joachim on 04/11/2015.
 */
public class Cluster {
    private String name;
    private ClusterSequence representative;
    private List<ClusterSequence> sequences = new ArrayList<ClusterSequence>();
    private int length = 0;

    public void addSequence(ClusterSequence sequence) {
        sequences.add(sequence);
        length++;
    }

    public void setRepresentative(ClusterSequence sequence) {
        this.representative = sequence;
    }

    public ClusterSequence get(int index) {
        if (index < sequences.size())
            return sequences.get(index);
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClusterSequence getRepresentative() {
        return representative;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public List<ClusterSequence> getSequences() {
        return sequences;
    }
}
