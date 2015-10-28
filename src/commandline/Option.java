package commandline;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;



/**
 * Created by Joachim on 21/10/2015.
 */
public class Option {
    private String shortFlag;
    private String longFlag;
    private int args = 0;
    private boolean isRequired = false;
    private Type type;
    private String[] value = null;
    private String regex = null;
    private String description;

    public Option(String shortFlag, String longFlag, int args, boolean isRequired, Type type, String description, String regex) throws Exception {
        if(regex != null && !this.isRegex(regex))
            throw new Exception("The given regular expression \" " + regex + "\" is invalid.");
        this.shortFlag = shortFlag;
        this.longFlag = longFlag;
        this.args = args;
        this.isRequired = isRequired;
        this.type = type;
        this.regex = regex;
        this.description = description;
        this.value = new String[args];
    }

    public Option(String shortFlag, String longFlag, int args, boolean isRequired, Type type, String description) throws Exception {
        this.loadRegex(type);
        this.shortFlag = shortFlag;
        this.longFlag = longFlag;
        this.args = args;
        this.isRequired = isRequired;
        this.type = type;
        this.description = description;
        this.value = new String[args];
    }

    public void loadRegex(Type type) {
        switch(type) {
            case INTEGER:
                this.regex = "(-|)\\d\\d*";
                break;
            case STRING:
                this.regex = ".*";
                break;
            case FILE:
                this.regex = "..*\\...*";
                break;
            case BOOLEAN:
                this.regex = "[Tt][Rr][Uu][Ee]|[Ff][Aa][Ll][Ss][Ee]";
                break;
        }
    }

    public static boolean isRegex(String regex) {
        boolean isRegex;
        try {
            Pattern.compile(regex);
            isRegex = true;
        } catch (PatternSyntaxException e) {
            isRegex = false;
        }
        return isRegex;
    }

    public boolean isRequired() {
        return this.isRequired;
    }

    public Type getType(){
        return this.type;
    }

    public int getInt() throws Exception {
        if (this.type == Type.INTEGER)
            return Integer.parseInt(this.value[0]);
        else
            throw new Exception("Value is not of type Integer.");
    }

    public boolean getBoolean() throws Exception {
        if (this.type == Type.BOOLEAN)
            return Boolean.valueOf(this.value[0]);
        else
            throw new Exception("Value is not of type Boolean.");
    }

    public String getFile() throws Exception {
        if (this.type == Type.FILE)
            return this.value[0];
        else
            throw new Exception("Value is not of type File.");
    }

    public String getString() throws Exception {
        if (this.type == Type.STRING)
            return this.value[0];
        else
            throw new Exception("Value is not of type String.");
    }

    public int getArgs() {
        return args;
    }

    public String getLongFlag() {
        return longFlag;
    }

    public String getShortFlag() {
        return shortFlag;
    }

    public boolean hasArg() {
        return this.args > 0;
    }

    public String getValue(int valNum) throws Exception {
        if(valNum <= this.args)
            return this.value[valNum-1];
        else
            throw new Exception("Option has no Argument with the index " + valNum + ".");
    }

    public void setValue(String[] values) throws Exception {
        if(values.length == this.args) {
            for (int i = 0; i < values.length; i++) {
                if (values[i].matches(regex)) {
                    this.value[i] = values[i];
                } else
                    throw new Exception("Input value does not match the given regular expression.");
            }
        }
    }

    public void setValue(String value, int index) throws Exception {
        if(index < this.args)
            if(value.matches(this.regex)) {
                this.value[index] = value;
            } else
                throw new Exception("Input value \"" + value + "\" does not match the given regular expression \"" + this.regex + "\"");
        else
            throw new Exception("Index " + index + " violates the condition index < this.args.");
    }

    public void setValue(String value) throws Exception {
        if(this.args <= 1)
            throw new Exception("Option yields more than 1 argument.");
        if(value.matches(this.regex))
            this.value[0] = value;
        else
            throw new Exception("Input value does not match the given regular expression.");
    }

    public String getDescription() {
        return description;
    }

    public void print() {
        System.out.println("Option: " + this.longFlag + ", " + this.shortFlag);
        System.out.println("Number of arguments to take: " + this.args);
        System.out.println("Is option required: " + this.isRequired);
        System.out.println("Type: " + this.type);
        System.out.println("Values: " + this.value);
        System.out.println("Regex: " + this.regex);
    }
}
