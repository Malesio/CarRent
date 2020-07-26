package org.krytonspace.carrent.controllers.utils;

import org.krytonspace.carrent.controllers.exceptions.InvalidDataException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A class containing various checks for user input.
 */
public final class Requirements {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("([0-9]{2}\\s*){5}");
    private static final Pattern MAIL_ADDRESS_PATTERN = Pattern.compile("\\S+@\\S+\\.[a-z]{2,4}");

    public static void nonEmpty(String... str) throws InvalidDataException {
        if (Arrays.stream(str).map(String::trim).anyMatch(String::isEmpty)) {
            throw new InvalidDataException("Field(s) cannot be empty.");
        }
    }

    public static Date validateDate(String pattern) throws InvalidDataException {
        try {
            return DATE_FORMAT.parse(pattern.trim());
        } catch (ParseException e) {
            throw new InvalidDataException("'" + pattern + "' is not a valid date.");
        }
    }

    public static void validPostalCode(String raw) throws InvalidDataException {
        try {
            int postalCode = Integer.parseInt(raw.trim());
            if (postalCode < 1000 || postalCode > 98890) {
                throw new InvalidDataException("Postal code must be between 01000 and 98890");
            }
        } catch (NumberFormatException e) {
            throw new InvalidDataException("'" + raw + "' is not a valid postal code.");
        }
    }

    public static void phoneNumber(String raw) throws InvalidDataException {
        if (!PHONE_NUMBER_PATTERN.matcher(raw.trim()).matches()) {
            throw new InvalidDataException("'" + raw + "' is not a valid phone number.");
        }
    }

    public static void mailAddress(String raw) throws InvalidDataException {
        if (!MAIL_ADDRESS_PATTERN.matcher(raw.trim()).matches()) {
            throw new InvalidDataException("'" + raw + "' is not a valid mail address.");
        }
    }

    public static int validatePositiveNumber(String raw) throws InvalidDataException {
        try {
            int n = Integer.parseInt(raw);
            if (n < 0) {
                throw new InvalidDataException("'" + raw + "' must be positive.");
            }

            return n;
        } catch (NumberFormatException e) {
            throw new InvalidDataException("'" + raw + "' is not a positive number.");
        }
    }

    public static void nonNull(Object... values) throws InvalidDataException {
        if (!Arrays.stream(values).allMatch(Objects::nonNull)) {
            throw new InvalidDataException("Value(s) must not be null.");
        }
    }

    public static void positive(int n) throws InvalidDataException {
        if (n < 0) {
            throw new InvalidDataException("Value must be >= 0.");
        }
    }
}
