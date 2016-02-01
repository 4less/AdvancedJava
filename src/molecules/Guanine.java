

package molecules;

import example.Utilities;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jogi on 24.01.2016.
 */
public class Guanine extends Nucleotide {
    { baseCoordinates = new Point3D[15]; }
    private static final HashMap<String,Integer> baseMap = new HashMap<>();
    {   baseMap.put("N9", 0);
        baseMap.put("C8", 1);
        baseMap.put("N7", 2);
        baseMap.put("C5", 3);
        baseMap.put("C6", 4);
        baseMap.put("O6", 5);
        baseMap.put("N1", 6);
        baseMap.put("C2", 7);
        baseMap.put("N2", 8);
        baseMap.put("N3", 9);
        baseMap.put("C4", 10);
        baseMap.put("H8", 11);
        baseMap.put("H1", 12);
        baseMap.put("H21", 13);
        baseMap.put("H22", 14);  }

    private static HashMap<String, ArrayList<String>> bondMap = new HashMap<>();
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("C2");
        list.add("C6");
        list.add("H1");
        bondMap.put("N1", list);
        list = new ArrayList<>();
        list.add("N1");
        list.add("N3");
        list.add("N2");
        bondMap.put("C2", list);
        list = new ArrayList<>();
        list.add("H21");
        list.add("H22");
        list.add("C2");
        bondMap.put("N2", list);
        list = new ArrayList<>();
        list.add("C2");
        list.add("C4");
        bondMap.put("N3", list);
        list = new ArrayList<>();
        list.add("N3");
        list.add("C5");
        list.add("N9");
        bondMap.put("C4", list);
        list = new ArrayList<>();
        list.add("C4");
        list.add("C6");
        list.add("N7");
        bondMap.put("C5", list);
        list = new ArrayList<>();
        list.add("N1");
        list.add("C5");
        list.add("O6");
        bondMap.put("C6", list);
        list = new ArrayList<>();
        list.add("C6");
        bondMap.put("O6", list);
        list = new ArrayList<>();
        list.add("C5");
        list.add("C8");
        bondMap.put("N7", list);
        list = new ArrayList<>();
        list.add("N7");
        list.add("N9");
        list.add("H8");
        bondMap.put("C8", list);
        list = new ArrayList<>();
        list.add("C1'");
        list.add("C4");
        list.add("C8");
        bondMap.put("N9", list);
        list = new ArrayList<>();
        list.add("N1");
        bondMap.put("H1", list);
        list = new ArrayList<>();
        list.add("N2");
        bondMap.put("H21", list);
        list = new ArrayList<>();
        list.add("N2");
        bondMap.put("H22", list);
        list = new ArrayList<>();
        list.add("N6");
        bondMap.put("H61", list);
        list = new ArrayList<>();
        list.add("N6");
        bondMap.put("H62", list);
        list = new ArrayList<>();
        list.add("C8");
        bondMap.put("H8", list);

        nucleotideBondsMap.get("C1'").add("N9");
    }

    public boolean checkValidity() {
        if (!super.checkValidity()) return false;
        for (int i = 0; i < baseCoordinates.length ; i++)
            if (baseCoordinates[i] == null) return false;
        return true;
    }

    @Override
    public ArrayList<String> bondedAtoms(String atom) {
        if (super.bondedAtoms(atom) != null)
            return super.bondedAtoms(atom);
        return bondMap.get(atom);
    }

    @Override
    public Set<String> getAtoms() {
        //atoms.addAll(baseMap.keySet());
        return Nucleotide.mergeKeySets(super.getAtoms(), baseMap.keySet());
    }

    // Getter for coordinates
    @Override
    public Point3D get(String atom) {
        if (baseMap.get(atom) != null)
            return baseCoordinates[baseMap.get(atom)];
        else return super.get(atom);
    }

    // Setter for coordinates
    @Override
    public boolean set(String atom, Point3D coordinates) {
        if (super.set(atom, coordinates)) return true;
        if (baseMap.get(atom) == null) {
            System.err.println("There is no atom with the name: " + atom + ". baseMap.get(atom): " + baseMap.get("P"));
            return false;
        } else {
            baseCoordinates[baseMap.get(atom)] = coordinates;
            return true;
        }
    }
}
