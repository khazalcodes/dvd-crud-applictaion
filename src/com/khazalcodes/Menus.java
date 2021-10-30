package com.khazalcodes;

/**
 * This class holds all the menu String definitions. They were moved to a class as they were referenced in more than one
 * and did not really feel at home in any of them.
 * */
public final class Menus {
    static final String HOME_MENU = "\n1. View library\n2. Search for title\n3. Add DVD\n4. Remove DVD\n" +
            "5. Edit DVD\n6. Quit";
    static final String EDIT_MENU = "\n1. Title\n2. Release date\n3. Rating\n4. Director\n5. Studio\n6. Review\n7. Cancel\n8. Confirm\n";
    static final String AGE_RATING_MENU = "\n1. U - Universal\n2. PG - Parental Guidance\n3. 12A\n4. 12\n5. 15\n" +
            "6. 18\n7. R18";
    static final String YES_NO_MENU = "\nEnter the number of the choice you wish to make\n1. Yes\n2. No\n";
}
