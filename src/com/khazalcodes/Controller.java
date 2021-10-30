package com.khazalcodes;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The controller initiates any and all actions be it reading and writing to the db or prompting input from the screen.
 * Upon start, the csv will be read and the user will be taken to the home menu where they can make 1 of 6 choices.
 * View the library, search for a title, add a dvd, remove a dvd and finally quit. Unless if the user decides to quit,
 * the controller will be taking the user back and forth between the various menus, and input prompts. Upon quitting,
 * all changes are saved to the csv.
 * */
public class Controller {

    // The user will have an enumerated list of choices whenever a menu pops up (see Menus class for more details).
    // The code is set up in a way that it is practically impossible to input an invalid choice through the terminal.
    // If more time were available, this would be turned into a runtime exception but I'll leave that for the next assignment ;)/
    private static final String NULL_CHOICE = "null action received. This should not happen. Please quit program and see code";

    // These constants are use when creating a temporary hashmap to store the edited values of a dvd when editing. Once the user
    // confirms they want to save the edits. The HashMap values are copied over to the pertinent dvd's setter methods.
    // This temporary HashMap avoids having to declare a new DVDModel which would get added as a new entry.
    private static final String TITLE_MAP_KEY = "title";
    private static final String RELEASE_DATE_MAP_KEY = "releaseDate";
    private static final String RATING_MAP_KEY = "rating";
    private static final String DIRECTOR_MAP_KEY = "director";
    private static final String STUDIO_MAP_KEY = "studio";
    private static final String REVIEW_MAP_KEY = "review";

    // This is the daddy. When the library is read, it saves the lines of the csv as HashMap entries to this object.
    // This is where all the
    public static Map<Integer, DVDModel> DVDHashMap = new HashMap<>();

    /**
     * The point at which the library starts up. The csv is read and returned through the LibraryDao. It is assumed that
     * the user wants to interact with the library. An idiomatic flag has been named accordingly and set to true. A while
     * loop uses this assumption to infinitely allow the user to interact in various ways unless if the user whishes to
     * quit whereby the flag will be set to false, the library saved, and the program exited. Within the while loop,
     * The View object is invoked to display the available choices and ask the user to choose something. This is an int
     * that is then converted to a HomeChoice enum and returned to the while loop upon which the controller will direct
     * the user to the appropriate menu based on the enum value.
     * */
    public static void startLibrary() {
        View.welcomeMessage();
        DVDHashMap = LibraryDao.readLibrary();

        boolean userWantsToInteract = true;

        while (userWantsToInteract) {
            HomeAction userChoice = View.homeMenu();

            if (userChoice == null) {
                System.out.println(NULL_CHOICE);
                break;
            }

            switch (userChoice) {
                case VIEW: viewLibrary(); break;
                case SEARCH: searchLibrary(); break;
                case ADD: addDVD(); break;
                case DELETE: removeDVD(); break;
                case EDIT: editDVD(); break;
                case QUIT: userWantsToInteract = false; break;
            }
        }

        LibraryDao.saveLibrary(DVDHashMap);
        View.goodbyeMessage();
    }

    /**
     * Prints all dvds within the library (HashMap) via the View object.
     * */
    public static void viewLibrary() { View.printDVDs(DVDHashMap); }

    /**
     * This will invoke the View to prompt the user to input a title or beginning of a title to search for. The string
     * is returned, converted to lowercase, and then used within a stream filter that converts all the titles to
     * lowercase as well and finds any matches that start with the inputted sequence. These matches are sent to the
     * view to be printed.
     * */
    public static void searchLibrary() {
        String titleToSearch = View.promptTitleSearch();
        if (titleToSearch.isEmpty()) {
            viewLibrary();
        } else {
            Map<Integer, DVDModel> filteredDVDHashMap = DVDHashMap.entrySet().stream()
                    .filter(e -> e.getValue()
                            .getTitle()
                            .toLowerCase(Locale.ROOT)
                            .startsWith(titleToSearch))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            View.printSearchResults(filteredDVDHashMap);
        }
    }

    /**
     * Invokes the view several times to ask for user input for all details of the new dvd to be added. Once all values
     * have been received, a new DVDModel will be instantiated using them and then added to the HashMap that contains
     * the rest of the library.
     * */
    public static void addDVD() {
        String title = View.promptRegularString(RegularStringInputs.TITLE);
        String releaseDate = View.promptReleaseDateInput();
        String rating = View.promptRatingInput();
        String director = View.promptRegularString(RegularStringInputs.DIRECTOR);
        String studio = View.promptRegularString(RegularStringInputs.STUDIO);
        String review = View.promptRegularString(RegularStringInputs.REVIEW);

        DVDModel newDVD = new DVDModel(title, releaseDate, rating, director, studio, review);

        DVDHashMap.put(newDVD.getKey(), newDVD);
    }

    /**
     * When a user wishes to edit or delete a record, the view will prompt the user to input the id of the record they
     * wish to affect. The user will most likely not remember what the specific id for the record they need is,
     * especially when the library grows in size. Thus, this method will ask the user if they wish to do a search
     * by title to find all of its details, specifically the ID which they can then use to continue with the operation.
     */
    private static void preliminarySearch() {
        BinaryChoice wantsPreliminarySearch = View.promptSearchCheck();

        while (wantsPreliminarySearch == BinaryChoice.YES) {
            searchLibrary();
            wantsPreliminarySearch = View.promptSearchSatisfaction();
        }
    }

