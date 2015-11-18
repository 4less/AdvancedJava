package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Created by Joachim on 05/11/2015.
 */
public class ClsrReader extends Clsr {
    FastaReader fReader;

    public void setfReader(FastaReader fReader) {
        this.fReader = fReader;
        fReader.initSequenceMap();
    }

    public void read(BufferedReader clusterReader) throws Exception {
        boolean isFReader = fReader != null;
        String line = null;
        String header = null;
        String nt;
        String sequence = null;
        String similarity = null;
        Cluster cluster = null;

        while((line = clusterReader.readLine()) != null) {
            if (line.startsWith(">")) {
                header = line.substring(1);
                cluster = new Cluster();
                cluster.setName(header);
                this.addCluster(cluster);
            } else {
                String[] split = line.split("(\\d+\t)|( at )|(, >)|( )");
                if (split.length == 4) {
                    nt = split[1];
                    if(split[2].matches(".*\\.")) {
                        sequence = split[2].substring(0, split[2].indexOf("."));
                    }
                    similarity = split[3].trim();
                    if (similarity.equals("*")) {
                        Sequence seq = this.fReader.getSequence(sequence);
                        assert cluster != null;
                        if (isFReader) {
                            cluster.setRepresentative(new ClusterSequence(sequence, seq, similarity));
                        } else {
                            cluster.setRepresentative(new ClusterSequence(sequence, similarity));
                        }
                    }
                    else {
                        similarity = similarity.split("\\+/")[1];
                        similarity = similarity.substring(0, similarity.indexOf("%"));
                        if (similarity.matches("\\d+(\\.\\d+)?") && cluster != null) {
                            Sequence seq = this.fReader.getSequence(sequence);
                            if(seq == null)
                                continue;
                            ClusterSequence toAdd;
                            if (isFReader) {
                                toAdd = new ClusterSequence(sequence, seq, similarity);
                            } else {
                                toAdd = new ClusterSequence(sequence, similarity);
                            }
                            cluster.addSequence(toAdd);
                        }
                    }
                }
            }
        }
    }

    public void print() {
        for(int i = 0; i < this.getCluster().size(); i++) {
            Cluster cluster = this.get(i);
            cluster.getRepresentative().print();
            for (int j = 0; j < cluster.getLength(); j++) {
                cluster.get(j).print();
            }
        }
    }

    public FastaReader getfReader() {
        return fReader;
    }

    public static void main(String[] args) throws Exception {
        ClsrReader cReader = new ClsrReader();
        cReader.setfReader(new FastaReader("staph_aur_aur_16S.fasta"));
        cReader.getfReader().print();
        for(String header : cReader.getfReader().getSequenceMap().keySet()) {
            System.out.print(header + "   ");
            System.out.print(cReader.getfReader().getSequence(header).toString());
        }
        BufferedReader bReader = new BufferedReader(new FileReader(new File("staph_aur_aur_16S_100.clsr")));
        cReader.read(bReader);
        cReader.print();
    }
}
