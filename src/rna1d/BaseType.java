package rna1d;

import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * Created by Jogi on 21.01.2016.
 */
public enum BaseType {
    Adenine ('A', Color.LIGHTBLUE, Color.PURPLE),
    Guanine ('G', Color.LIGHTGREEN, Color.PURPLE),
    Cytosine ('C', Color.LIGHTCORAL, Color.PURPLE),
    Uracil ('U', Color.LIGHTYELLOW, Color.PURPLE);

    private char base;
    private Color baseColor;
    private Color typeColor;

    BaseType(char base, Color baseColor, Color typeColor) {
        this.base = base;
        this.baseColor = baseColor;
        this.typeColor = typeColor;
    }

    public char getBase() { return base; }
    public Color getBaseColor() {
        return baseColor;
    }
    public Color getTypeColor() { return typeColor; }

    public static BaseType getBaseType(char c, int pos) throws IOException {
        switch(c) {
            case 'A':
                return Adenine;
            case 'G':
                return Guanine;
            case 'C':
                return Cytosine;
            case 'U':
                return Uracil;
            default:
                throw new IOException(c + " at pos " + pos + " is not a valid rna base.");
        }
    }
}
