package moleview;

import Shapes3D.Line3D;
import com.sun.prism.impl.BaseMesh;
import io.PDB;
import io.PDBAtom;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joachim on 14/12/2015.
 */
public class NucleotideMeshs extends Group {
    private static final ArrayList<Point3D> phosphorCoords = new ArrayList<>();
    private static final int SCALE = 1;
    private static final int PHOSPHOR_RADIUS = 1;

    private static final int[] sugarIndizes = {0,1,2,3,4};
    private static final int[] sugarfaces = {
            0, 0, 1, 0, 2, 0,
            0, 0, 2, 0, 3, 0,
            0, 0, 3, 0, 4, 0,
            0, 0, 2, 0, 1, 0,
            0, 0, 3, 0, 2, 0,
            0, 0, 4, 0, 3, 0,
    };

    private static final int[] purinIndizes = {5,6,7,8,9,10,11,12,13};
    private static final int[] purinfaces = {
            0, 0, 1, 0, 5, 0,
            1, 0, 4, 0, 5, 0,
            1, 0, 2, 0, 4, 0,
            2, 0, 3, 0, 4, 0,
            0, 0, 5, 0, 1, 0,
            1, 0, 5, 0, 4, 0,
            1, 0, 4, 0, 2, 0,
            2, 0, 4, 0, 3, 0,
            4, 0, 7, 0, 6, 0,
            4, 0, 8, 0, 7, 0,
            4, 0 , 3 , 0, 8, 0,
            4, 0, 6, 0, 7, 0,
            4, 0, 7, 0, 8, 0,
            4, 0, 8, 0, 3, 0
    };

    private static final int[] pyrimidineIndizes = {5,6,7,8,9,10};
    private static final int[] pyrimidinefaces = {
            0, 0, 1, 0, 5, 0,
            1, 0, 4, 0, 5, 0,
            1, 0, 2, 0, 4, 0,
            2, 0, 3, 0, 4, 0,
            0, 0, 5, 0, 1, 0,
            1, 0, 5, 0, 4, 0,
            1, 0, 4, 0, 2, 0,
            2, 0, 4, 0, 3, 0,
    };

    private static Material material;

    private static final Material materialG = new PhongMaterial(Color.GREEN);
    private static final Material materialA = new PhongMaterial(Color.BLUE);
    private static final Material materialU = new PhongMaterial(Color.YELLOW);
    private static final Material materialC = new PhongMaterial(Color.RED);

    public NucleotideMeshs(PDB pdb) {
        parsePDB(pdb);
    }

