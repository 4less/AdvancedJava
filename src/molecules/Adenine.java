

package molecules;

import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Jogi on 24.01.2016.
 */
public class Adenine extends Nucleotide {
    { baseCoordinates = new Point3D[14]; }
    private static final HashMap<String,Integer> baseMap = new HashMap<>();
    {   baseMap.put("N1", 0);
        baseMap.put("C2", 1);
        baseMap.put("N3", 2);
        baseMap.put("C4", 3);
        baseMap.put("C5", 4);
        baseMap.put("C6", 5);
        baseMap.put("N6", 6);
        baseMap.put("N7", 7);
        baseMap.put("C8", 8);
        baseMap.put("N9", 9);
        baseMap.put("H2", 10);
        baseMap.put("H61", 11);
        baseMap.put("H62", 12);
        baseMap.put("H8", 13);   }

    private static HashMap<String, ArrayList<String>> bondMap = new HashMap<>();
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("C2");
        list.add("C6");
        bondMap.put("N1", list);
        list = new ArrayList<>();
        list.add("N1");
        list.add("N3");
        list.add("H2");
        bondMap.put("C2", list);
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
        list.add("N6");
        bondMap.put("C6", list);
        list = new ArrayList<>();
        list.add("C6");
        list.add("H61");
        list.add("H62");
        bondMap.put("N6", list);
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
        list.add("C2");
        bondMap.put("H2", list);
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
            System.err.println("There is no atom with the name: " + atom);
            return false;
        } else {
            baseCoordinates[baseMap.get(atom)] = coordinates;
            return true;
        }
    }

    @Override
    public String[] getWcDonor() {
        return new String[] {"N6"};
    }

    @Override
    public String[] getWcAcceptor() {
        return new String[]  {"N1"};
    }

    @Override
    public String[] getWcDonorH() {
        return new String[] {"H62"};
    }
}
