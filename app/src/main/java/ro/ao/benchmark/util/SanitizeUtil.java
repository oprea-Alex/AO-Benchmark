package ro.ao.benchmark.util;

import java.util.List;
import java.util.regex.Pattern;

public abstract class SanitizeUtil {

    /* Check if the given string is null or empty */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    /* Check if the given list is null or empty */
    public static boolean isNullOrEmpty(List l) {
        return l == null || l.isEmpty();
    }

    /* Check if the given email is valid */
    public static boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    /* Capitalize the first letter in a word */
    public static String capitalize(String s) {
        if (isNullOrEmpty(s))
            return s;

        char first = s.charAt(0);
        return Character.isUpperCase(first) ? s : Character.toUpperCase(first) + s.substring(1);
    }

}
