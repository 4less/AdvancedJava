package io;

import java.io.*;

/**
 * Created by Joachim on 09/12/2015.
 */
public class PDBParser {
    public static PDB parse(File pdbFile) throws IOException {
        PDB pdb = new PDB();
        PDBModel model;
        BufferedReader pdbReader = new BufferedReader(new FileReader(pdbFile));
        String line;
        while ((line = pdbReader.readLine()) != null) {
            if (line.startsWith("MODEL")) {
                pdb.getModels().add(parseModel(line));
            }
            if (line.startsWith("ATOM")) {
                if (pdb.getModels().isEmpty())
                    pdb.getModels().add(new PDBModel(-1));
                pdb.getAtoms(pdb.getModels().size()-1).add(parseAtom(line));
            } else if (line.startsWith("CONECT")) {
                pdb.getConect().add(parseConect(line));
            }
        }
        return pdb;
    }

    private static PDBConect parseConect(String line) {
        char[] lineArray = line.toCharArray();

        int serial1 = -1;
        int serial2 = -1;
        int serial3 = -1;
        int serial4 = -1;
        int serial5 = -1;

        if (!subString(lineArray,7,11).trim().isEmpty())
            serial1 = Integer.parseInt(subString(lineArray, 7,11).trim());
        if (!subString(lineArray,12,16).trim().isEmpty())
            serial2 = Integer.parseInt(subString(lineArray, 12,16).trim());
        if (!subString(lineArray,17,21).trim().isEmpty())
            serial3 = Integer.parseInt(subString(lineArray, 17,21).trim());
        if (!subString(lineArray,22,26).trim().isEmpty())
            serial4 = Integer.parseInt(subString(lineArray,22 ,26).trim());
        if (!subString(lineArray,27,31).trim().isEmpty())
            serial5 = Integer.parseInt(subString(lineArray,27 ,31).trim());

        return new PDBConect(
                subString(lineArray,1,6).trim(),
                serial1,
                serial2,
                serial3,
                serial4,
                serial5);
    }

    private static PDBAtom parseAtom(String line) {
        char[] lineArray = line.toCharArray();
        int serial = -1;
        int resSeq = -1;
        float x = 0.0f;
        float y = 0.0f;
        float z = 0.0f;
        double occupancy = 0.0;
        double tempFactor = 0.0;


        if (!subString(lineArray,7,11).trim().isEmpty())
            serial = Integer.parseInt(subString(lineArray,7,11).trim());
        if (!subString(lineArray,23,26).trim().isEmpty())
            resSeq = Integer.parseInt(subString(lineArray,23,26).trim());
        if (!subString(lineArray,31,38).trim().isEmpty())
            x = Float.parseFloat(subString(lineArray, 31, 38).trim());
        if (!subString(lineArray,39,46).trim().isEmpty())
            y = Float.parseFloat(subString(lineArray, 39, 46).trim());
        if (!subString(lineArray,47,54).trim().isEmpty())
            z = Float.parseFloat(subString(lineArray, 47, 54).trim());
        if (!subString(lineArray,55,60).trim().isEmpty())
            occupancy = Double.parseDouble(subString(lineArray,55,60).trim());
        if (!subString(lineArray,61,66).trim().isEmpty())
            tempFactor = Double.parseDouble(subString(lineArray,61,66).trim());

        return new PDBAtom(
                subString(lineArray,1,6).trim(),
                serial,
                subString(lineArray,13,16).trim(),
                lineArray[17-1],
                subString(lineArray,18,20).trim(),
                lineArray[22-1],
                resSeq,
                lineArray[27-1],
                x,
                y,
                z,
                occupancy,
                tempFactor,
                subString(lineArray, 77, 78).trim(),"");
                //subString(lineArray, 79, 80).trim());
    }

    private static PDBModel parseModel(String line) {
        char[] lineArray = line.toCharArray();
        int modelNum = -1;
        if (!subString(lineArray, 11, 14).trim().isEmpty() && lineArray.length >= 13) {
            modelNum = Integer.parseInt(subString(lineArray,11,14).trim());
        }
        return new PDBModel(modelNum);
    }

    private static String subString(char[] string, int beginCol, int endCol) {
        StringBuilder sBuilder = new StringBuilder();
        for (int i = beginCol; i <= endCol; i++)
            if (string[i-1] != ' ')
                sBuilder.append(string[i-1]);
        return sBuilder.toString();
    }
}
