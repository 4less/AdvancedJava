package commandline;

import java.util.ArrayList;

/**
 * Created by Joachim on 03/11/2015.
 */
public class MultiArgOption<T> extends OptionBase {
    private ArrayList<T> values;
    private int numArgs;

    public MultiArgOption(int numArgs) {
        this.values = new ArrayList<T>();
        this.numArgs = numArgs;
    }

    public void setValue(T element, int index) {
        if(index < this.numArgs)
            values.set(index, element);
        this.isSet();
    }

    public T getValue(int index) {
        if(index < this.numArgs)
            return values.get(index);
        else
            System.out.println("value with the index " + index
                    + " is not available. Number of Arguments is "
                    + this.numArgs);
        return null;
    }
}
