package com.khazalcodes;

import java.util.Scanner;
/**
 * This class is responsible for parsing user input. Since choices are always simple, the user is always told to input
 * a number for any interaction for the sake of standardisation. Therefore, there are multiple places within the code
 * that will use the exact same logic. Hence, it was made into its own class with the methodality as a static method.
 * */
public class UserInput {
    private static final int INVALID_INTEGER = -1;
    private static final Scanner s = new Scanner(System.in); // No need to keep instantiating new Scanner objects.

    /**
     * The method will wait for a user to input something and try to parse that as an integer to be validated.
     * If the entry is not convertible, then it will return -1. This is safe since none of the validation checks
     * elsewhere have -1 within the range of valid values.
     * */
    public static int asInt() {
        String input = s.nextLine();
        int convertedInput;

        try {
            convertedInput = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            convertedInput = INVALID_INTEGER;
        }

        return convertedInput;
    }

    public static String asString() {
        return s.nextLine();
    }
}
