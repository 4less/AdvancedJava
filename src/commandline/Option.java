package commandline;

/**
 * Created by Joachim on 03/11/2015.
 */
public class Option<T> extends OptionBase {
    private T value;
    private boolean argOptional = false;
    final boolean space = true;

    public Option(char identifier) {
        this.setIdentifier(identifier);
        this.setValue(value);
    }

    public void setValue(T value) {
        this.value = value;
        this.setSet(true);
    }

    public boolean isArgOptional() {
        return argOptional;
    }

    public boolean isSpace() {
        return space;
    }
}
