package au.edu.unsw.infs3605.aboriginalplatform.utils;

import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegUtil {
    /**
     * Regex of email.
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * Regex of url.
     */
    public static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";


    public static boolean isMatch(String regex, String string) {
        return !isNullString(string) && Pattern.matches(regex, string);
    }

    public static boolean isNullString(@Nullable String str) {
        return str == null || str.length() == 0 || "null".equals(str);
    }

    private final static SimpleArrayMap<String, String> CITY_MAP = new SimpleArrayMap<>();

    private RegUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether input matches regex of email.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isEmail(final CharSequence input) {
        return isMatch(REGEX_EMAIL, input);
    }

    /**
     * Return whether input matches regex of url.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isURL(final CharSequence input) {
        return isMatch(REGEX_URL, input);
    }


    /**
     * Return whether input matches the regex.
     *
     * @param regex The regex.
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    /**
     * Return the list of input matches the regex.
     *
     * @param regex The regex.
     * @param input The input.
     * @return the list of input matches the regex
     */
    public static List<String> getMatches(final String regex, final CharSequence input) {
        if (input == null) {
            return Collections.emptyList();
        }
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * Splits input around matches of the regex.
     *
     * @param input The input.
     * @param regex The regex.
     * @return the array of strings computed by splitting input around matches of regex
     */
    public static String[] getSplits(final String input, final String regex) {
        if (input == null) {
            return new String[0];
        }
        return input.split(regex);
    }

    /**
     * Replace the first subsequence of the input sequence that matches the
     * regex with the given replacement string.
     *
     * @param input       The input.
     * @param regex       The regex.
     * @param replacement The replacement string.
     * @return the string constructed by replacing the first matching
     * subsequence by the replacement string, substituting captured
     * subsequences as needed
     */
    public static String getReplaceFirst(final String input,
                                         final String regex,
                                         final String replacement) {
        if (input == null) {
            return "";
        }
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }

    /**
     * Replace every subsequence of the input sequence that matches the
     * pattern with the given replacement string.
     *
     * @param input       The input.
     * @param regex       The regex.
     * @param replacement The replacement string.
     * @return the string constructed by replacing each matching subsequence
     * by the replacement string, substituting captured subsequences
     * as needed
     */
    public static String getReplaceAll(final String input,
                                       final String regex,
                                       final String replacement) {
        if (input == null) {
            return "";
        }
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }
}
