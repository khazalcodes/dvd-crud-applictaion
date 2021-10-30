package com.khazalcodes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

/**
 * This class handles all input validation. In all cases, the UserInput class is invoked to get input from the user.
 * That input is then passed to a boolean is****() method which checks if that input is valid based on the effective
 * if statement defined in the boolean function based on the constant values defined at the start of the class
 * */
public class ValidInput {
    private static final String INVALID_INTEGER_INPUT_MESSAGE = "You have entered an invalid input. Please select the " +
            "number that corresponds with the following";

    // These values are based on the options available in the Menus strings. Apart from rating which simply translates
    // to a string that the user is forced to pick as number for standardisation purposes, the choices are also reflected
    // in enum classes.
    private static final int MIN_HOME_CHOICE = 1;
    private static final int MAX_HOME_CHOICE = 6;
    private static final int MIN_EDIT_CHOICE = 1;
    private static final int MAX_EDIT_CHOICE = 8;
    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 7;

    private static final int MIN_DAY = 1;
    private static final int MAX_DAY = 31;
    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;
    private static final int MIN_YEAR = 1888; // The year the earliest film was produced.
    private static final int MAX_YEAR = LocalDate.now().getYear();
    private static final int YES = 1;
    private static final int NO = 2;

    public static int homeChoice() {
        int userInput = UserInput.asInt();

        while (!isValidHomeChoice(userInput)) {
            System.out.println("\n" + INVALID_INTEGER_INPUT_MESSAGE + " actions:");
            System.out.println(Menus.HOME_MENU);
            userInput = UserInput.asInt();
        }

        return userInput;
    }

    public static int editChoice() {
        int userInput = UserInput.asInt();

        while (!isValidEditChoice(userInput)) {
            System.out.println("\n" + INVALID_INTEGER_INPUT_MESSAGE + " actions:");
            System.out.println(Menus.EDIT_MENU);
            userInput = UserInput.asInt();
        }

        return userInput;
    }

    public static BinaryChoice yesNo() {
        int response = UserInput.asInt();

        while (!isValidYesNo(response)) {
            System.out.println("Invalid response, make sure you enter a number that corresponds with the choice");
            System.out.println(Menus.YES_NO_MENU);
            response = UserInput.asInt();
        }

        switch (response) {
            case 1: return BinaryChoice.YES;
            case 2: return BinaryChoice.NO;
        }

        return null;
    }

    public static int id(Set<Integer> validIds) {
        int id = UserInput.asInt();

        while (!validIds.contains(id)) {
            System.out.printf("Please input a valid Id. Id values range from %d-%d. The corresponding DVD will be shown" +
                            "once more to confirm that you wish to edit/delete it, if you wish to double check its value.",
                    Collections.min(validIds), Collections.max(validIds));

            id = UserInput.asInt();
        }

        return id;
    }

    public static int day() {
        int day = UserInput.asInt();

        while (!isValidDay(day)) {
            System.out.println("Invalid Day, make sure the number entered is between 1-28/29/30/31 :");
            day = UserInput.asInt();
        }

        return day;
    }

    public static int month() {
        int month = UserInput.asInt();

        while (!isValidMonth(month)) {
            System.out.println("Invalid Month, make sure the number entered is between 1-12 :");
            month = UserInput.asInt();
        }

        return month;
    }

    public static int year() {
        int year = UserInput.asInt();

        while(!isValidYear(year)) {
            System.out.println("Invalid Year. the must be a number between 1888 (when the first movie ever was produced)" +
                    "and the current year");
            year = UserInput.asInt();
        }

        return year;
    }

    public static String rating() {
        int userInput = UserInput.asInt();
        String rating = "NA";

        while (!isValidRating(userInput)) {
            System.out.println("\n" + INVALID_INTEGER_INPUT_MESSAGE + " ratings:");
            System.out.println(Menus.AGE_RATING_MENU);
            userInput = UserInput.asInt();
        }

        switch (userInput) {
            case 1: rating = "U"; break;
            case 2: rating = "PG"; break;
            case 3: rating = "12A"; break;
            case 4: rating = "12"; break;
            case 5: rating = "15"; break;
            case 6: rating = "18"; break;
            case 7: rating = "R18"; break;
        }

        return rating;
    }

    private static boolean isValidHomeChoice(int userInput) {
        return (userInput >= MIN_HOME_CHOICE && userInput <= MAX_HOME_CHOICE);
    }
    private static boolean isValidEditChoice(int userInput) {
        return (userInput >= MIN_EDIT_CHOICE && userInput <= MAX_EDIT_CHOICE);
    }
    private static boolean isValidRating(int userInput) {
        return (userInput >= MIN_RATING && userInput <= MAX_RATING);
    }
    private static boolean isValidDay(int userInput) {
        return (userInput >= MIN_DAY && userInput <= MAX_DAY);
    }
    private static boolean isValidMonth(int userInput) {
        return (userInput >= MIN_MONTH && userInput <= MAX_MONTH);
    }
    private static boolean isValidYear(int userInput) {
        return (userInput >= MIN_YEAR && userInput <= MAX_YEAR);
    }
    private static boolean isValidYesNo(int userInput) {
        return (userInput == YES || userInput == NO);
    }
}
