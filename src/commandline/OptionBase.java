package commandline;
import com.sun.java.util.jar.pack.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;



/**
 * Created by Joachim on 21/10/2015.
 */
public abstract class OptionBase extends UtilityArgument {
    private char identifier;

    public char getIdentifier() {
        return identifier;
    }

    public void setIdentifier(char identifier) {
        this.identifier = identifier;
    }

    @Override
    public String printable() {
        String result = "-" + getIdentifier() + " " + "<"
                + getParameterName() + ">";
        if(this.isMandatory()) {
            result = "[" + result + "]";
        }
        return result;
    }

    public void addToMap(HashMap<String, UtilityArgument> optionMap) {
        optionMap.put(String.valueOf(this.getIdentifier()), this);
    }
}