    /**
     * Sets two flag variables. 1. userWantsToEdit allows a while loop to constantly show the edit menu until a
     * terminating action is taken, similar to what is going on in startLibrary(). 2. userWantsToSaveEdit signals
     * whether the user wants to confirm that the edits take place. This is set to false initially and only true
     * when the user chooses the confirm EditChoice. Checks if the user wants to search first. Prompts the id of the
     * dvd to edit and sets a reference to it in the DVDHashMap. If the user confirms that they want to edit that
     * dvd, they will be taken to the while loop. Otherwise, they are sent back to the home menu. Before entering the
     * loop, an empty HasMap edit is declared which will hold all the values that need to overwrite what is currently
     * in the DVD HashMap. The View is invoked to show the Edit menu, in exactly the same fashion as the Home menu.
     * Once an EditChoice enum is returned, the user will be directed to the appropriate prompt. If the user does not
     * cancel, or initiate the save sequence, the View will prompt the user to input the appropriate detail (say
     * release date for example) and then once that value has been parsed, validated and returned, it will immediately
     * get saved to the temporary HashMap entry with the appropriate key. These values can be prompted for and overwritten
     * ad infinitum (or the JVM runs out of stack frames). Once the user confirms, userWantsToSaveEdit will be set to true,
     * the user will be prompted to confirm whether they wish to save the changes and the method will exit thereafter.
     * */
    public static void editDVD() {
        boolean userWantsToEdit = true;
        boolean userWantsToSaveEdit = false;

        preliminarySearch();

        int idToEdit = View.promptIdInput(DVDHashMap.keySet());
        DVDModel original = DVDHashMap.get(idToEdit);
        BinaryChoice confirmation = View.promptActionConfirmation(HomeAction.EDIT, original);

        if (confirmation == BinaryChoice.YES) {
            Map<String, String> edit = new HashMap<>();

            while (userWantsToEdit) {
                EditAction userChoice = View.editMenu();

                if (userChoice == null) {
                    System.out.println(NULL_CHOICE);
                    break;
                }

                switch (userChoice) {
                    case TITLE: edit.put(TITLE_MAP_KEY, View.promptEditTitle(edit.get(TITLE_MAP_KEY))); break;
                    case RELEASE_DATE: edit.put(RELEASE_DATE_MAP_KEY, View.promptEditReleaseDate(edit.get(RELEASE_DATE_MAP_KEY))); break;
                    case RATING: edit.put(RATING_MAP_KEY, View.promptEditRating(edit.get(RATING_MAP_KEY))); break;
                    case DIRECTOR: edit.put(DIRECTOR_MAP_KEY, View.promptEditDirector(edit.get(DIRECTOR_MAP_KEY))); break;
                    case STUDIO: edit.put(STUDIO_MAP_KEY, View.promptEditStudio(edit.get(STUDIO_MAP_KEY))); break;
                    case REVIEW: edit.put(REVIEW_MAP_KEY, View.promptEditReview(edit.get(REVIEW_MAP_KEY))); break;
                    case CANCEL: userWantsToEdit = false; break;
                    case SAVE: userWantsToSaveEdit = true; break;
                }

                if (userWantsToSaveEdit) {
                    BinaryChoice confirmSavingOfEdit = View.promptEditSaveConfirmation(original, edit);

                    if (confirmSavingOfEdit == BinaryChoice.YES) {
                        saveEdit(original, edit);
                        userWantsToEdit = false;
                    }

                    userWantsToSaveEdit = false;
                }
            }
        }
    }
    // Helper method for the editDvd method. Check for all present keys and overwrites the dvd's fields with their values.
    public static void saveEdit(DVDModel dvd, Map<String, String> edit) {
        if (edit.containsKey(TITLE_MAP_KEY)) {
            dvd.setTitle(edit.get(TITLE_MAP_KEY));
        }
        if (edit.containsKey(RELEASE_DATE_MAP_KEY)) {
            dvd.setReleaseDate(edit.get(RELEASE_DATE_MAP_KEY));
        }
        if (edit.containsKey(RATING_MAP_KEY)) {
            dvd.setRating(edit.get(RATING_MAP_KEY));
        }
        if (edit.containsKey(DIRECTOR_MAP_KEY)) {
            dvd.setDirector(edit.get(DIRECTOR_MAP_KEY));
        }
        if (edit.containsKey(STUDIO_MAP_KEY)) {
            dvd.setStudio(edit.get(STUDIO_MAP_KEY));
        }
        if (edit.containsKey(REVIEW_MAP_KEY)) {
            dvd.setReview(edit.get(REVIEW_MAP_KEY));
        }
    }

    /**
     * Check if user wants to search by title for Id first. Invokes View to prompt for ID, confirms deletion choice,
     * removes DVD from HashMAp
     * */
    public static void removeDVD() {
        preliminarySearch();

        int idToDelete = View.promptIdInput(DVDHashMap.keySet());
        BinaryChoice confirmation = View.promptActionConfirmation(HomeAction.DELETE, DVDHashMap.get(idToDelete));

        if (confirmation == BinaryChoice.YES) {
            DVDHashMap.remove(idToDelete);
        }
    }
}

