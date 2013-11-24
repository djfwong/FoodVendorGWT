package com.sneakyxpress.webapp.shared;

/**
 * Created by michael on 11/20/2013.
 */
public class FormValidator {
    public static void validateReview(int stars, String text) throws IllegalArgumentException {
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Star rating must be between 1 and 5.");
        }

        if (containsIllegal(text)) {
            throw new IllegalArgumentException("Your review text contains illegal characters."
                    + " Please remove them and submit your review again.");
        }

        if (!checkLength(text)) {
            throw new IllegalArgumentException("Sorry, your review is too long!" +
                    " Reviews must be no longer than 500 characters.");
        }
    }

    public static boolean containsIllegal(String toExamine) {
        String[] arr = toExamine.split("[~#@*+{}<>\\[\\]|\\_^]", 2);
        return arr.length > 1;
    }

    public static boolean validatePhoneNo(String toExamine) {
        boolean patternOK = toExamine.matches("^[0-9]*$");
        boolean lengthOK = toExamine.length() == 10;

        return lengthOK && patternOK;
    }

    public static boolean validateEmail(String toExamine) {
        return toExamine.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean checkLength(String toExamine) {
        return toExamine.length() <= 500;
    }
}
