package commandline;

import com.sun.org.apache.xpath.internal.operations.*;

import java.lang.String;
import java.util.HashMap;

/**
 * Created by Joachim on 02/11/2015.
 */
public class UtilityParser {
    private String[] args;
    private Operand[] operands;
    private HashMap<Character, OptionBase> toOption;

    public UtilityParser(String[] args) {

    }

    public void parseSingleArg() {

    }

    public void parseMultiFlag(int pos) {
        if(pos < args.length) {
            char[] flags = args[pos].substring(1,args[pos].length()).toCharArray();
            boolean hasOption = false;
            for (int i = 0; i < flags.length; i++) {
                char id = flags[i];
                if(hasOption(id)) {
                    Object optionBase  = toOption.get(id);
                    if (optionBase instanceof Command)
                        ((Command) optionBase).setValue(true);
                    else if (optionBase instanceof Option) {
                        if (hasOption)
                            System.out.println("Argument with multiple " +
                                    "flags already has an option which " +
                                    "takes an option argument. In a " +
                                    "argument with multiple flags only one " +
                                    "option may take an option argument");
                        else {
                            Option option = (Option) optionBase;
                            if(option.isArgOptional()) {
                                System.out.println("In an argument with " +
                                        "multiple flags, flags are " +
                                        "disallowed that take an optional " +
                                        "option argument.");
                            }
                        }
                    } else if (optionBase instanceof MultiArgOption) {
                        System.out.println("Options that take multiple" +
                                " option arguments are not allowed.");
                    } else {
                        System.out.println("The given flag -" + flags[i] + " is not specified in this program");
                    }
                }
            }
        }
    }

    public void parseSingleFlag(int pos) {
        if(pos < args.length) {
            char flag = args[pos].charAt(1);
            if(hasOption(flag)) {
                Object optionBase = toOption.get(flag);
                if (optionBase instanceof Command) {
                    ((Command) optionBase).setValue(true);
                } else if (optionBase instanceof Option) {
                    Option option = (Option) optionBase;
                    if(option.isArgOptional()) {
                        option.isMandatory();
                    }
                } else if (optionBase instanceof MultiArgOption) {
                    MultiArgOption mArgOption = (MultiArgOption) optionBase;
                }
            }
        }
    }

    public void parse() {

    }

    public boolean hasOption(char option) {
        for (char c : toOption.keySet())
            if(c == option) return true;
        return false;
    }
}