    private void scalePoints(Point3D[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] != null) {
                points[i] = points[i].multiply(SCALE);
            }

        }
    }

    private void parsePDB(PDB pdb) {
        int resSeq = Integer.MIN_VALUE;
        boolean corrupt = false;
        Point3D[] coordinates = new Point3D[15];
        Sphere phosphor = null;

        float[] texCoords = {
                0, 1,
                0.5f, 0.5f,
                0.5f, 1
        };


        for (PDBAtom atom : pdb.getAtoms()) {
            //System.out.println(atom.getName());
            if (resSeq != atom.getResSeq()) {
                //printCoordinates(coordinates);
                //printAsArray(coordinates);
                //System.out.println("######" + atom.getResName() + "#####################");
                resSeq = atom.getResSeq();
                coordinates = new Point3D[15];
                phosphor = null;
                corrupt = false;
            }
            if (!corrupt) {
                switch (atom.getName()) {
                    case "P":
                        coordinates[14] = atom.getPoint3D();
                        break;
                    case "C1'":
                        coordinates[0] = atom.getPoint3D();
                        break;
                    case "C2'":
                        coordinates[1] = atom.getPoint3D();
                        break;
                    case "C3'":
                        coordinates[2] = atom.getPoint3D();
                        break;
                    case "C4'":
                        coordinates[3] = atom.getPoint3D();
                        break;
                    case "O4'":
                        coordinates[4] = atom.getPoint3D();
                        break;
                    case "N1":
                        coordinates[5] = atom.getPoint3D();
                        break;
                    case "C2":
                        coordinates[6] = atom.getPoint3D();
                        break;
                    case "N3":
                        coordinates[7] = atom.getPoint3D();
                        break;
                    case "C4":
                        coordinates[8] = atom.getPoint3D();
                        break;
                    case "C5":
                        coordinates[9] = atom.getPoint3D();
                        break;
                    case "C6":
                        coordinates[10] = atom.getPoint3D();
                        break;
                    case "N7":
                        coordinates[11] = atom.getPoint3D();
                        break;
                    case "C8":
                        coordinates[12] = atom.getPoint3D();
                        break;
                    case "N9":
                        coordinates[13] = atom.getPoint3D();
                        break;
                }
            }

            if (coordinates[14] != null && isSugarValid(coordinates) && isBaseValid(coordinates, atom.getResName()) && !corrupt) {
                scalePoints(coordinates);

                TriangleMesh sugarMesh = new TriangleMesh();
                TriangleMesh baseMesh = new TriangleMesh();
                sugarMesh.getPoints().addAll(coordinateArray(coordinates, sugarIndizes));
                sugarMesh.getFaces().addAll(sugarfaces);


                switch (atom.getResName()) {
                    case "G":
                        material = materialG;
                        baseMesh.getPoints().addAll(coordinateArray(coordinates, purinIndizes));
                        baseMesh.getFaces().addAll(purinfaces);
                        break;
                    case "A":
                        material = materialA;
                        baseMesh.getPoints().addAll(coordinateArray(coordinates, purinIndizes));
                        baseMesh.getFaces().addAll(purinfaces);
                        break;
                    case "U":
                        material = materialU;
                        baseMesh.getPoints().addAll(coordinateArray(coordinates, pyrimidineIndizes));
                        baseMesh.getFaces().addAll(pyrimidinefaces);
                        break;
                    case "C":
                        material = materialC;
                        baseMesh.getPoints().addAll(coordinateArray(coordinates, pyrimidineIndizes));
                        baseMesh.getFaces().addAll(pyrimidinefaces);
                        break;
                }
                sugarMesh.getTexCoords().addAll(texCoords);
                baseMesh.getTexCoords().addAll(texCoords);
                System.out.println("ResSeq: " + atom.getResSeq());

                //Tooltips
                Tooltip tooltip = new Tooltip(atom.getResName() + atom.getResSeq());

                MeshView sugar = createMesh(sugarMesh);
                Tooltip.install(sugar, tooltip);
                MeshView base = createMesh(baseMesh);
                Tooltip.install(base, tooltip);
                phosphor = createSphere(coordinates[14]);
                Tooltip.install(phosphor, tooltip);

                //add connections
                this.addConnections(atom.getResName(), coordinates);

                this.getChildren().addAll(sugar, base, phosphor);
                this.phosphorCoords.add(coordinates[14]);
                corrupt = true;
            }
        }
        connectPhosphorAtoms();
    }

    private void connectPhosphorAtoms() {
        Line3D line;
        for (int current = 1, previous = 0; current < phosphorCoords.size(); current++, previous++) {
            System.out.println(phosphorCoords.get(previous).distance(phosphorCoords.get(current)));
            if (phosphorCoords.get(previous).distance(phosphorCoords.get(current)) <= 8.0 / SCALE) {
                System.out.println(phosphorCoords.get(previous) + " : " + phosphorCoords.get(current));
                line = new Line3D(phosphorCoords.get(previous), phosphorCoords.get(current), PHOSPHOR_RADIUS / 5.0);
                line.setMaterial(new PhongMaterial(Color.PURPLE));
                this.getChildren().add(line);
            }
        }
    }

    private MeshView createMesh(TriangleMesh mesh) {
        MeshView newMesh = new MeshView(mesh);
        newMesh.setCullFace(CullFace.BACK);
        newMesh.setMaterial(material);
        newMesh.setDrawMode(DrawMode.FILL);

        return newMesh;
    }

    private Sphere createSphere(Point3D point) {
        Sphere result = new Sphere();
        result.setRadius(PHOSPHOR_RADIUS);
        result.setTranslateX(point.getX());
        result.setTranslateY(point.getY());
        result.setTranslateZ(point.getZ());
        result.setDrawMode(DrawMode.FILL);
        result.setMaterial(new PhongMaterial(Color.PURPLE));
        return result;
    }

    private void printCoordinates(Point3D[] coordinates) {
        System.out.println("#################################");
        System.out.println("coordinates length. " + coordinates.length);
        for (Point3D point : coordinates) {
            if (point != null) {
                System.out.println(point.getX() + ", " + point.getY() + ", " + point.getZ());
            } else {
                System.out.println("null");
            }
        }
    }

    private void printAsArray(Point3D[] coordinates) {
        System.out.println("float[] coordinates = {");
        for (Point3D point : coordinates) {
            if (point != null) {
                System.out.println(point.getX() + ", " + point.getY() + ", " + point.getZ() + ",");
            } else {
                System.out.println("null");
            }
        }
        System.out.println("}");
    }

    private boolean isSugarValid(Point3D[] sugar) {
        for (int i : sugarIndizes)
            if (sugar[i] == null) return false;
        return true;
    }

    private boolean isBaseValid(Point3D[] bases, String base) {
        switch (base) {
            case "G":
                return checkPurine(bases);
            case "A":
                return checkPurine(bases);
            case "U":
                return checkPyrimidine(bases);
            case "C":
                return checkPyrimidine(bases);
        }
        return false;
    }

    private void addConnections(String atom, Point3D[] coordinates) {
        double radius = 0.05;
        //Material material = new PhongMaterial(Color.WHITE);
        Line3D phosphorsugar = new Line3D(coordinates[14], coordinates[3], radius);
        phosphorsugar.setMaterial(material);
        Line3D sugarbase = null;
        switch(atom) {
            case "G":
            case "A":
                sugarbase = new Line3D(coordinates[13], coordinates[0], radius);
                break;
            case "U":
            case "C":
                sugarbase = new Line3D(coordinates[5], coordinates[0], radius);
                break;
        }
        if (sugarbase != null) {
            sugarbase.setMaterial(material);
            this.getChildren().addAll(phosphorsugar, sugarbase);
        }
    }

    private boolean isPhosphorValid(Sphere phosphor) {
        return phosphor != null;
    }

    private float[] coordinateArray(Point3D[] coordinates, int[] indizes) {
        float[] result = new float[indizes.length * 3];

        for (int i = 0; i < indizes.length; i++) {
            result[3*i] = (float) coordinates[indizes[i]].getX();
            result[3*i+1] = (float) coordinates[indizes[i]].getY();
            result[3*i+2] = (float) coordinates[indizes[i]].getZ();
        }
        return result;
    }

    private boolean checkPyrimidine(Point3D[] bases) {
        for (int i : pyrimidineIndizes)
            if (bases[i] == null) return false;
        return true;
    }

    private boolean checkPurine(Point3D[] bases) {
        for (int i : purinIndizes)
            if (bases[i] == null) return false;
        return true;
    }
}
