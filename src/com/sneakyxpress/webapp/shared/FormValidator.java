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
    }

    private static boolean containsIllegal(String toExamine) {
        String[] arr = toExamine.split("[~#@*+{}<>\\[\\]|\\_^]", 2);
        return arr.length > 1;
    }
}
