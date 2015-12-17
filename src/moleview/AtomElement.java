package moleview;

import javafx.scene.paint.Color;

import java.lang.annotation.ElementType;

/**
 * Created by Joachim on 09/12/2015.
 */
public enum AtomElement {
    C (Color.LIGHTGRAY),
    O (Color.RED),
    N (Color.BLUE),
    P (Color.PURPLE),
    H (Color.LIGHTBLUE),
    ANY (Color.GRAY);

    private Color color;

    AtomElement(Color color) {
        this.color = color;
    }

    public static AtomElement createElement(String element) {
        switch(element) {
            case "C":
                return AtomElement.C;
            case "O":
                return AtomElement.O;
            case "N":
                return AtomElement.N;
            case "P":
                return AtomElement.P;
            case "H":
                return AtomElement.H;
        }
        return AtomElement.ANY;
    }

    public double dist(AtomElement element) {
        if (this == element && element != AtomElement.C) {
            System.out.println("equal");
            return 0.5;
        }
        System.out.println("not equal");
        return 1.65;
    }

    public Color getColor() {
        return color;
    }
}
