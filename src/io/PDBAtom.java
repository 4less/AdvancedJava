package io;

import javafx.geometry.Point3D;

/**
 * Created by Joachim on 09/12/2015.
 */
public class PDBAtom {
    private String recordName; //Name of Type
    private int serial; // Atom serial Number
    private String name; // Atom name
    private char altLoc; // alternate location indicator
    private String resName; // residue name
    private char chainID; // chain identifier
    private int resSeq; // residue sequence number
    private char iCode; // doe for insertion of residues

    private float x,y,z; //Orthogonal coordinates for X in Angstroms
    private double occupancy; // Occupancy
    private double tempFactor; // Temperature factor

    private String element;
    private String charge;

    public PDBAtom(String recordName, int serial, String name, char altLoc, String resName, char chainID, int resSeq, char iCode, float x, float y, float z, double occupancy, double tempFactor, String element, String charge) {
        this.recordName = recordName;
        this.serial = serial;
        this.name = name;
        this.altLoc = altLoc;
        this.resName = resName;
        this.chainID = chainID;
        this.resSeq = resSeq;
        this.chainID = chainID;
        this.iCode = iCode;
        this.x = x;
        this.y = y;
        this.z = z;
        this.occupancy = occupancy;
        this.tempFactor = tempFactor;
        this.element = element;
        this.charge = charge;
    }

    public String getRecordName() {
        return recordName;
    }

    public int getSerial() {
        return serial;
    }

    public String getName() {
        return name;
    }

    public char getAltLoc() {
        return altLoc;
    }

    public String getResName() {
        return resName;
    }

    public char getChainID() {
        return chainID;
    }

    public int getResSeq() {
        return resSeq;
    }

    public char getiCode() {
        return iCode;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Point3D getPoint3D() {
        return new Point3D(x, y, z);
    }

    public double getOccupancy() {
        return occupancy;
    }

    public double getTempFactor() {
        return tempFactor;
    }

    public String getElement() {
        return element;
    }

    public String getCharge() {
        return charge;
    }
}
