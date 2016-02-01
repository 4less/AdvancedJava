

package molecules;

import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jogi on 24.01.2016.
 */
public class Uracil extends Nucleotide {
    { baseCoordinates = new Point3D[11]; }
    private static final HashMap<String,Integer> baseMap = new HashMap<>();
    {   baseMap.put("N1", 0);
        baseMap.put("C2", 1);
        baseMap.put("O2", 2);
        baseMap.put("N3", 3);
        baseMap.put("C4", 4);
        baseMap.put("O4", 5);
        baseMap.put("C5", 6);
        baseMap.put("C6", 7);
        baseMap.put("H3", 8);
        baseMap.put("H5", 9);
        baseMap.put("H6", 10);   }

    private static HashMap<String, ArrayList<String>> bondMap = new HashMap<>();
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("C6");
        list.add("C2");
        list.add("C1'");
        bondMap.put("N1", list);
        list = new ArrayList<>();
        list.add("N1");
        list.add("N3");
        list.add("O2");
        bondMap.put("C2", list);
        list = new ArrayList<>();
        list.add("C2");
        bondMap.put("O2", list);
        list = new ArrayList<>();
        list.add("C2");
        list.add("C4");
        list.add("H3");
        bondMap.put("N3", list);
        list = new ArrayList<>();
        list.add("N3");
        bondMap.put("H3", list);
        list = new ArrayList<>();
        list.add("C5");
        list.add("O4");
        list.add("N3");
        bondMap.put("C4", list);
        list = new ArrayList<>();
        list.add("C4");
        bondMap.put("O4", list);
        list = new ArrayList<>();
        list.add("C6");
        list.add("C4");
        list.add("H5");
        bondMap.put("C5", list);
        list = new ArrayList<>();
        list.add("C5");
        bondMap.put("H5", list);
        list = new ArrayList<>();
        list.add("N1");
        list.add("C5");
        list.add("H6");
        bondMap.put("C6", list);
        list = new ArrayList<>();
        list.add("C6");
        bondMap.put("H6", list);


        nucleotideBondsMap.get("C1'").add("N1");
    }

    public boolean checkValidity() {
        if (!super.checkValidity()) return false;
        for (int i = 0; i < baseCoordinates.length ; i++)
            if (baseCoordinates[i] == null) return false;
        return true;
    }

    public ArrayList<String> bondedAtoms(String atom) {
        if (super.bondedAtoms(atom) != null)
            return super.bondedAtoms(atom);
        return bondMap.get(atom);
    }

    @Override
    public Set<String> getAtoms() {
        //atoms.addAll(guanineMap.keySet());
        return Nucleotide.mergeKeySets(super.getAtoms(), baseMap.keySet());
    }

    // Getter for index
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
            System.err.println("There is no atom with the name: " + atom);
            return false;
        } else {
            baseCoordinates[baseMap.get(atom)] = coordinates;
            return true;
        }
    }
}
