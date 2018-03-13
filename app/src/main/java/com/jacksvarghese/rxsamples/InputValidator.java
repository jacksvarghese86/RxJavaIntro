package com.jacksvarghese.rxsamples;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jacksvarghese on 3/11/18.
 */

public class InputValidator {

    public static final Pattern VALID_USERNAME = Pattern.compile("([a-zA-Z]{3,15})$");
    public static final Pattern VALID_PASSWORD = Pattern.compile("([a-zA-Z0-9@*#]{8,15})$");
    public static final Pattern VALID_EMAIL_ADDRESS =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        return VALID_EMAIL_ADDRESS.matcher(emailStr).matches();
    }

    public static boolean validateUserName(String emailStr) {
        return VALID_USERNAME.matcher(emailStr).matches();
    }

    public static boolean validatePassword(String emailStr) {
        return VALID_PASSWORD.matcher(emailStr).matches();
    }
}
