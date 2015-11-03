package commandline;

/**
 * Created by Joachim on 03/11/2015.
 */
public class Command extends OptionBase {
    private boolean value;

    public Command(char identifier) {
        this.setIdentifier(identifier);
    }

    public void setValue(boolean value) {
        this.value = value;
        this.setSet(true);
    }
}
