package com.example.homehelperfinder.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class ValidationUtils {

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Patterns.EMAIL_ADDRESS;

    // Phone validation pattern (Vietnamese phone numbers)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+84|0)[3|5|7|8|9][0-9]{8}$");

    // Password validation pattern (at least 6 characters, contains letter and number)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,}$");

    // Name validation pattern (only letters and spaces)
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-ZÀ-ỹ\\s]+$");

    // Private constructor to prevent instantiation
    private ValidationUtils() {
        throw new UnsupportedOperationException("ValidationUtils class cannot be instantiated");
    }

    // Check if string is empty or null
    public static boolean isEmpty(String text) {
        return TextUtils.isEmpty(text) || text.trim().isEmpty();
    }

    // Check if string is not empty
    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    // Email validation
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches() && email.length() <= Constants.MAX_EMAIL_LENGTH;
    }

    // Phone validation
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    // Password validation
    public static boolean isValidPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }
        return password.length() >= Constants.MIN_PASSWORD_LENGTH && PASSWORD_PATTERN.matcher(password).matches();
    }

    // Name validation
    public static boolean isValidName(String name) {
        if (isEmpty(name)) {
            return false;
        }
        return NAME_PATTERN.matcher(name).matches() && name.length() <= Constants.MAX_NAME_LENGTH;
    }

    // Check if passwords match
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return isNotEmpty(password) && password.equals(confirmPassword);
    }

    // User type validation
    public static boolean isValidUserType(String userType) {
        if (isEmpty(userType)) {
            return false;
        }
        return userType.equals(Constants.USER_TYPE_CUSTOMER) ||
                userType.equals(Constants.USER_TYPE_HELPER) ||
                userType.equals(Constants.USER_TYPE_ADMIN);
    }

    // Numeric validation
    public static boolean isNumeric(String text) {
        if (isEmpty(text)) {
            return false;
        }
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Integer validation
    public static boolean isInteger(String text) {
        if (isEmpty(text)) {
            return false;
        }
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Length validation
    public static boolean isValidLength(String text, int minLength, int maxLength) {
        if (isEmpty(text)) {
            return false;
        }
        int length = text.trim().length();
        return length >= minLength && length <= maxLength;
    }

    // URL validation
    public static boolean isValidUrl(String url) {
        if (isEmpty(url)) {
            return false;
        }
        return Patterns.WEB_URL.matcher(url).matches();
    }

    // Get validation error messages
    public static String getEmailError() {
        return "Please enter a valid email address";
    }

    public static String getPhoneError() {
        return "Please enter a valid phone number";
    }

    public static String getPasswordError() {
        return "Password must be at least " + Constants.MIN_PASSWORD_LENGTH + " characters and contain both letters and numbers";
    }

    public static String getNameError() {
        return "Name can only contain letters and spaces, maximum " + Constants.MAX_NAME_LENGTH + " characters";
    }

    public static String getPasswordMismatchError() {
        return "Passwords do not match";
    }

    public static String getRequiredFieldError(String fieldName) {
        return fieldName + " is required";
    }

    public static String getLengthError(String fieldName, int minLength, int maxLength) {
        return fieldName + " must be between " + minLength + " and " + maxLength + " characters";
    }
}
