package molecules;

import javafx.geometry.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jogi on 24.01.2016.
 */
public abstract class Nucleotide {
    // Phosphate
    private Point3D[] nucleotideCoordinates =  new Point3D[19];
    protected Point3D[] baseCoordinates;

    private static final HashMap<String, Integer> atomMap = new HashMap<>();
    {   atomMap.put("P", 0);
        atomMap.put("OP1", 1);
        atomMap.put("OP2", 2);
        atomMap.put("O5'", 3);
        atomMap.put("C5'", 4);
        atomMap.put("C4'", 5);
        atomMap.put("O4'", 6);
        atomMap.put("C3'", 7);
        atomMap.put("O3'", 8);
        atomMap.put("C2'", 9);
        atomMap.put("O2'", 10);
        atomMap.put("C1'", 11);
        atomMap.put("H5'", 12);
        atomMap.put("H5''", 13);
        atomMap.put("H4'", 14);
        atomMap.put("H3'", 15);
        atomMap.put("H2'", 16);
        atomMap.put("HO2'", 17);
        atomMap.put("H1'", 18); }

    protected static HashMap<String, ArrayList<String>> nucleotideBondsMap = new HashMap<>();
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("OP1");
        list.add("OP2");
        list.add("O5'");
        nucleotideBondsMap.put("P", list);
        list = new ArrayList<>();
        list.add("P");
        nucleotideBondsMap.put("OP1", list);
        list = new ArrayList<>();
        list.add("P");
        nucleotideBondsMap.put("OP2", list);
        list = new ArrayList<>();
        list.add("P");
        list.add("C5'");
        nucleotideBondsMap.put("O5'", list);
        list = new ArrayList<>();
        list.add("O5'");
        list.add("H5'");
        list.add("H5''");
        list.add("C4'");
        nucleotideBondsMap.put("C5'", list);
        list = new ArrayList<>();
        list.add("C5'");
        nucleotideBondsMap.put("H5'", list);
        list = new ArrayList<>();
        list.add("C5'");
        nucleotideBondsMap.put("H5''", list);
        list = new ArrayList<>();
        list.add("C5'");
        list.add("O4'");
        list.add("C3'");
        list.add("H4'");
        nucleotideBondsMap.put("C4'", list);
        list = new ArrayList<>();
        list.add("C4'");
        nucleotideBondsMap.put("H4'", list);
        list = new ArrayList<>();
        list.add("C1'");
        list.add("C4'");
        nucleotideBondsMap.put("O4'", list);
        list = new ArrayList<>();
        list.add("C2'");
        list.add("C4'");
        list.add("H3'");
        list.add("O3'");
        nucleotideBondsMap.put("C3'", list);
        list = new ArrayList<>();
        list.add("C3'");
        nucleotideBondsMap.put("O3'", list);
        list = new ArrayList<>();
        list.add("C3'");
        nucleotideBondsMap.put("H3'", list);
        list = new ArrayList<>();
        list.add("C1'");
        list.add("O2'");
        list.add("H2'");
        list.add("C3'");
        nucleotideBondsMap.put("C2'", list);
        list = new ArrayList<>();
        list.add("HO2'");
        list.add("C2'");
        nucleotideBondsMap.put("O2'", list);
        list = new ArrayList<>();
        list.add("C2'");
        nucleotideBondsMap.put("H2'", list);
        list = new ArrayList<>();
        list.add("O2'");
        nucleotideBondsMap.put("HO2'", list);
        list = new ArrayList<>();
        //list.add("N1");
        list.add("O4'");
        list.add("C2'");
        list.add("H1'");
        nucleotideBondsMap.put("C1'", list);
        list = new ArrayList<>();
        list.add("C1'");
        nucleotideBondsMap.put("H1'", list);
    }

    public static final double WC_MAX_DISTANCE = 3.1;
    public static final double WC_MIN_ANGLE = 110;

    public boolean checkValidity() {
        for (int i = 0; i < nucleotideCoordinates.length ; i++)
            if (nucleotideCoordinates[i] == null) return false;
        return true;
    }

    public ArrayList<String> bondedAtoms(String atom) {
        return nucleotideBondsMap.get(atom);
    }

    public Set<String> getAtoms() {
        return atomMap.keySet();
    }

    public static Set<String> mergeKeySets(Set<String> a, Set<String> b) {
        Set<String> newSet = new HashSet<>();
        for (String s : a)
            newSet.add(s);
        for (String s : b)
            newSet.add(s);
        return newSet;
    }

    public Point3D get(String atom) {
        return (atomMap.get(atom) == null ? null : nucleotideCoordinates[atomMap.get(atom)]);
    }

    public boolean set(String atom, Point3D coordinates) {
        if (atomMap.get(atom) == null) {
            System.err.println("There is no atom with the name: " + atom);
            return false;
        } else {
            nucleotideCoordinates[atomMap.get(atom)] = coordinates;
            return true;
        }
    }

    public abstract String[] getWcDonor();
    public abstract String[] getWcAcceptor();
    public abstract String[] getWcDonorH();

    public static boolean isWatsonCrick(Nucleotide a, Nucleotide b) {
        boolean result = false;
        double dist, angle;
        Point3D acc, don, hdon;

        for (int i = 0; i < a.getWcDonor().length && i < b.getWcAcceptor().length; i++) {
            String h = "H" + a.getWcDonor()[i].substring(1,2);
            don = a.get(a.getWcDonor()[i]);
            acc = b.get(b.getWcAcceptor()[i]);

            if (a.get(h) != null) {
                hdon = a.get(h);
                dist = hdon.distance(acc);
                angle = hdon.angle(acc, don);
                if (dist > Nucleotide.WC_MAX_DISTANCE ||
                        angle < Nucleotide.WC_MIN_ANGLE)
                    return false;
            } else if (a.get(h+"1") != null) {
                dist = Math.min(a.get(h+"1").distance(acc), a.get(h+"2").distance(acc));
                angle = Math.max(a.get(h+"1").angle(acc, don), a.get(h+"2").angle(acc,don));

                if (dist > Nucleotide.WC_MAX_DISTANCE ||
                        angle < Nucleotide.WC_MIN_ANGLE)
                    return false;
            }
        }
        for (int i = 0; i < a.getWcAcceptor().length && i < b.getWcDonor().length; i++) {
            String h = "H" + b.getWcDonor()[i].substring(1,2);
            don = b.get(b.getWcDonor()[i]);
            acc = a.get(a.getWcAcceptor()[i]);

            if (b.get(h) != null) {
                hdon = b.get(h);
                dist = hdon.distance(acc);
                angle = hdon.angle(acc, don);
                if (dist > Nucleotide.WC_MAX_DISTANCE ||
                        angle < Nucleotide.WC_MIN_ANGLE)
                    return false;
            } else if (b.get(h+"1") != null) {
                    dist = Math.min(b.get(h + "1").distance(acc), b.get(h + "2").distance(acc));
                    angle = Math.max(b.get(h+"1").angle(acc, don), b.get(h+"2").angle(acc,don));

                if (dist > Nucleotide.WC_MAX_DISTANCE ||
                        angle < Nucleotide.WC_MIN_ANGLE)
                    return false;
            }
            result = true;
        }
        System.err.println("Hit");
        return result;
    }

    public Point3D[] getBaseCoordinates() {
        return baseCoordinates;
    }

    public Point3D[] getNucleotideCoordinates() {
        return nucleotideCoordinates;
    }
}
