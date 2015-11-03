package commandline;

/**
 * Created by Joachim on 02/11/2015.
 */
public abstract class UtilityArgument {
    private boolean mandatory = false;
    private String description;
    private String parameterName = "option_argument";
    private boolean set = false;

    public boolean isMandatory() {
        return this.mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }

    public abstract String printable();
}
