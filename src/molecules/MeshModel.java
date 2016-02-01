package molecules;

import Shapes3D.Line3D;
import javafx.geometry.Point3D;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import moleview.Atom3DStickModel;
import moleview.AtomElement;

/**
 * Created by Jogi on 26.01.2016.
 */
public class MeshModel extends DrawModel {
    private static float[] texCoords = {
            0, 1,
            0.5f, 0.5f,
            0.5f, 1
    };

    private static final int PHOSPHOR_RADIUS = 1;

    private static final String[] sugarIndizes = {"C1'","C2'","C3'","C4'","O4'"};
    private static final int[] sugarfaces = {
            0, 0, 1, 0, 2, 0,
            0, 0, 2, 0, 3, 0,
            0, 0, 3, 0, 4, 0,
            0, 0, 2, 0, 1, 0,
            0, 0, 3, 0, 2, 0,
            0, 0, 4, 0, 3, 0,
    };

    private static final String[] purinIndizes = {"N1", "C2", "N3", "C4", "C5", "C6", "N7", "C8", "N9"};
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

    private static final String[] pyrimidineIndizes = {"N1", "C2", "N3", "C4", "C5", "C6"};
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

    private static final Color colorG = Color.GREEN;
    private static final Color colorA = Color.BLUE;
    private static final Color colorU = Color.YELLOW;
    private static final Color colorC = Color.RED;

    private static final Color colorPyr = Color.PURPLE;
    private static final Color colorPur = Color.PINK;

    private static final Color colorDefault = Color.DARKGRAY;

    private static final PhongMaterial materialG = new PhongMaterial(Color.GREEN);
    private static final PhongMaterial materialA = new PhongMaterial(Color.BLUE);
    private static final PhongMaterial materialU = new PhongMaterial(Color.YELLOW);
    private static final PhongMaterial materialC = new PhongMaterial(Color.RED);

    private PhongMaterial material;

    @Override
    public void draw(Nucleotide... nucleotides) {
        Residue res;
        Sphere phosphor = null;
        TriangleMesh sugarMesh;
        TriangleMesh baseMesh;
        String baseName = "";

        for (int i = 0; i < nucleotides.length; i++) {
            res = new Residue();
            sugarMesh = new TriangleMesh();
            baseMesh = new TriangleMesh();

            // Initialize Sugar Mesh
            sugarMesh.getPoints().addAll(coordinateArray(nucleotides[i], sugarIndizes));
            sugarMesh.getFaces().addAll(sugarfaces);

            // initialize the base mesh according to the base
            if (nucleotides[i] instanceof Guanine) {
                baseName = "Guanine";
                baseMesh.getPoints().addAll(coordinateArray(nucleotides[i], purinIndizes));
                baseMesh.getFaces().addAll(purinfaces);
                material = materialG;
            } else if (nucleotides[i] instanceof Adenine) {
                baseName = "Adenine";
                baseMesh.getPoints().addAll(coordinateArray(nucleotides[i], purinIndizes));
                baseMesh.getFaces().addAll(purinfaces);
                material = materialA;
            } else if (nucleotides[i] instanceof Cytosine) {
                baseName = "Cytosine";
                baseMesh.getPoints().addAll(coordinateArray(nucleotides[i], pyrimidineIndizes));
                baseMesh.getFaces().addAll(pyrimidinefaces);
                material = materialC;
            } else if (nucleotides[i] instanceof Uracil) {
                baseName = "Uracil";
                baseMesh.getPoints().addAll(coordinateArray(nucleotides[i], pyrimidineIndizes));
                baseMesh.getFaces().addAll(pyrimidinefaces);
                material = materialU;
            }

            // add texture coordinates
            sugarMesh.getTexCoords().addAll(texCoords);
            baseMesh.getTexCoords().addAll(texCoords);


            MeshView sugar = createMesh(sugarMesh, material);
            MeshView base = createMesh(baseMesh, material);

            if (nucleotides[i].get("P") != null) {
                phosphor = createSphere(nucleotides[i].get("P"));
                res.getChildren().add(phosphor);
                res.setFirst(nucleotides[i].get("P"));
                this.getChildren().add(phosphor);
            }

            //Tooltips
            Tooltip tooltip = new Tooltip((i+1) + " : " + baseName);
            Tooltip.install(res, tooltip);


            //add connections
            this.addConnections(baseName, nucleotides[i], res);
            res.getChildren().addAll(sugar, base);
            residues.add(res);
        }
        addBackBone();
    }

    public void colorPyrPur() {
        materialA.setDiffuseColor(colorPur);
        materialG.setDiffuseColor(colorPur);
        materialC.setDiffuseColor(colorPyr);
        materialU.setDiffuseColor(colorPyr);
    }

    public void colorBases() {
        materialA.setDiffuseColor(colorA);
        materialG.setDiffuseColor(colorG);
        materialC.setDiffuseColor(colorC);
        materialU.setDiffuseColor(colorU);
    }

    private void addConnections(String atom, Nucleotide nucleotide, Residue res) {
        double radius = 0.05;
        if (nucleotide.get("P") != null) {
            Line3D phosphorsugar = new Line3D(nucleotide.get("P"), nucleotide.get("C4'"), radius);
            phosphorsugar.setMaterial(material);
            res.getChildren().add(phosphorsugar);
        }

        Line3D sugarbase = null;
        switch(atom.substring(0,1)) {
            case "G":
            case "A":
                sugarbase = new Line3D(nucleotide.get("N9"), nucleotide.get("C1'"), radius);
                break;
            case "U":
            case "C":
                sugarbase = new Line3D(nucleotide.get("N1"), nucleotide.get("C1'"), radius);
                break;
        }
        if (sugarbase != null) {
            sugarbase.setMaterial(material);
            res.getChildren().addAll(sugarbase);
        }
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

    private MeshView createMesh(TriangleMesh mesh, Material material) {
        MeshView newMesh = new MeshView(mesh);
        newMesh.setCullFace(CullFace.BACK);
        newMesh.setMaterial(material);
        newMesh.setDrawMode(DrawMode.FILL);
        return newMesh;
    }

    private float[] coordinateArray(Nucleotide nucleotide, String[] indizes) {
        float[] result = new float[indizes.length * 3];

        for (int i = 0; i < indizes.length; i++) {
            result[3*i] = (float) nucleotide.get(indizes[i]).getX();
            result[3*i+1] = (float) nucleotide.get(indizes[i]).getY();
            result[3*i+2] = (float) nucleotide.get(indizes[i]).getZ();
        }
        return result;
    }

    public void addBackBone() {
        if (residues.size() > 1) {
            this.getChildren().add(residues.get(0));

            Residue last = residues.get(0);
            Residue current;
            Point3D aPoint;
            Point3D bPoint;
            for (int i = 1; i < residues.size(); i++) {
                this.getChildren().add(residues.get(i));
                current = residues.get(i);

                aPoint = last.getFirst();
                bPoint = current.getFirst();
                Line3D line;

                if (i == 23)
                    System.out.println(aPoint + " : " + bPoint);

                if (aPoint != null && bPoint != null && aPoint.distance(bPoint) < 7) {
                    line = new Line3D(aPoint, bPoint, PHOSPHOR_RADIUS / 5.0);
                    line.setMaterial(new PhongMaterial(Color.PURPLE));
                    this.getChildren().add(line);
                }
                last = current;
            }
        }
    }
}
