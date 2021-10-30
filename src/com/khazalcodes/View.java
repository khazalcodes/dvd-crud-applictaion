package com.khazalcodes;

import java.util.*;
/**
 * Where all the user interaction magic happens. Several messages are stored as constants. If there is anything to do with a menu interaction, the ints taken in
 * will be processed into Enums that are returned to the Controller. This circumvents having to have duplicate constants
 * across classes. Validation and actual user input is offloaded to the ValidInput. Generally the View class will only
 * return once something valid has been inputted by the user, this way, input handling and error checking have been automated under
 * the hood for both the Controller but also the View.
 * */
public class View {
    private static final String WELCOME_MESSAGE = "Welcome to the DVD library.";
    private static final String GOODBYE_MESSAGE = "Thanks for using the library. See you soon";
    private static final String ENTER_NUMBER_HOME = "Enter the number of the corresponding action you wish to take:";
    private static final String ENTER_NUMBER_EDIT = "Enter the number of the corresponding detail you wish to edit" +
            " or press 7 to cancel and return to the home menu:";
    public static void welcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }
    public static void goodbyeMessage() {
        System.out.println(GOODBYE_MESSAGE);
    }

    /**
     * Prints the a prompt message along with the Home menu (stored in the Menus constant class) before requesting valid
     * input from the user. Once that has been received, the input is converted to a HomeAction enum and passed back
     * to the controller.
     * */
    public static HomeAction homeMenu() {
        System.out.println("\n" + ENTER_NUMBER_HOME);
        System.out.println(Menus.HOME_MENU);

        int userInput = ValidInput.homeChoice();

        switch (userInput) {
            case 1: return HomeAction.VIEW;
            case 2: return HomeAction.SEARCH;
            case 3: return HomeAction.ADD;
            case 4: return HomeAction.DELETE;
            case 5: return HomeAction.EDIT;
            case 6: return HomeAction.QUIT;
        }

        return null;
    }

    /**
     * Same exact behaviour as the homeMenu method but for editing. Something tells me there must be a design pattern
     * that can make this more DRY but the costs outweigh the benefits in this example.
     * */
    public static EditAction editMenu() {
        System.out.println("\n" + ENTER_NUMBER_EDIT);
        System.out.println(Menus.EDIT_MENU);

        int userInput = ValidInput.editChoice();

        switch (userInput) {
            case 1: return EditAction.TITLE;
            case 2: return EditAction.RELEASE_DATE;
            case 3: return EditAction.RATING;
            case 4: return EditAction.DIRECTOR;
            case 5: return EditAction.STUDIO;
            case 6: return EditAction.REVIEW;
            case 7: return EditAction.CANCEL;
            case 8: return EditAction.SAVE;
        }

        return null;
    }

    /**
     * Takes the big daddy library HashMap and calls for every element to be printed.
     * */
    public static void printDVDs(Map<Integer, DVDModel> DVDHashMap) {
        DVDHashMap.forEach((key, value) -> printDVDModel(value));
    }

    /**
     * This should be moved to a toString Overriding method in DVDModel but time is of the essence at 00:55 30/10/2021
     * */
    public static void printDVDModel(DVDModel model) {
        System.out.printf("%d.\t%s\t%s\t%s\t%s\t%s\t%s\n",
                model.getKey(), model.getTitle(), model.getReleaseDate(), model.getRating(), model.getDirector(),
                model.getStudio(), model.getReview());
    }


    ////////////////////////////////////////////////////////////////
    // Most of the following methods are self-explanatory, but I will explain those that require some background.
    ///////////////////////////////////////////////////////////////

    public static void printSearchResults(Map<Integer, DVDModel> resultsHashMap) {
        if (resultsHashMap.isEmpty()) {
            System.out.println("No titles begin with that sequence");
        } else {
            System.out.println("The following title(s) matches that sequence\n");
            printDVDs(resultsHashMap);
        }
    }

    /**
     * For the 4 fields (Title, Director, Studio, Review )that do not require any validation, or checks because they can
     * theoretically be an arbitrarily long string, this method is used. the name of the field is passed to make the
     * prompt message relevant and if an empty string is entered, it is defaulted to "NA".
     * */
    public static String promptRegularString(String resultName) {
        String result;
        System.out.printf("Please enter the %s:\n", resultName);
        result = UserInput.asString();

        if (result.isEmpty()) {
            result = "NA";
        }

        return result;
    }

    public static String promptTitleSearch() {
        System.out.println("You can search for DVDs by their title.\nThe Library will search for any and all DVD titles" +
                "that start with the string you enter (case insensitive).");
        System.out.println("Please enter the title - or the first few characters of it - you  so search for: ");

        return UserInput.asString().toLowerCase(Locale.ROOT);
    }

    public static String promptReleaseDateInput() {
        StringBuilder date = new StringBuilder();
        System.out.println("Please enter the date as numbers conforming to the dd/mm/yyyy format.\n");
        System.out.println("WARNING: pay very close attention to the day numbers in combination with the month.\n " +
                "If you input a 30/02/2020 or a 31/11/1919, i.e an invalid date, the code will default to approximating" +
                "what it thinks you mean which in this case, the day will be shown as the last day of that month\n");

        System.out.println("Day (numbers 1-28/29/30/31) :");
        int day = ValidInput.day();
        if (day < 10) {
            date.append("0");
        }
        date.append(day);

        System.out.println("Month (numbers between 1-12) :");
        int month = ValidInput.month();
        if (month < 10) {
            date.append("0");
        }
        date.append(month);

        System.out.println("Year: ");
        int year = ValidInput.year();
        date.append(year);

        return date.toString();
    }

    public static BinaryChoice promptSearchCheck() {
        System.out.println("This action requires that you input the ID (<ID>. <DVD details>) of the DVD. " +
                "\nWould you like to search for the title to double check the ID first?");
        System.out.println(Menus.YES_NO_MENU);
        return ValidInput.yesNo();
    }

    public static BinaryChoice promptSearchSatisfaction() {
        System.out.println("Do you want to continue searching?");
        System.out.println(Menus.YES_NO_MENU);
        return ValidInput.yesNo();
    }

    public static int promptIdInput(Set<Integer> validIds) {
        System.out.println("Please enter the Id of the DVD you want to select:");
        return ValidInput.id(validIds);
    }

    public static String promptRatingInput() {
        System.out.println("Enter the numbered of rating you wish to add: ");
        System.out.println(Menus.AGE_RATING_MENU);
        return ValidInput.rating();
    }

    /**
     * This is the initial confirmation prompt that determines prompts the user to double down on their decision to edit
     * or delete a dvd after they have input the id. If yes, the user will be directed to the next steps, which in
     * the deletion's case is being taken back to the home menu.
     * */
    public static BinaryChoice promptActionConfirmation(HomeAction a, DVDModel dvd) {
        System.out.printf("Are you sure you wish to %s this record?\n\n", a.name());
        printDVDModel(dvd);
        System.out.println(Menus.YES_NO_MENU);
        return ValidInput.yesNo();
    }

    /**
     * This will prompt the user to confirm that they wish to save their edited changes to a dvd. The original details
     * will be showen followed by what has been changed. If yes, the dvd's details will be overwritten in the Controller.
     * */
    public static BinaryChoice promptEditSaveConfirmation(DVDModel original, Map<String, String> edit) {
        System.out.println("Are you sure you wish to Save the following changes?");

        System.out.println("\nORIGINAL: ");
        printDVDModel(original);

        System.out.println("\nEDIT: ");
        edit.forEach((key, value) -> System.out.println(key + " has been changed to " + value));

        System.out.println(Menus.YES_NO_MENU);
        return ValidInput.yesNo();
    }

    public static String promptEditTitle(String current) {
        if (current != null) {
            System.out.printf("The current title is: %s\n\n", current);
        }

        System.out.println("What would you like the new title to be?");
        return promptRegularString(RegularStringInputs.TITLE);
    }

    public static String promptEditReleaseDate(String current) {
        if (current != null) {
            System.out.printf("The current release date is: %s\n\n", current);
        }

        System.out.println("What would you like the new release date to be?\n");
        return promptReleaseDateInput();
    }

    public static String promptEditRating(String current) {
        if (current != null) {
            System.out.printf("The current age rating is: %s\n\n", current);
        }

        System.out.println("What would you like the new rating to be?\n");
        return promptRatingInput();
    }

    public static String promptEditDirector(String current) {
        if (current != null) {
            System.out.printf("The current director date  is: %s\n\n", current);
        }

        System.out.println("What would you like the new director to be?\n");
        return promptRegularString(RegularStringInputs.DIRECTOR);
    }

    public static String promptEditStudio(String current) {
        if (current != null) {
            System.out.printf("The current studio is: %s\n\n", current);
        }

        System.out.println("What would you like the new studio to be?\n");
        return promptRegularString(RegularStringInputs.STUDIO);
    }

    public static String promptEditReview(String current) {
        if (current != null) {
            System.out.printf("The current review is: %s\n\n", current);
        }

        System.out.println("What would you like the new review to be?\n");
        return promptRegularString(RegularStringInputs.REVIEW);
    }

}
