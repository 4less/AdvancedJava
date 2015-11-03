package commandline;

/**
 * Created by Joachim on 29/10/2015.
 */
public class Operand<T> extends UtilityArgument {
    private String name;
    private T value;

    public Operand(String name, boolean mandatory) {
        this.setName(name);
        this.setMandatory(mandatory);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(String name) {
        return this.name.equals(name);
    }

    @Override
    public String printable() {
        String result = "";
        result = "<" + this.getParameterName() + ">";
        if(!isMandatory())
            result = "[" + result + "]";
        return result;
    }
}
