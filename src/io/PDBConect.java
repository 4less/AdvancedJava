package io;

/**
 * Created by Joachim on 09/12/2015.
 */
public class PDBConect {
    private String recordName = "CONECT";
    private int serialAtom;
    private int serialBond1;
    private int serialBond2;
    private int serialBond3;
    private int serialBond4;

    public PDBConect(String recordName, int serialAtom, int serialBond1, int serialBond2, int serialBond3, int serialBond4) {
        this.recordName = recordName;
        this.serialAtom = serialAtom;
        this.serialBond1 = serialBond1;
        this.serialBond2 = serialBond2;
        this.serialBond3 = serialBond3;
        this.serialBond4 = serialBond4;
    }

    public String getRecordName() {
        return recordName;
    }

    public int getSerialAtom() {
        return serialAtom;
    }

    public int getSerialBond1() {
        return serialBond1;
    }

    public int getSerialBond2() {
        return serialBond2;
    }

    public int getSerialBond3() {
        return serialBond3;
    }

    public int getSerialBond4() {
        return serialBond4;
    }
}
