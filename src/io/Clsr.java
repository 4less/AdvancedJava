package io;

import java.util.Vector;

/**
 * Created by Joachim on 05/11/2015.
 */
public class Clsr {
    private Vector<Cluster> cluster = new Vector<Cluster>();
    private int length = 0;

    public void addCluster(Cluster cluster) {
        this.cluster.add(cluster);
        this.length++;
    }

    public Cluster get(int index) {
        if (index < length)
            return cluster.get(index);
        return null;
    }

    public Vector<Cluster> getCluster() {
        return cluster;
    }
}
